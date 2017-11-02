package nguyenhoangviet.vpcorp.team360;

import android.app.Application;
import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;

/**
 * Created by takano-yasuhiro on 2017/06/18.
 */

public class Team360Application extends Application {

    private static final String TAG = "team360";

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();

        //Realm.deleteRealm(realmConfiguration);

        Realm.setDefaultConfiguration(realmConfiguration);

        Log.v(TAG, "Start Team360Application");
    }

//    private RealmConfiguration buildRealmConfiguration() {
//        return new RealmConfiguration.Builder(this)
//                .schemaVersion(1L)
//                .migration(new RealmMigration() {
//                    @Override
//                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
//                        if (oldVersion == 0L) {
////                            final RealmObjectSchema tweetSchema = realm.getSchema().get("Tweet");
////                            tweetSchema.addField("favorited", boolean.class);
////                            //noinspection UnusedAssignment
////                            oldVersion++;
//                        }
//                    }
//                })
//                .build();
//    }

}
