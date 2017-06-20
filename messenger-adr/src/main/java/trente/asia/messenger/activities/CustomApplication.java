package trente.asia.messenger.activities;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by tien on 6/19/2017.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
}
