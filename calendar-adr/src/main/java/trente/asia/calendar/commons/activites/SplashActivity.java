package trente.asia.calendar.commons.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import trente.asia.calendar.R;
import trente.asia.welfare.adr.activity.WelfareActivity;

public class SplashActivity extends WelfareActivity{

	public static final String	SENDER_ID		= "SENDER_ID";
	// Splash screen timer
	private static int			SPLASH_TIME_OUT	= 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// Api.get(this, Const.API_VERSION_CHECK, new TCUtil.JsonBuilder().add("version", BuildConfig.VERSION_NAME).getJsonObj(), this, this, null);
		// String senderId = response.optString("sender_id");
		// getApplicationContext().getSharedPreferences(AbstractTCFragment.SHARED_PREFERENCES_NAME, Activity.MODE_PRIVATE).edit().putString(SENDER_ID,
		// senderId).commit();

		// Showing splash screen with a timer.
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				Intent i = new Intent(SplashActivity.this, MainClActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
