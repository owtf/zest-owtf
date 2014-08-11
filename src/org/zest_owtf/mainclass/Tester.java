package org.zest_owtf.mainclass;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.parosproxy.paros.network.HttpMalformedHeaderException;

import sun.org.mozilla.javascript.internal.NativeArray;


//mainly there are two functions of this main class - 1) Creates Zest script given HTTP request (main purpose)
//                                                    2) Creates Zest script from given urs by making a GET request (testing purpose)

//commented code is for testing purpose
public class Tester {
	/*
	private final static String USER_AGENT = "Mozilla/5.0";
	private static HttpClient client = HttpClientBuilder.create().build();
	List<String> url = new ArrayList<String>();
	static List<CustomObject> obj= new ArrayList<CustomObject>();
	static List<HttpMessage> msg = new ArrayList<HttpMessage>();
	*/
	
public static void main(String[] args) throws Exception {
	
	//	Filereader rdr = new Filereader("/root/zest-owtf/url.txt",false);
	//	obj=GetPageContent(rdr.fileLineContent);
	
	//Assigns variables from arguments
	String Root_Dir=args[0];
	String Output_Dir=args[1];
	String target_config_path=args[2];
	String Output_path=args[3];
	String[] targets=args[4].split(" ");	
	String[] items = args[5].split(" ");
	String record = args[6];
	int req_count=items.length;

	//converts string of target ids and transaction ids to List of integers
	
	List <Integer> transactions= new ArrayList<Integer>();
	for(int i=0;i<req_count;i++){
		
		transactions.add(Integer.parseInt(items[i]));
		
	}
	
	int target_count=targets.length;
	List <Integer> target_list= new ArrayList<Integer>();
	for(int i=0;i<target_count;i++){
		
		target_list.add(Integer.parseInt(targets[i]));
		
	}
	
	//Prepares the script from the template
	ScriptPrepare scr= new ScriptPrepare(Root_Dir);
	
	//gets records of transactions from database of the given target,converts it to custom objects and then to list of HttpMessage
	//CustomObject is just an intermediate representation to keep a better track of transactions
	DBHandler db_handler = new DBHandler();
	if(record.equals("True")){ //if true then record script
		db_handler.CreateRecordScript(transactions,target_list,target_config_path,Output_Dir);
	}
	else //target script
		db_handler.CreateTargetScript(transactions,Output_Dir);
	
	//Creates Zest script from List of HttpMessage
 	new Creator(db_handler.http_list,scr.scr,scr.zsw,Output_path);
	
	
};


/*
    //gets page content from list of urls and return a list of customobjects
     
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
     
	//for testing purpose
	private static void printArgs(String[] args)
	{
		for(int i=0;i<args.length;i++)
		{
			System.out.println(i+" "+args[i]+"\n");
			
		}
	
		
	
	}
	*/


}
