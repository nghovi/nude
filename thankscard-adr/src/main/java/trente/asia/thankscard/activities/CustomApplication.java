package trente.asia.thankscard.activities;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by tien on 7/19/2017.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
}
