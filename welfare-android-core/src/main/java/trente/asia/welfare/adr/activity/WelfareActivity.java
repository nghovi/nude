package trente.asia.welfare.adr.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import trente.asia.android.activity.ChiaseActivity;
import trente.asia.welfare.adr.R;

/**
 * WelfareActivity
 *
 * @author TrungND
 */
public class WelfareActivity extends ChiaseActivity{

	public boolean isClearRegistrationId = false;
    public Map<String, Object> dataMap = new HashMap<String, Object>();

	////////////// Device Back Button/////////////////////////////////////////////////////////////
	public interface OnDeviceBackButtonClickListener{

		void onClickDeviceBackButton();
	}

	public interface OnActivityResultListener{

		void onActivityResult(int requestCode, int resultCode, Intent data);
	}

	private OnDeviceBackButtonClickListener	onDeviceBackButtonClickListener;
	private boolean							doubleBackToExitPressedOnce	= false;
	private OnActivityResultListener		onActivityResultListener;

	public void setOnDeviceBackButtonClickListener(OnDeviceBackButtonClickListener listener){
		this.onDeviceBackButtonClickListener = listener;
	}

	public OnDeviceBackButtonClickListener getOnDeviceBackButtonClickListener(){
		return this.onDeviceBackButtonClickListener;
	}

	public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener){
		this.onActivityResultListener = onActivityResultListener;
	}

	@Override
	public void onBackPressed(){
		if(onDeviceBackButtonClickListener != null){
			onDeviceBackButtonClickListener.onClickDeviceBackButton();
		}else if(getSupportFragmentManager().getBackStackEntryCount() <= 1){
			setDoubleBackPressedToFinish();
		}else{
			backOneFragment();
		}
	}

	public void setDoubleBackPressedToFinish(){
		if(this.doubleBackToExitPressedOnce){
			finish();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, getString(R.string.main_activity_back_btn_guide), Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	/////////////// End Device Back

	/*
	 * Fragment Management To use this function, make sure that activity's layout file include
	 * fragment_container. no add to back stack
	 */
	public void replaceWithFragment(Fragment fragment){
		getSupportFragmentManager().popBackStack();
		addFragment(fragment);
	}

	// add to back stack
	public void addFragment(Fragment fragment){
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.ipt_id_body, fragment, fragment.getClass().toString());
		fragmentTransaction.addToBackStack(fragment.getClass().toString());
		fragmentTransaction.commit();
	}

	public void backOneFragment(){
		getSupportFragmentManager().popBackStack();
	}

	public void emptyFragmentStack(){
		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		if(onActivityResultListener != null){
			onActivityResultListener.onActivityResult(requestCode, resultCode, data);
		}
	}


}
