package nguyenhoangviet.vpcorp.messenger.services.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tien on 6/16/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private OnNetworkChangeListener callback;

    public NetworkChangeReceiver(OnNetworkChangeListener listener) {
        this.callback = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (callback == null) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)) {
            callback.onNetworkConnectionChanged(true);
        } else {
            callback.onNetworkConnectionChanged(false);
        }
    }

    public interface OnNetworkChangeListener {
        void onNetworkConnectionChanged(boolean connected);
    }
}
