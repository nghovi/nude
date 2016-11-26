package trente.asia.android.volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

/**
 * Created by TrungND on 10/27/2014.
 */
public class MultipartRequest extends Request<String>{

	private MultipartEntity					entity				= new MultipartEntity();

	private static final String				FILE_PART_NAME		= "file";
	private static final String				STRING_PART_NAME	= "text";

	private final Response.Listener<String>	mListener;
	private final ArrayList<File>			mFilePart;
	private final String					mStringPart;

	public MultipartRequest(String url, ArrayList<File> files, String stringPart, Response.Listener<String> listener, Response.ErrorListener errorListener){
		super(Method.POST, url, errorListener);

		mListener = listener;
		mFilePart = files;
		mStringPart = stringPart;
		buildMultipartEntity();
	}

	private void buildMultipartEntity(){
		try{
			for(File file : mFilePart){
				entity.addPart(FILE_PART_NAME, new FileBody(file));
			}
			entity.addPart(STRING_PART_NAME, new StringBody(mStringPart));
		}catch(Exception e){
			VolleyLog.e("UnsupportedEncodingException");
		}
	}

	@Override
	public String getBodyContentType(){
		return entity.getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			entity.writeTo(bos);
		}catch(IOException e){
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
		}
		return bos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response){
		return Response.success("Uploaded", getCacheEntry());
	}

	@Override
	protected void deliverResponse(String response){
		mListener.onResponse(response);
	}
}
