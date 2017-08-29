package asia.trente.officeletter.commons.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import android.util.Log;

import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * MyFirebaseInstanceIDService
 *
 * @author TrungND
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

	private final String TAG = "InstanceIDService";

	@Override
	public void onTokenRefresh(){
		// Get updated InstanceID token.
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		Log.d(TAG, "Refreshed token: " + refreshedToken);

		// If you want to send messages to this application instance or
		// manage this apps subscriptions on the server side, send the
		// Instance ID token to your app server.
		sendRegistrationToServer(refreshedToken);
	}

	private void sendRegistrationToServer(String token){
		PreferencesSystemUtil preferencesSystemUtil = new PreferencesSystemUtil(getApplicationContext());
		preferencesSystemUtil.set(WelfareConst.REGISTRATION_ID_PARAM, token);
	}
}
