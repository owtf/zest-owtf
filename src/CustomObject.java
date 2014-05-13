import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;


public class CustomObject {
	 public HttpGet request;
	 public HttpResponse response;
	 public String content;
	 public String url;
	 public Header[] h_req;
	 public Header[] h_res;
	 public String req_header;
	 public String res_header;
	 byte[] res_array ;
	 public String method;
	
	public CustomObject(HttpGet req,HttpResponse res,String uri,String meth) throws Exception {
		content=GetPageContent(res);
		response=res;
		request=req;
		url=uri;
		method=meth;
		h_res=response.getAllHeaders();
		h_req=request.getAllHeaders();
		
	/*	for (Header header : h_res) {
			System.out.println("Key : " + header.getName() 
	                           + " ,Value : " + header.getValue());
	 
		}*/
		
		
		req_header=HeadertoString(h_req,true);
		res_header=HeadertoString(h_res,false);
		res_array = content.getBytes();
		
		
		
	}

	
	private String GetPageContent(HttpResponse res) throws Exception
	{
	BufferedReader rd = new BufferedReader(
            new InputStreamReader(res.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}

	return result.toString();
	}

	
    private String HeadertoString(Header h[],boolean check){
    	
    	StringBuffer req_h= new StringBuffer();
    	if (check){
    		req_h.append(method+" "+url+" "+"HTTP/1.1\r\n");
    	}
    	else{
    		req_h.append("HTTP/1.1 "+response.getStatusLine().getStatusCode()+" OK\r\n");
    		
    	}
    	for(int i=0;i<h.length;i++)
    	{
    	 req_h.append(h[i].toString()+"\r\n");
    		
    	
    	}
    	
    	return req_h.toString();
     
    }	

	
	
	
}
