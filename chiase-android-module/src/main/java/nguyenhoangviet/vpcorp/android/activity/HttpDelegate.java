package nguyenhoangviet.vpcorp.android.activity;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import asia.chiase.core.util.CCStringUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import nguyenhoangviet.vpcorp.android.util.CARequestUtil;

/**
 * Created by takyas on 11/27/16.
 */

public class HttpDelegate{

	OkHttpClient		client	= new OkHttpClient();

	protected String	host;

	public HttpDelegate(String host){
		this.host = host;
	}

	public void get(final HttpCallback callback, final String url, JSONObject param, final boolean isAlert){

		final OkHttpClient client = new OkHttpClient();

		String fullUrl = CARequestUtil.getGetUrl(host + url, param);

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
					JSONObject resJson = null;
					if(!CCStringUtil.isEmpty(s)){
						resJson = new JSONObject(s);
					}
					callback.callbackLoad(HttpCallback.SUCCESS, url, resJson, isAlert);
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		};

		asyncTask.execute();

	}

	public void post(final HttpCallback callback, final String url, final JSONObject param, final boolean isAlert){

		final OkHttpClient client = new OkHttpClient();

		AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void...params){
				try{

					MediaType JSON = MediaType.parse("application/json; charset=utf-8");
					RequestBody formBody = RequestBody.create(JSON, param.toString());

					Request request = new Request.Builder().url(host + url).post(formBody).build();

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
					JSONObject resJson = null;
					if(!CCStringUtil.isEmpty(s)) resJson = new JSONObject(s);
					callback.callbackUpdate(HttpCallback.SUCCESS, url, resJson, isAlert);
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		};

		asyncTask.execute();
	}

	public void upload(final HttpCallback callback, final String url, final JSONObject param, final Map<String, File> files, final boolean isAlert){

		final OkHttpClient client = new OkHttpClient();

		AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void...params){
				try{

					MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

					Iterator<String> nameItr = param.keys();
					while(nameItr.hasNext()){
						String name = nameItr.next();
						body.addFormDataPart(name, param.getString(name));
					}

					for(Map.Entry<String, File> e : files.entrySet()){
						File file = e.getValue();
						body.addFormDataPart(e.getKey(), file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
					}

					Request request = new Request.Builder().url(host + url).post(body.build()).build();

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
					JSONObject resJson = null;
					if(!CCStringUtil.isEmpty(s)) resJson = new JSONObject(s);
					callback.callbackUpload(HttpCallback.SUCCESS, url, resJson, isAlert);
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
		};

		asyncTask.execute();

	}

	private void log(String msg) {
		Log.e("HttpDelegate", msg);
	}
}
