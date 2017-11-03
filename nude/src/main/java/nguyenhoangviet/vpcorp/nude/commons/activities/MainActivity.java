package nguyenhoangviet.vpcorp.nude.commons.activities;

import com.google.android.gms.ads.MobileAds;

import android.os.Bundle;

import nguyenhoangviet.vpcorp.nude.R;
import nguyenhoangviet.vpcorp.nude.services.user.PhotoPageContainerFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;

public class MainActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		MobileAds.initialize(this, "ca-app-pub-4354101127297995~5318775175");
		setContentView(R.layout.activity_address_card);
		addFragment(new PhotoPageContainerFragment());
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig){
	//// super.onConfigurationChanged(newConfig);
	// }
}
