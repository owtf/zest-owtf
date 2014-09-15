package org.zest_owtf.mainclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;


//this class handles the database part, takes transaction id as input and fetches the details from the target_db and 
//converts them to custom object and ultimately to List of httpMessages

public class DBHandler {
	
	public Connection c_global = null;
	public Connection c_tar=null;
	public Statement stmt = null;
	public List <HttpMessage> http_list=new ArrayList<HttpMessage>();
	public PreparedStatement trans_query=null;
	public PreparedStatement target_query=null;
	public List <CustomObject> cust_obj=new ArrayList<CustomObject>();
	
	public DBHandler(String db_url,String db_user_id,String db_password) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String url = GetDBPathforTarget(db_url);
		c_global=DriverManager.getConnection(url,db_user_id, db_password);
		trans_query=PrepareStatement(c_global,"transactions");
	}

	public void CreateZestScript(List<Integer> trans_ID) throws HttpMalformedHeaderException{
		try{
		for(int i=0;i<trans_ID.size();i++){
			getTransaction(trans_ID.get(i));
			}
		}
		catch( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		Convert_to_http();
	}
	
    private void setQueryArgs(PreparedStatement stmt,Integer arg) throws SQLException {
    	stmt.setInt(1,arg);	
	}

	private String GetDBPathforTarget(String target_config_path) {
    		return "jdbc:postgresql://"+target_config_path;
	}
    
    private PreparedStatement PrepareStatement(Connection c,String table) throws SQLException{
    	String query = "SELECT * FROM "+table+" where id=?";
    	return c.prepareStatement(query);
    }
    
    private String GetValueFromResult(ResultSet rs,String argument) throws SQLException{
    	
    	return rs.getString(argument);
    }

	private void getTransaction(int trans_ID) throws Exception{
    		
    		setQueryArgs(trans_query,trans_ID);
    		ResultSet rs = trans_query.executeQuery();
    		while ( rs.next() ) {
    			String raw_request = GetValueFromResult(rs,"raw_request");
    			String core_req = raw_request.split("\r\n\r\n")[0];
    			String req_data = GetValueFromResult(rs,"data");
    			String res_header= GetValueFromResult(rs,"response_headers");
    			String status_code=GetValueFromResult(rs,"response_status");
    			String res_body=GetValueFromResult(rs,"response_body");
    			cust_obj.add(new CustomObject(core_req,req_data,status_code,res_header,res_body));
    		}
    		rs.close();
    		
    }

	private void Convert_to_http() throws HttpMalformedHeaderException{
		 
			for(int i=0;i<cust_obj.size();i++){
				http_list.add(new HttpMessage(cust_obj.get(i).req_header,cust_obj.get(i).req_data,cust_obj.get(i).res_header,cust_obj.get(i).res_array));	
			}
	 }
}
