package org.zest_owtf.mainclass;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.parosproxy.paros.network.HttpMalformedHeaderException;


public class Tester {
	
	private final static String USER_AGENT = "Mozilla/5.0";
	private static HttpClient client = HttpClientBuilder.create().build();
	List<String> url = new ArrayList<String>();
	static List<CustomObject> obj= new ArrayList<CustomObject>();
	static List<HttpMessage> msg = new ArrayList<HttpMessage>();
	
	
public static void main(String[] args) throws Exception {
	
	//	Filereader rdr = new Filereader("/root/zest-owtf/url.txt",false);
	//	obj=GetPageContent(rdr.fileLineContent);
	
		String Root_Dir=args[4];
		ScriptPrepare scr= new ScriptPrepare(Root_Dir);
		
		RemoveQuotes(args);
	
		String Output_path=args[0];
		String req_h=args[1];
		String res_h=args[2];
		String res_body=args[3];
		res_h="HTTP/1.1 "+res_h;
		obj.add(new CustomObject(req_h,res_h,res_body));
		Convert_to_http();
		Creator crt = new Creator(msg,scr.scr,Root_Dir+"/"+Output_path);
		

	};
	
/*
	private static List<CustomObject> GetPageContent(List<String> url) throws Exception {
		 
		List<CustomObject>cust = new ArrayList<CustomObject>();
		
		
		for (int i=0;i<url.size();i++)
		{
		HttpGet request = new HttpGet(url.get(i));
	    
	    
		request.setHeader("User-Agent", USER_AGENT);
		request.setHeader("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,q=0.8");
		request.setHeader("Accept-Language", "en-US,en;q=0.5");
	    
		HttpResponse response = client.execute(request);
		
	    
		
		int responseCode = response.getStatusLine().getStatusCode();
	 
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		CustomObject c= new CustomObject(request,response,url.get(i),"GET");
		cust.add(c);
		
		}
		return cust;
		
	  }
*/
	
	 private static void Convert_to_http() throws HttpMalformedHeaderException{
		 
		 for(int i=0;i<obj.size();i++){
		  msg.add(new HttpMessage(obj.get(i).req_header,null,obj.get(i).res_header,obj.get(i).res_array));	
		 }
		 
	 }
	 
	 
	 private static void RemoveQuotes(String[] ip){
		 
		 for(int i=0;i<ip.length;i++){
			 if(ip[i]!=null && ip[i]!="")
				 ip[i] = ip[i].substring(1, ip[i].length() - 1);
			 
		 }
		 
	 }
	

}
