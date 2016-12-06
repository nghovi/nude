package trente.asia.android.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import trente.asia.android.util.AndroidUtil;
import trente.asia.android.util.CARequestUtil;

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

	OkHttpClient client = new OkHttpClient();

//	public void run(final String url, final boolean isAlert) throws IOException{
//		final Request request = new Request.Builder().url(url).build();
//		AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
//
//			@Override
//			protected String doInBackground(Void...params){
//				try{
//					Response response = client.newCall(request).execute();
//					if(!response.isSuccessful()){
//						return null;
//					}
//					return response.body().string();
//				}catch(Exception e){
//					e.printStackTrace();
//					return null;
//				}
//			}
//
//			@Override
//			protected void onPostExecute(String s){
//				super.onPostExecute(s);
//				try{
//					JSONObject resJson = new JSONObject(s);
//					onSuccessBackground(resJson, isAlert, url);
//				}catch(JSONException e){
//					e.printStackTrace();
//				}
//			}
//		};
//
//		asyncTask.execute();
//	}

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
		String fullUrl = CARequestUtil.getGetUrl(host + url, jsonObject);

		try{
            final Request request = new Request.Builder().url(fullUrl).build();
            AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void...params){
                    try{
                        Response response = client.newCall(request).execute();
                        if(!response.isSuccessful()){
                            return null;
                        }
                        return response.body().string();
                    }catch(Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s){
                    super.onPostExecute(s);
                    try{
                        JSONObject resJson = new JSONObject(s);
                        onSuccessBackground(resJson, isAlert, url);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            };

            asyncTask.execute();

		}catch(Exception e){
			e.printStackTrace();
		}
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
