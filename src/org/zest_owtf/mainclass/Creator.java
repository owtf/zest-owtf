package org.zest_owtf.mainclass;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestJSON;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestVariables;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

/*this class takes HTTP request or list of HTTP requests to create Zest Script from them*/

public class Creator {
	
	public static ZestScript zScr=null;
	public static ZestScriptWrapper zSw=null;
	
    public Creator(List<HttpMessage> msg,ZestScript scr,ZestScriptWrapper zsw,String Path) throws MalformedURLException, HttpMalformedHeaderException, SQLException{
    	
    	zScr=scr;
    	zSw = zsw;
    	create(msg);
    	String res=(String) ZestJSON.toString(zScr);
    	//System.out.println(res);
    	new FileWrtr(res,Path);  //Writes to the script
    	
    }
	
    public Creator(HttpMessage msg,ZestScript scr) throws MalformedURLException, HttpMalformedHeaderException, SQLException{
    	
    	zScr=scr;
    	create(msg);
    	String res=(String) ZestJSON.toString(zScr);
    	System.out.println(res);
    }	
	

    private static void create(List <HttpMessage> msg) throws MalformedURLException, HttpMalformedHeaderException, SQLException{

    	for(int i=0;i<msg.size();i++){
    		
		create (msg.get(i));
		
		}

    }


    private static void create (HttpMessage msg) throws MalformedURLException, HttpMalformedHeaderException, SQLException{
	
    	ZestRequest req = ZestZapUtils.toZestRequest(msg, false);
    	zScr.add(req);
    	if (zSw.isIncStatusCodeAssertion()) {
			req.addAssertion(new ZestAssertion(
					new ZestExpressionStatusCode(msg
							.getResponseHeader().getStatusCode())));

		}
		if (zSw.isIncLengthAssertion()) {
			req.addAssertion(new ZestAssertion(
					new ZestExpressionLength(
							ZestVariables.RESPONSE_BODY, msg
									.getResponseBody().length(), zSw
									.getLengthApprox())));
		
    }
}
}
