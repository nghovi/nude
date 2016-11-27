package trente.asia.android.activity;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import trente.asia.android.util.CARequestUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by takyas on 11/27/16.
 */

public class HttpDelegate {

    OkHttpClient client = new OkHttpClient();

    protected String host;

    public HttpDelegate(String host) {
        this.host = host;
    }

    public void get(final HttpCallback callback, final String url, JSONObject param, final boolean isAlert) {


        final JSONObject resJson = null;

        final OkHttpClient client = new OkHttpClient();

        String fullUrl = CARequestUtil.getGetUrl(host + url, param);

        final Request request = new Request.Builder().url(fullUrl).build();

        AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        return null;
                    }
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject resJson = new JSONObject(s);
                    callback.callbackLoad(HttpCallback.SUCCESS, url, resJson, isAlert);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        asyncTask.execute();

    }


}
