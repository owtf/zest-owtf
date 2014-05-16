package org.zest_owtf.mainclass;

import java.net.MalformedURLException;

import java.sql.SQLException;
import java.util.List;

import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestScript;
import org.parosproxy.paros.network.HttpMalformedHeaderException;

/*this class takes HTTP request or list of HTTP requests to create Zest Script from them*/




@SuppressWarnings("deprecation")
public class Creator {
	
	public static ZestScript zScr=null;
	
	
    public Creator(List<HttpMessage> msg,ZestScript scr) throws MalformedURLException, HttpMalformedHeaderException, SQLException{
    	
    	zScr=scr;
    	create(msg);
    	String res=(String) ZestJSON.toString(zScr);
    	
    	System.out.println(res);
    }
	
    public Creator(HttpMessage msg,ZestScript scr) throws MalformedURLException, HttpMalformedHeaderException, SQLException{
    	
    	zScr=scr;
    	create(msg);
    	
    	String res=(String) ZestJSON.toString(zScr);
    	
    	System.out.println(res);
    }	
	
	
private static void create(List <HttpMessage> msg) throws MalformedURLException, HttpMalformedHeaderException, SQLException{

	for(int i=0;i<msg.size();i++)
	{
		//System.out.println(obj.get(i).content);
	
	
		//System.out.println(obj.req_header);
		//byte[] b = page.getBytes(Charset.forName("UTF-8"));
		//System.out.println(b);
	
	//	System.out.println(obj.get(i).res_header);
	
	
		//HttpMessage msg = new HttpMessage (obj.get(i).req_header,null,obj.get(i).res_header,obj.get(i).res_array);
	    create (msg.get(i));
		
	
		
	
		
	}
	
	

	


}


private static void create (HttpMessage msg) throws MalformedURLException, HttpMalformedHeaderException, SQLException{
	
	ZestRequest req = ZestZapUtils.toZestRequest(msg, false);
	zScr.add(req);
}





}








































