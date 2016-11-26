package trente.asia.messenger.services.user;

import android.app.Activity;
import android.os.Bundle;

import trente.asia.messenger.R;
import trente.asia.welfare.adr.activity.WelfareActivity;

/**
 * UserListActivity
 *
 * @author TrungND
 */
public class UserListActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);

		addFragment(new UserListFragment());
	}

	@Override
	public void onBackPressed(){
		activity.setResult(Activity.RESULT_CANCELED);
		activity.finish();
	}
}
