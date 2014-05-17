package plugin.http.request;

import java.util.Map;

public class Args {

	private Map<String, String> mHeaders;
	private Map<String, String> mParameters;
	private String mRequestType;
	private int mRequestTypeInt;
	private String mURL;

	public Args() {
	}

	public Args(String pRequestType, String pURL, Map<String, String> pHeaders, Map<String, String> pParameters) {
		mURL = pURL;
		mRequestType = pRequestType;
		mHeaders = pHeaders;
		mParameters = pParameters;
	}

	public int getRequestTypeInt() {
		return mRequestTypeInt;
	}

	public void setRequestTypeInt(int pRequestTypeInt) {
		mRequestTypeInt = pRequestTypeInt;
	}

	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	public void setHeaders(Map<String, String> pHeaders) {
		mHeaders = pHeaders;
	}

	public Map<String, String> getParameters() {
		return mParameters;
	}

	public void setParameters(Map<String, String> pParameters) {
		mParameters = pParameters;
	}

	public String getRequestType() {
		return mRequestType;
	}

	public void setRequestType(String pRequestType) {
		mRequestType = pRequestType;
	}

	public String getURL() {
		return mURL;
	}

	public void setURL(String pURL) {
		mURL = pURL;
	}

}
