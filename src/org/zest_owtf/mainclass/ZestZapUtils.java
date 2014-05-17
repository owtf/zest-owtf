package org.zest_owtf.mainclass;


import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import org.mozilla.zest.core.v1.ZestRequest;
import org.mozilla.zest.core.v1.ZestResponse;


import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMalformedHeaderException;



public class ZestZapUtils {

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
