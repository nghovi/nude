package nguyenhoangviet.vpcorp.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import nguyenhoangviet.vpcorp.android.exception.CAException;

/**
 * CARequestUtil
 * <p>
 * Created by takyas on 29/10/15.
 */
public class CARequestUtil {

    public static int changeDpToPixcel(Context contect, int dp) {

        Resources res = contect.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());

    }

    /**
     * Get url.
     *
     * @param url        the url
     * @param method     the method
     * @param jsonObject the json object
     * @return the string
     */
    public static String getUrl(String url, int method, JSONObject jsonObject) {

        url = url + "?" + getQueryString(jsonObject);

        // try{
        // url = URLEncoder.encode(url, "UTF-8");
        // }catch (UnsupportedEncodingException ex){
        // ex.printStackTrace();
        // }

        return (url);
    }

    public static String getGetUrl(String url, JSONObject jsonObject) {
//        url = url + "?" + getQueryString(jsonObject);
        return (url);
    }

    public static String getQueryString(JSONObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        try {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                builder.append(key + "=" + URLEncoder.encode(jsonObject.optString(key), "UTF-8") + "&");
            }
        } catch (UnsupportedEncodingException ex) {
            new CAException(ex);
        }
        return builder.toString();
    }
}
