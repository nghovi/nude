package trente.asia.android.activity;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by takyas on 11/27/16.
 */

public interface HttpCallback {

    /**
     * 成功時のレスポンスコード
     */
    public static final int SUCCESS = 0;

    /**
     * 失敗時のレスポンスコード
     */
    public static final int ERROR = -1;

    /**
     * コールバックメソッド
     */
    public void callback(final int responseCode,  String url, final JSONObject resultMap, final boolean isAlert);

}
