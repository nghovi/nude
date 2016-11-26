package trente.asia.android.activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * ChiaseJsonObjectRequest
 */
public class ChiaseJsonObjectRequest extends JsonObjectRequest{

	public ChiaseJsonObjectRequest(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		super(method, url, requestBody, listener, errorListener);
	}

	public ChiaseJsonObjectRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		super(Method.GET, url, listener, errorListener);
	}

	/**
	 * Creates a new request.
	 * 
	 * @param method the HTTP method to use
	 * @param url URL to fetch the JSON from
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public ChiaseJsonObjectRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		super(method, url, listener, errorListener);
	}

	/**
	 * Creates a new request.
	 * 
	 * @param method the HTTP method to use
	 * @param url URL to fetch the JSON from
	 * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
	 * indicates no parameters will be posted along with request.
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public ChiaseJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
	}

	/**
	 * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
	 * <code>null</code>, <code>POST</code> otherwise.
	 *
	 * @see #ChiaseJsonObjectRequest(int, String, JSONObject, Response.Listener, Response.ErrorListener)
	 */
	public ChiaseJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest, listener, errorListener);
	}

	protected void onHtmlReceived(String html){

	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
		try{
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
			if(response.headers.get("Content-Type").contains("text/html")){
				return Response.error(new ChiaseParseError(new JSONException("Parse json failed, html received"), jsonString));
			}
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		}catch(UnsupportedEncodingException e){
			return Response.error(new ParseError(e));
		}catch(JSONException je){
			return Response.error(new ParseError(je));
		}catch(Exception ex){
			return Response.error(new ParseError(ex));
		}catch(OutOfMemoryError error){
			return Response.error(new ParseError(error));
		}
	}

	// @Override
	// public Map<String, String> getHeaders() throws AuthFailureError{
	// HashMap<String, String> params = new HashMap<String, String>();
	// String creds = String.format("%s:%s", "shk", "258456");
	// String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
	// params.put("Authorization", auth);
	// return params;
	// }

}
