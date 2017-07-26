package trente.asia.thankscard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.common.TCDetailFragment;
import trente.asia.thankscard.services.common.TcLogInFragment;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.mypage.MypageFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

public class MainActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();
		if(CCStringUtil.isEmpty(userModel.key)){
			addFragment(new TcLogInFragment());
		}else{
			Bundle mExtras = getIntent().getExtras();
			if(mExtras != null){
				showFragment(mExtras);
			}else{
				addFragment(new MypageFragment());
			}
		}

		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		PreferencesSystemUtil preference = new PreferencesSystemUtil(this);
		preference.set(TcConst.PREF_FRAME_WIDTH, String.valueOf(displayMetrics.widthPixels));
		preference.set(TcConst.PREF_FRAME_HEIGHT, String.valueOf(displayMetrics.widthPixels / TcConst.FRAME_RATIO));
	}

	private void showFragment(Bundle mExtras){
		String serviceCode = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE);
		String key = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY);
		String parentKey = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_PARENT_KEY);
		if(!WelfareConst.NotificationType.TC_NOTI_RECEIVE_CARD.equals(serviceCode)){
			addFragment(new MypageFragment());
		}else{
			TCDetailFragment detailFragment = new TCDetailFragment();
			detailFragment.isClickNotification = true;
			HistoryModel historyModel = new HistoryModel(key);
			historyModel.categoryId = parentKey;
			detailFragment.setCurrentHistory(historyModel);
			addFragment(detailFragment);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void log(String msg) {
		Log.e("MainActivity", msg);
	}
}
