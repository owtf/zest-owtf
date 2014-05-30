package org.zest_owtf.mainclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.parosproxy.paros.network.HttpMalformedHeaderException;


//this class handles the database part, takes transaction id as input and fetches the details from the target_db and 
//converts them to custom object and ultimately to List of httpMessages

public class DBHandler {
	
	public Connection c = null;
	public Statement stmt = null;
	public List <HttpMessage> http_list=new ArrayList<HttpMessage>();
	public PreparedStatement trans_query=null;
	public List <CustomObject> cust_obj=new ArrayList<CustomObject>();
	
	
	public DBHandler(List<Integer> ID,String op_path) throws HttpMalformedHeaderException{
		
			try{
				String url="jdbc:sqlite:"+op_path+"/transactions.db";
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection(url);
				trans_query = c.prepareStatement("SELECT * FROM transactions where id=?");

				for(int i=0;i<ID.size();i++){
					getTransaction(ID.get(i));
				}
					
				trans_query.close();
				c.close();
			} 
			
			catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
			Convert_to_http();
	}

    private void getTransaction(int trans_ID) throws Exception{
    		
    		trans_query.setInt(1,trans_ID);
    		ResultSet rs = trans_query.executeQuery();
    		
    		while ( rs.next() ) {

    			String  raw_request = rs.getString("raw_request");
    			String res_header= rs.getString("response_headers");
    			String status_code=rs.getString("response_status");
    			String res_body=rs.getString("response_body");
            
    			/*
            	System.out.println( "ID = " + id );
            	System.out.println( "request = " + raw_request );
            	System.out.println( "response header = " + res_header );
            	System.out.println( "response code = " + status_code );
            	System.out.println( "response body  = " + res_body );
     			System.out.println();
    			 */
            
    			cust_obj.add(new CustomObject(raw_request,status_code,res_header,res_body));
    		}
    		rs.close();
    		
    }
	
	private void Convert_to_http() throws HttpMalformedHeaderException{
		 
			for(int i=0;i<cust_obj.size();i++){
				http_list.add(new HttpMessage(cust_obj.get(i).req_header,null,cust_obj.get(i).res_header,cust_obj.get(i).res_array));	
			}
	 }
}
