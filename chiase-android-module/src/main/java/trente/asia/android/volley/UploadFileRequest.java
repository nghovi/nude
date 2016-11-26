package trente.asia.android.volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import trente.asia.android.activity.ChiaseJsonObjectRequest;

/**
 * Created by TrungND on 10/27/2014.
 */
public class UploadFileRequest extends ChiaseJsonObjectRequest{

	private MultipartEntity						entity				= new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, MIME.UTF8_CHARSET);

	// private static final String FILE_PART_NAME = "file";
	private static final String					STRING_PART_NAME	= "text";

	private final Response.Listener<JSONObject>	mListener;
	private final Map<String, File>				mFilePart;
	private final JSONObject					mStringPart;

	// private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

	public UploadFileRequest(String url, Map<String, File> files, JSONObject stringPart, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
		super(Method.POST, url, listener, errorListener);

		mListener = listener;
		mFilePart = files;
		mStringPart = stringPart;
		buildMultipartEntity();
	}

	private void buildMultipartEntity(){
		try{
			// ByteArrayBody
			for(String name : mFilePart.keySet()){
				File file = mFilePart.get(name);
				if(file != null){
					entity.addPart(name, new FileBody(file.getAbsoluteFile()));
				}
			}
			Iterator<String> iterator = mStringPart.keys();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = mStringPart.getString(key);
				entity.addPart(key, new StringBody(value, "text/plain", MIME.UTF8_CHARSET));
			}
		}catch(Exception e){
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@Override
	public String getBodyContentType(){
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			entity.writeTo(bos);
		}catch(IOException e){
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
		try{
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		}catch(UnsupportedEncodingException e){
			return Response.error(new ParseError(e));
		}catch(JSONException je){
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response){
		mListener.onResponse(response);
	}

	// private String encodeParameters(JSONObject jsonObject) {
	// StringBuilder encodedParams = new StringBuilder();
	// Iterator<String> iterator = jsonObject.keys();
	// try {
	// while(iterator.hasNext()) {
	// String key = iterator.next();
	// String value = jsonObject.getString(key);
	// encodedParams.append(URLEncoder.encode(key, DEFAULT_PARAMS_ENCODING));
	// encodedParams.append('=');
	// encodedParams.append(URLEncoder.encode(value, DEFAULT_PARAMS_ENCODING));
	// encodedParams.append('&');
	// }
	// } catch (Exception ex) {
	// return "";
	// }
	// return encodedParams.toString();
	// }
}
