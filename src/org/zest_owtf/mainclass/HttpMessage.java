package org.zest_owtf.mainclass;


import org.apache.commons.httpclient.URI;

import org.apache.log4j.Logger;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;

import org.parosproxy.paros.network.HttpHeader;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpRequestHeader;
import org.parosproxy.paros.network.HttpResponseHeader;
import org.zaproxy.zap.extension.httppanel.Message;
import org.zaproxy.zap.extension.httpsessions.HttpSession;
import org.zaproxy.zap.network.HttpRequestBody;
import org.zaproxy.zap.network.HttpResponseBody;
import org.zaproxy.zap.users.User;


/**
 * Representation of a HTTP message request (header and body) and response (header and body) pair.
 * 
 */
public class HttpMessage implements Message {

	private HttpRequestHeader mReqHeader = new HttpRequestHeader();
	private HttpRequestBody mReqBody = new HttpRequestBody();
	private HttpResponseHeader mResHeader = new HttpResponseHeader();
	private HttpResponseBody mResBody = new HttpResponseBody();
	private Object userObject = null;
	private int timeElapsed = 0;
	private long timeSent = 0;
    //private String tag = "";
    // ZAP: Added note to HttpMessage
    private String note = "";
    // ZAP: Added historyRef
    private HistoryReference historyRef = null;
    // ZAP: Added logger
    private static Logger log = Logger.getLogger(HttpMessage.class);
    // ZAP: Added HttpSession
	private HttpSession httpSession = null;
	// ZAP: Added support for requesting the message to be sent as a particular User
	private User requestUser;
	// Can be set by scripts to force a break
	private boolean forceIntercept = false;

    /**
     * Flag that indicates if the response has been received or not from the target host.
     * <p>
     * Default is {@code false}.
     */
    private boolean responseFromTargetHost = false;


    public HistoryReference getHistoryRef() {
		return historyRef;
	}

	public void setHistoryRef(HistoryReference historyRef) {
		this.historyRef = historyRef;
	}
	
	/**
	 * Gets the http session associated with this message.
	 * 
	 * @return the http session
	 */
	public HttpSession getHttpSession(){
		return this.httpSession;
	}
	
	/**
	 * Sets the http session associated with this message.
	 * 
	 * @param session the new http session
	 */
	public void setHttpSession(HttpSession session) {
		this.httpSession = session;
	}

	/**
	 * Constructor for a empty HTTP message.
	 *
	 */
	public HttpMessage() {
	}
	
	public HttpMessage(URI uri) throws HttpMalformedHeaderException {
	    setRequestHeader(new HttpRequestHeader(HttpRequestHeader.GET, uri, HttpHeader.HTTP11));
	}

	/**
	 * Constructs an HTTP message with the given request header.
	 *
	 * @param reqHeader the request header
	 * @throws IllegalArgumentException if the parameter {@code reqHeader} is {@code null}.
	 */
	public HttpMessage(HttpRequestHeader reqHeader) {
	    setRequestHeader(reqHeader);
	}
	
	/**
	 * Constructs an HTTP message with the given request header and request body.
	 *
	 * @param reqHeader the request header
	 * @param reqBody the request body
	 * @throws IllegalArgumentException if the parameter {@code reqHeader} or {@code reqBody} are {@code null}.
	 */
	public HttpMessage(HttpRequestHeader reqHeader, HttpRequestBody reqBody) {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);
	}

	/**
	 * Constructor for a HTTP message with given request and response pair.
	 * @param reqHeader the request header
	 * @param reqBody the request body
	 * @param resHeader the response header
	 * @param resBody the response body
	 * @throws IllegalArgumentException if one of the parameters is {@code null}.
	 */
	public HttpMessage(HttpRequestHeader reqHeader, HttpRequestBody reqBody,
			HttpResponseHeader resHeader, HttpResponseBody resBody) {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);
		setResponseHeader(resHeader);
		setResponseBody(resBody);		
	}
	
	public HttpMessage(String reqHeader, byte[] reqBody, String resHeader, byte[] resBody) throws HttpMalformedHeaderException {
		setRequestHeader(reqHeader);
		setRequestBody(reqBody);
		if (resHeader != null && !resHeader.equals("")) {
		    setResponseHeader(resHeader);
		    setResponseBody(resBody);
		}
	}

	/**
	 * Gets the request header of this message.
	 * 
	 * @return the request header, never {@code null}
	 */
	public HttpRequestHeader getRequestHeader() {
		return mReqHeader;
	}
	
	/**
	 * Sets the request header of this message.
	 * 
	 * @param reqHeader the new request header
	 * @throws IllegalArgumentException if parameter {@code reqHeader} is {@code null}.
	 */
	public void setRequestHeader(HttpRequestHeader reqHeader) {
		if (reqHeader == null) {
			throw new IllegalArgumentException("The parameter reqHeader must not be null.");
		}
		mReqHeader = reqHeader;
	}

	/**
	 * Gets the response header of this message.
	 * <p>
	 * To know if a response has been set call the method {@code HttpResponseHeader#isEmpty()} on the returned response header.
	 * The response header is initially empty.
	 * </p>
	 * 
	 * @return the response header, never {@code null}
	 * @see HttpResponseHeader#isEmpty()
	 */
	public HttpResponseHeader getResponseHeader() {
		return mResHeader;
	}

	/**
	 * Sets the response header of this message.
	 * 
	 * @param resHeader the new response header
	 * @throws IllegalArgumentException if parameter {@code resHeader} is {@code null}.
	 */
	public void setResponseHeader(HttpResponseHeader resHeader) {
		if (resHeader == null) {
			throw new IllegalArgumentException("The parameter resHeader must not be null.");
		}
		mResHeader = resHeader;
	}

	/**
	 * Gets the request body of this message.
	 * 
	 * @return the request body, never {@code null}
	 */
	public HttpRequestBody getRequestBody() {
		return mReqBody;
	}

	/**
	 * Sets the request body of this message.
	 * 
	 * @param reqBody the new request body
	 * @throws IllegalArgumentException if parameter {@code reqBody} is {@code null}.
	 */
	public void setRequestBody(HttpRequestBody reqBody) {
		if (reqBody == null) {
			throw new IllegalArgumentException("The parameter reqBody must not be null.");
		}
		mReqBody = reqBody;
	}

	/**
	 * Gets the response body of this message.
	 * 
	 * @return the response body, never {@code null}
	 */
	public HttpResponseBody getResponseBody() {
		return mResBody;
	}

	/**
	 * Sets the response body of this message.
	 * 
	 * @param resBody the new response body
	 * @throws IllegalArgumentException if parameter {@code resBody} is {@code null}.
	 */
	public void setResponseBody(HttpResponseBody resBody) {
		if (resBody == null) {
			throw new IllegalArgumentException("The parameter resBody must not be null.");
		}
		mResBody = resBody;
	    getResponseBody().setCharset(getResponseHeader().getCharset());

	}
	
	public void setRequestHeader(String reqHeader) throws HttpMalformedHeaderException {
		HttpRequestHeader newHeader = new HttpRequestHeader(reqHeader);
		setRequestHeader(newHeader);
	}
	
	public void setResponseHeader(String resHeader) throws HttpMalformedHeaderException {
		HttpResponseHeader newHeader = new HttpResponseHeader(resHeader);
		setResponseHeader(newHeader);

	}

	public void setRequestBody(String body) {
	    getRequestBody().setCharset(getRequestHeader().getCharset());
		getRequestBody().setBody(body);

	}
	
	public void setRequestBody(byte[] body) {
		getRequestBody().setBody(body);
	    getRequestBody().setCharset(getRequestHeader().getCharset());

	}

	public void setResponseBody(String body) {
	    getResponseBody().setCharset(getResponseHeader().getCharset());
		getResponseBody().setBody(body);

	}
	
	public void setResponseBody(byte[] body) {
		getResponseBody().setBody(body);
	    getResponseBody().setCharset(getResponseHeader().getCharset());
	}

	
	@Override
	public boolean isInScope() {
		return Model.getSingleton().getSession().isInScope(this.getRequestHeader().getURI().toString());
	}

	
	@Override
	public boolean isForceIntercept() {
		String vals = this.getRequestHeader().getHeader(HttpHeader.X_SECURITY_PROXY);
		if (vals != null) {
			for (String val : vals.split(",")) {
				if (HttpHeader.SEC_PROXY_INTERCEPT.equalsIgnoreCase(val.trim())) {
					// The browser told us to do it Your Honour
					return true;
				}
			}
		}
		return forceIntercept;
	}
	
	public void setForceIntercept(boolean force) {
		this.forceIntercept = force;
	}

	/**
	 * Gets the request user.
	 *
	 * @return the request user
	 */
	public User getRequestingUser() {
		return requestUser;
	}

	public int getTimeElapsedMillis() {
        return timeElapsed;
    }

}