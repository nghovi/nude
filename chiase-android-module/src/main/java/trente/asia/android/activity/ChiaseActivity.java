package trente.asia.android.activity;

import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import trente.asia.android.util.AndroidUtil;
import trente.asia.android.util.CARequestUtil;
import trente.asia.android.volley.VolleySingleton;

/**
 * ChiaseActivity
 */
public class ChiaseActivity extends FragmentActivity{

	/**
	 * The Activity.
	 */
	protected ChiaseActivity	activity	= this;

	public boolean				isInitData	= false;

	protected String			host;

	/**
	 * Instantiates a new Chiase activity.
	 */
	public ChiaseActivity(){
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	/**
	 * Request in background
	 *
	 * @param url the url
	 * @param jsonObject the json object
	 * @param isAlert the is alert
	 */
	protected void requestBackground(final String url, JSONObject jsonObject, final boolean isAlert){

		if(AndroidUtil.invalidInternet(activity)){
			return;
		}

		initParams(jsonObject);
		String fullUrl = CARequestUtil.getUrl(host + url, Request.Method.GET, jsonObject);

		ChiaseJsonObjectRequest jsonRequest = new ChiaseJsonObjectRequest(Request.Method.GET, fullUrl, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response){

                onSuccessBackground(response, isAlert, url);
			}
		},

						new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error){
							}
						});
		RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonRequest.setRetryPolicy(policy);
		VolleySingleton.getInstance(activity).addToRequestQueue(jsonRequest);
	}

    /**
     * Success background task.
     *
     * @param response the response
     */
    protected void onSuccessBackground(JSONObject response, boolean isAlert, String url){
    }

    protected void initParams(JSONObject jsonObject){
    }

	@Override
	protected void onDestroy(){
		super.onDestroy();
		activity = null;
	}
}
