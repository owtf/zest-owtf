
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;






import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestScript;






@SuppressWarnings("deprecation")
public class Creator {
	private final static String USER_AGENT = "Mozilla/5.0";
	private static HttpClient client = HttpClientBuilder.create().build();
	
	
	public static void main(String[] args) throws Exception {
		String USER_AGENT = "Mozilla/5.0";
		String content = readFile("test.zst",StandardCharsets.UTF_8);
		ScriptType sa=new ScriptType("stand_alone","script.type.stand_alone",null,true);
		ScriptWrapper script= new ScriptWrapper();
		script.setType(sa);
		script.setContents(content);
		script.setEngine(null);
		script.setName("test_script");
		script.setDescription(null);
		script.setLoadOnStart(false);
		
		ZestScriptWrapper zsw = new ZestScriptWrapper(script);
		
		@SuppressWarnings("deprecation")
		//URI myUri = new URI("http://stackoverflow.com");
		
		
		List<String> url = new ArrayList<String>();
		url.add("http://www.google.com");
		url.add("http://owasp.org");
		
		
		List<CustomObject> obj=GetPageContent(url);
		ZestScript scr = zsw.getZestScript();
		
		for(int i=0;i<obj.size();i++)
		{
			System.out.println(obj.get(i).content);
		
		
			//System.out.println(obj.req_header);
			//byte[] b = page.getBytes(Charset.forName("UTF-8"));
			//System.out.println(b);
		
			System.out.println(obj.get(i).res_header);
		
		
			HttpMessage msg = new HttpMessage (obj.get(i).req_header,null,obj.get(i).res_header,obj.get(i).res_array);
		
			ZestRequest req = ZestZapUtils.toZestRequest(msg, false);
		
			
		
			scr.add(req);
		}
		String res=(String) ZestJSON.toString(scr);
		
		System.out.println(res);
		
		
		
		
		
	};
	
	
	



static String readFile(String path, Charset encoding) 
		 
		{
		  byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		  
		}








private static List<CustomObject> GetPageContent(List<String> url) throws Exception {
	 
	List<CustomObject>cust = new ArrayList<CustomObject>();
	
	
	for (int i=0;i<url.size();i++)
	{
	HttpGet request = new HttpGet(url.get(i));
    
    
	request.setHeader("User-Agent", USER_AGENT);
	request.setHeader("Accept",
		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
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

}








































