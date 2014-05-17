package plugin.http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class HttpRequest extends CordovaPlugin {
	private Context context = null;
	private RequestQueue queue = null;

	private enum REQUEST_METHODS {
		get, post, delete, put
	};

	public void initialize(CordovaInterface cordova, final CordovaWebView webView) {
		this.context = cordova.getActivity();
		this.queue = Volley.newRequestQueue(this.context);
	}

	@Override
	public boolean execute(String pAction, JSONArray pArgs, final CallbackContext pCallback) {

		if (!pAction.contentEquals("execute")) {
			return false;
		}

		Args args = parseMethod(parseArgs(pArgs));

		Listener<String> onSuccess = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				pCallback.success(response);
			}
		};
		ErrorListener onError = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pCallback.error(error.getMessage());
			}
		};

		StringRequest request = new StringRequest(this.context, args.getRequestTypeInt(), args.getURL(), args.getParameters(), args.getHeaders(), onSuccess, onError);
		queue.add(request);

		return true;
	}

	protected Args parseMethod(Args pArgs) {

		int requestType = 0;

		try {
			switch (REQUEST_METHODS.valueOf(pArgs.getRequestType())) {
			case get:
				requestType = Method.GET;
				break;
			case post:
				requestType = Method.POST;
				break;
			case put:
				requestType = Method.PUT;
				break;
			case delete:
				requestType = Method.DELETE;
				break;
			default:
				requestType = Method.DEPRECATED_GET_OR_POST;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		pArgs.setRequestTypeInt(requestType);

		if (Method.GET == requestType) {
			StringBuilder url = new StringBuilder(pArgs.getURL());
			url.append("?");
			for (Entry<String, String> entry : pArgs.getParameters().entrySet()) {
				url.append(entry.getKey());
				url.append("=");
				url.append(entry.getValue());
				url.append("&");
			}

			url.deleteCharAt(url.length() - 1);

			pArgs.setURL(url.toString());
			pArgs.setParameters(Collections.<String, String> emptyMap());
		}

		return pArgs;
	}

	/**
	 * { type : [get,post], url : http://example.com, header : {}, params : {} }
	 * 
	 * @param pArgs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Args parseArgs(JSONArray pArgs) {

		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		String request = "";
		String url = "";

		try {
			JSONObject arg = null;

			if (pArgs != null && pArgs.length() == 1) {
				arg = pArgs.getJSONObject(0);
			} else {
				arg = new JSONObject();
			}

			request = (arg.optString("type")).toLowerCase();
			url = arg.optString("url");
			JSONObject headerObj = arg.optJSONObject("header");
			JSONObject paramObj = arg.optJSONObject("params");

			if (headerObj != null) {
				Iterator<Object> headerItr = headerObj.keys();
				while (headerItr.hasNext()) {
					String key = String.valueOf(headerItr.next());
					String value = headerObj.optString(key);
					headers.put(key, value);
				}
			}

			if (paramObj != null) {
				Iterator<Object> paramItr = paramObj.keys();
				while (paramItr.hasNext()) {
					String key = String.valueOf(paramItr.next());
					String value = paramObj.optString(key);
					params.put(key, value);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new Args(request, url, headers, params);
	}
}