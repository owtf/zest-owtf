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


//this class handles the database part, takes transaction id as input and fetches the details from the target_db and 
//converts them to custom object and ultimately to List of httpMessages

public class DBHandler {
	
	public Connection c_trans = null;
	public Connection c_tar=null;
	public Statement stmt = null;
	public List <HttpMessage> http_list=new ArrayList<HttpMessage>();
	public PreparedStatement trans_query=null;
	public PreparedStatement target_query=null;
	public List <CustomObject> cust_obj=new ArrayList<CustomObject>();
	
	public DBHandler() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
	}

	public void CreateRecordScript(List<Integer> trans_ID,List<Integer> target_ID,String target_config_path,String Output_Dir) throws HttpMalformedHeaderException{

			try{
				String url = GetDBPathforTarget(target_config_path); 
				c_tar=DriverManager.getConnection(url);
				
				for(int i=0;i<target_ID.size();i++)
				{
				target_query=PrepareStatement(c_tar,"targets");
				setQueryArgs(target_query,target_ID.get(i));
				ResultSet rs_tar = target_query.executeQuery();
				while(rs_tar.next()){
					
					String host = GetValueFromResult(rs_tar,"target_url");
					String modified_host=host.replace("//", "_").replace(':', '_');
					String target_path = Output_Dir+"/"+modified_host;
					String transaction_db_path = target_path+"/transactions.db";
					String trans_url = GetDBPathforTarget(transaction_db_path);
					Connection c = DriverManager.getConnection(trans_url);
					trans_query=PrepareStatement(c,"transactions");
					getTransaction(trans_ID.get(i));
					}
				rs_tar.close();
				}
			} 
			
			catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
			Convert_to_http();
	}

	public void CreateTargetScript(List<Integer> trans_ID,String Output_Dir) throws HttpMalformedHeaderException{
		try{
		
		String url = GetDBPathforTarget(Output_Dir+"/transactions.db");
		c_trans=DriverManager.getConnection(url);
		trans_query=PrepareStatement(c_trans,"transactions");
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
    		return "jdbc:sqlite:"+target_config_path;
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
    			String  raw_request = GetValueFromResult(rs,"raw_request");
    			String res_header= GetValueFromResult(rs,"response_headers");
    			String status_code=GetValueFromResult(rs,"response_status");
    			String res_body=GetValueFromResult(rs,"response_body");
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
