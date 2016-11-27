package trente.asia.android.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import trente.asia.android.util.AndroidUtil;
import trente.asia.android.util.CARequestUtil;
import trente.asia.android.volley.VolleySingleton;

/**
 * ChiaseActivity
 */
public class ChiaseActivity extends FragmentActivity {

    /**
     * The Activity.
     */
    protected ChiaseActivity activity = this;

    public boolean isInitData = false;

    protected String host;

    /**
     * Instantiates a new Chiase activity.
     */
    public ChiaseActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    OkHttpClient client = new OkHttpClient();


    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * Request in background
     *
     * @param url        the url
     * @param jsonObject the json object
     * @param isAlert    the is alert
     */
    protected void requestBackground(final String url, JSONObject jsonObject, final boolean isAlert) {

        if (AndroidUtil.invalidInternet(activity)) {
            return;
        }

        initParams(jsonObject);
        String fullUrl = CARequestUtil.getGetUrl(host + url, jsonObject);

        try {
            String result = run(fullUrl);
            JSONObject resJson = new JSONObject(result);
            onSuccessBackground(resJson, isAlert, url);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Success background task.
     *
     * @param response the response
     */
    protected void onSuccessBackground(JSONObject response, boolean isAlert, String url) {
    }

    protected void initParams(JSONObject jsonObject) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }
}
