/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2013 The ZAP Development Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */


import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.log4j.Logger;
import org.mozilla.zest.core.v1.ZestActionFail;
import org.mozilla.zest.core.v1.ZestActionIntercept;
import org.mozilla.zest.core.v1.ZestActionInvoke;
import org.mozilla.zest.core.v1.ZestActionPrint;
import org.mozilla.zest.core.v1.ZestActionScan;
import org.mozilla.zest.core.v1.ZestActionSleep;
import org.mozilla.zest.core.v1.ZestAssertion;
import org.mozilla.zest.core.v1.ZestAssignFieldValue;
import org.mozilla.zest.core.v1.ZestAssignRandomInteger;
import org.mozilla.zest.core.v1.ZestAssignRegexDelimiters;
import org.mozilla.zest.core.v1.ZestAssignReplace;
import org.mozilla.zest.core.v1.ZestAssignString;
import org.mozilla.zest.core.v1.ZestAssignStringDelimiters;
import org.mozilla.zest.core.v1.ZestComment;
import org.mozilla.zest.core.v1.ZestConditional;
import org.mozilla.zest.core.v1.ZestControlLoopBreak;
import org.mozilla.zest.core.v1.ZestControlLoopNext;
import org.mozilla.zest.core.v1.ZestControlReturn;
import org.mozilla.zest.core.v1.ZestElement;
import org.mozilla.zest.core.v1.ZestExpressionAnd;
import org.mozilla.zest.core.v1.ZestExpressionEquals;
import org.mozilla.zest.core.v1.ZestExpressionLength;
import org.mozilla.zest.core.v1.ZestExpressionOr;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestExpressionResponseTime;
import org.mozilla.zest.core.v1.ZestExpressionStatusCode;
import org.mozilla.zest.core.v1.ZestExpressionURL;
import org.mozilla.zest.core.v1.ZestLoopFile;
import org.mozilla.zest.core.v1.ZestLoopInteger;
import org.mozilla.zest.core.v1.ZestLoopString;
import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;
import org.mozilla.zest.core.v1.ZestRuntime;
import org.mozilla.zest.core.v1.ZestScript;
import org.mozilla.zest.core.v1.ZestStatement;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMalformedHeaderException;

import org.parosproxy.paros.network.HttpResponseHeader;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.extension.script.ScriptNode;

public class ZestZapUtils {

	private static final String ZEST_VAR_VALID_CHRS = "-:.";
	
	private static final Logger log = Logger.getLogger(ZestZapUtils.class);
	
	// Only use for debugging for now, as the tree wont be fully updated if indexes change
	private static boolean showIndexes = false;


/*	public static HttpMessage toHttpMessage(ZestRequest request,
			ZestResponse response) throws URIException,
			HttpMalformedHeaderException {
		if (request == null || request.getUrl() == null) {
			return null;
		}
		HttpMessage msg = new HttpMessage(new URI(request.getUrl().toString(), false));
		if (request.getHeaders() != null) {
			try {
				msg.setRequestHeader(msg.getRequestHeader().getPrimeHeader()
						+ "\r\n" + request.getHeaders());
			} catch (HttpMalformedHeaderException e) {
				log.error(e.getMessage(), e);
			}
		}
		msg.getRequestHeader().setMethod(request.getMethod());
		msg.setRequestBody(request.getData());
		msg.getRequestHeader().setContentLength(msg.getRequestBody().length());

		if (response != null) {
			try {
				msg.setResponseHeader(new HttpResponseHeader(response.getHeaders()));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			msg.setResponseBody(response.getBody());
			msg.setTimeElapsedMillis((int) response.getResponseTimeInMs());
		}

		return msg;
	}
*/
	public static ZestResponse toZestResponse(HttpMessage msg)
			throws MalformedURLException {
		return new ZestResponse(new URL(msg.getRequestHeader().getURI().toString()), 
				msg.getResponseHeader().toString(), 
				msg.getResponseBody().toString(), 
				msg.getResponseHeader().getStatusCode(), 
				msg.getTimeElapsedMillis());
	}

	public static ZestRequest toZestRequest(HttpMessage msg, boolean replaceTokens)
			throws MalformedURLException, HttpMalformedHeaderException,
			SQLException {
		return toZestRequest(msg, replaceTokens, false);
	}

	public static ZestRequest toZestRequest(HttpMessage msg,
			boolean replaceTokens, boolean incAllHeaders) throws MalformedURLException,
			HttpMalformedHeaderException, SQLException {
		if (replaceTokens) {
			ZestRequest req = new ZestRequest();
			req.setMethod(msg.getRequestHeader().getMethod());
			if (msg.getRequestHeader().getURI() != null) {
				req.setUrl(new URL(msg.getRequestHeader().getURI().toString()));
			}
			req.setUrlToken(correctTokens(msg.getRequestHeader().getURI().toString()));
			
			if (incAllHeaders) {
				setAllHeaders(req, msg);
			} else {
				setHeaders(req, msg, true);
			}
			req.setData(correctTokens(msg.getRequestBody().toString()));
			req.setResponse(
					new ZestResponse(
							req.getUrl(), 
							msg.getResponseHeader().toString(), 
							msg.getResponseBody().toString(), 
							msg.getResponseHeader().getStatusCode(), 
							msg.getTimeElapsedMillis()));
			return req;

		} else {
			ZestRequest req = new ZestRequest();
			req.setUrl(new URL(msg.getRequestHeader().getURI().toString()));
			req.setMethod(msg.getRequestHeader().getMethod());
			if (incAllHeaders) {
				setAllHeaders(req, msg);
			} else {
				setHeaders(req, msg, true);
			}
			req.setData(msg.getRequestBody().toString());
			req.setResponse(new ZestResponse(req.getUrl(), msg
					.getResponseHeader().toString(), msg.getResponseBody()
					.toString(), msg.getResponseHeader().getStatusCode(), msg
					.getTimeElapsedMillis()));
			return req;
		}
	}

	private static void setHeaders(ZestRequest req, HttpMessage msg, boolean replaceTokens) {
		// TODO make the headers included be configurable
		String[] headers = msg.getRequestHeader().getHeadersAsString()
				.split(HttpHeader.CRLF);
		StringBuilder sb = new StringBuilder();
		for (String header : headers) {
			if (header.toLowerCase().startsWith(
					HttpHeader.CONTENT_TYPE.toLowerCase())) {
				sb.append(header);
				sb.append(HttpHeader.CRLF);
			}
		}
		if (replaceTokens) {
			req.setHeaders(correctTokens(sb.toString()));
		} else {
			req.setHeaders(sb.toString());
		}
	}

	private static void setAllHeaders(ZestRequest req, HttpMessage msg) {
		String[] headers = msg.getRequestHeader().getHeadersAsString()
				.split(HttpHeader.CRLF);
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String header : headers) {
			if (first) {
				// Drop the first one as this one will get added anyway
				first = false;
			} else {
				sb.append(header);
				sb.append(HttpHeader.CRLF);
			}
		}
		req.setHeaders(sb.toString());
	}

	private static String correctTokens(String str) {
		return str.replace("%7B%7B", "{{").replace("%7D%7D", "}}");
	}




	
	
}
