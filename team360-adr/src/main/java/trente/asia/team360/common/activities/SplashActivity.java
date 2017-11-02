package nguyenhoangviet.vpcorp.team360.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import nguyenhoangviet.vpcorp.team360.R;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;

public class SplashActivity extends WelfareActivity{

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// Showing splash screen with a timer.
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				Intent i = new Intent(SplashActivity.this, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}
}
