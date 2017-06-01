package trente.asia.shiftworking.common.activities;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.exception.CAException;
import trente.asia.android.util.AndroidUtil;
import trente.asia.shiftworking.BuildConfig;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.services.login.SwLoginFragment;
import trente.asia.shiftworking.services.offer.WorkOfferDetailFragment;
import trente.asia.shiftworking.services.worktime.WorkerFragment;
import trente.asia.shiftworking.services.worktime.WorknoticeFormFragment;
import trente.asia.shiftworking.services.worktime.WorktimeCheckInFragment;
import trente.asia.shiftworking.services.worktime.model.NoticeModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

public class MainActivity extends WelfareActivity{

	public Timer	mTimer;
	public String	uncheckedCount;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		host = BuildConfig.HOST;

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();
		// Bundle mExtras = getIntent().getExtras();
		if(!CCStringUtil.isEmpty(userModel.key)){
			Bundle mExtras = getIntent().getExtras();
			if(mExtras != null){
				showFragment(mExtras);
			}else{
				addFragment(new WorktimeCheckInFragment());
			}
		}else{
			showLogInFragment();
		}
	}

	private void showLogInFragment(){
		SwLoginFragment loginFragment = new SwLoginFragment();
		addFragment(loginFragment);
	}

	private void showFragment(Bundle mExtras){
		String serviceCode = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE);
		if(WelfareConst.NotificationType.SW_NOTI_OVER_TIME.equals(serviceCode)){
			addFragment(new WorkerFragment());
		}else if(WelfareConst.NotificationType.SW_NOTI_NEW_NOTICE.equals(serviceCode)){
			String key = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY);
			WorknoticeFormFragment fragment = new WorknoticeFormFragment();
			NoticeModel noticeModel = new NoticeModel();
			noticeModel.key = key;
			fragment.setNoticeModel(noticeModel);
			fragment.isClickNotification = true;
			addFragment(fragment);
        }else if(WelfareConst.NotificationType.SW_NOTI_OFFER.equals(serviceCode)){
            String key = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY);
            WorkOfferDetailFragment offerDetailFragment = new WorkOfferDetailFragment();
            offerDetailFragment.setActiveOfferId(key);
            offerDetailFragment.isClickNotification = true;
            addFragment(offerDetailFragment);
		}else{
			addFragment(new WorktimeCheckInFragment());
		}
	}

	@Override
	public void onResume(){
		super.onResume();

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		if(!CCStringUtil.isEmpty(prefAccUtil.getUserPref().key)){
			startTimer();
		}
	}

	public void startTimer(){
		if(mTimer == null) mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run(){
				activity.runOnUiThread(new Runnable() {

					public void run(){
						loadUncheckNotice();
					}
				});
			}
		}, 0, WelfareConst.ONE_MINUTE_TIME_LOAD);
	}

    public void stopTimer(){
        uncheckedCount = "";
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

	private void loadUncheckNotice(){
		requestBackground(SwConst.API_CHECK, new JSONObject(), false);
	}

	@Override
	protected void initParams(JSONObject jsonObject){
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		if(prefAccUtil != null){
			UserModel userModel = prefAccUtil.getUserPref();

			try{
				jsonObject.put("loginUserId", CCStringUtil.toString(userModel.key));
				jsonObject.put("companyId", CCStringUtil.toString(userModel.companyId));
				jsonObject.put(CsConst.ARG_TOKEN, userModel.token);
				jsonObject.put("language", Resources.getSystem().getConfiguration().locale.getLanguage());
				TimeZone timeZone = TimeZone.getDefault();
				jsonObject.put(CsConst.ARG_TIMEZONE, timeZone.getID());
				jsonObject.put(CsConst.ARG_DEVICE, "A");
				jsonObject.put(CsConst.ARG_VERSION, AndroidUtil.getVersionName(activity));
				jsonObject.put("serviceCd", WelfareConst.SERVICE_CD_SW);
			}catch(JSONException ex){
				new CAException(ex);
			}
		}
	}

	/**
	 * On success background.
	 *
	 * @param response the response
	 */
	@Override
	protected void onSuccessBackground(JSONObject response, boolean isAlert, String url){
		String status = response.optString(CsConst.STATUS);
		String returnCd = response.optString(CsConst.RETURN_CODE_PARAM);
		if(CsConst.STATUS_OK.equals(status) && (CCStringUtil.isEmpty(returnCd) || CCConst.NONE.equals(returnCd))){
			if(SwConst.API_CHECK.equals(url)){
				final TextView txtUnread = (TextView)activity.findViewById(R.id.txt_id_unread_message);
				if(txtUnread != null){
					uncheckedCount = response.optString("uncheckedCount");
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){
							if(!CCStringUtil.isEmpty(uncheckedCount) && !CCConst.NONE.equals(uncheckedCount)){
								txtUnread.setVisibility(View.VISIBLE);
								txtUnread.setText(uncheckedCount);
							}else{
								txtUnread.setVisibility(View.GONE);
							}
						}
					});

				}
			}
		}else{
		}
	}

	@Override
	public void onPause(){
		super.onPause();
		stopTimer();
	}
}
