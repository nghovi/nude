package trente.asia.android.volley;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

/**
 * Created by TrungND on 8/28/2014.
 */
public class JsonArrayRequest extends JsonRequest<JSONArray>{

	/**
	 * Creates a new request.
	 * 
	 * @param url URL to fetch the JSON from
	 * @param listener Listener to receive the JSON response
	 * @param errorListener Error listener, or null to ignore errors.
	 */
	public JsonArrayRequest(int method, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
		super(method, url, null, listener, errorListener);
	}

	public JsonArrayRequest(int method, String url, String requestBody, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
		super(method, url, requestBody, listener, errorListener);
	}

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response){
		try{
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		}catch(UnsupportedEncodingException e){
			return Response.error(new ParseError(e));
		}catch(JSONException je){
			return Response.error(new ParseError(je));
		}
	}
}
