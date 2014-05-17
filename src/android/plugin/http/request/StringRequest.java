package plugin.http.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class StringRequest extends
    com.android.volley.toolbox.StringRequest {
  private static final String SET_COOKIE_KEY = "set-cookie";
  private static final String COOKIE_KEY = "Cookie";
  private static final String SESSION_COOKIE = "connect.sid";
  private SharedPreferences pref = null;
  private final Map<String, String> _params;
  private final Map<String, String> _headers;
  
  public StringRequest(Context context, int method, String url, Map<String, String> params, Listener<String> listener,ErrorListener errorListener) {
	  this(context,method,url,params,Collections.<String,String>emptyMap(),listener,errorListener);
  }

  public StringRequest(Context context, int method, String url, Map<String, String> params, Map<String,String> headers, Listener<String> listener, ErrorListener errorListener) {
    super(method, url, listener, errorListener);
    pref = PreferenceManager.getDefaultSharedPreferences(context);
    _params = params;
    _headers = headers;
  }
  
  @Override
  protected Response<String> parseNetworkResponse(NetworkResponse response) {
    // since we don't know which of the two underlying network vehicles
    // will Volley use, we have to handle and store session cookies manually
    this.checkSessionCookie(response.headers);

    return super.parseNetworkResponse(response);
  }
  
  @Override
  protected Map<String, String> getParams() {
      return _params;
  }
  
  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    Map<String, String> headers = super.getHeaders();

    if (headers == null || headers.equals(Collections.emptyMap())) {
      headers = new HashMap<String, String>();
    }

    this.addSessionCookie(headers);
    
    for (Entry<String,String> entry : _headers.entrySet()) {
		headers.put(entry.getKey(), entry.getValue());
	}
    
    return headers;
  }

  public final void checkSessionCookie(Map<String, String> headers) {
    if (headers.containsKey(SET_COOKIE_KEY)
        && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
      String cookie = headers.get(SET_COOKIE_KEY);
      if (cookie.length() > 0) {
        String[] splitCookie = cookie.split(";");
        String[] splitSessionId = splitCookie[0].split("=");
        cookie = splitSessionId[1];
        Editor prefEditor = pref.edit();
        prefEditor.putString(SESSION_COOKIE, cookie);
        prefEditor.commit();
      }
    }
  }

  public final void addSessionCookie(Map<String, String> headers) {
    String sessionId = pref.getString(SESSION_COOKIE, "");
    if (sessionId.length() > 0) {
      StringBuilder builder = new StringBuilder();
      builder.append(SESSION_COOKIE);
      builder.append("=");
      builder.append(sessionId);
      if (headers.containsKey(COOKIE_KEY)) {
        builder.append("; ");
        builder.append(headers.get(COOKIE_KEY));
      }
      headers.put(COOKIE_KEY, builder.toString());
    }
  }
}