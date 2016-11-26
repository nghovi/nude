package trente.asia.android.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;

/**
 * Created by TrungND on 8/28/2014.
 */
public class VolleySingleton{

	private static VolleySingleton	mInstance;
	private RequestQueue			mRequestQueue;
	private static Context			mCtx;

	private VolleySingleton(Context context){
		mCtx = context;
		mRequestQueue = getRequestQueue();
	}

	public static synchronized VolleySingleton getInstance(Context context){
		if(mInstance == null){
			mInstance = new VolleySingleton(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue(){
		if(mRequestQueue == null){
			// HttpClient object to manage cookie
			// DefaultHttpClient httpclient = CAMsgUtil.getThreadSafeClient(mCtx);

			// HttpStack httpStack = new HttpClientStack(httpclient);

			mRequestQueue = Volley.newRequestQueue(mCtx);
			// mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req){
		getRequestQueue().add(req);
	}
}
