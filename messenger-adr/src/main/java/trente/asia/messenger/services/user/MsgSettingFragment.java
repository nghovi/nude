package trente.asia.messenger.services.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.messenger.R;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.model.WFMStampCategoryModel;
import trente.asia.messenger.services.message.model.WFMStampModel;
import trente.asia.messenger.services.setting.MsContactUsFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgSettingFragment extends AbstractMsgFragment implements View.OnClickListener{

	private LinearLayout	mLnrSignOut;
	private LinearLayout	mLnrAccInfo;
	private LinearLayout	mLnrAboutApp;
	private LinearLayout	mLnrTerm;
	private LinearLayout	mLnrPolicy;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		// Inflate the layout for this fragment
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_msg_setting, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		initHeader(trente.asia.welfare.adr.R.drawable.wf_back_white, getString(R.string.msg_setting), null);

		mLnrAccInfo = (LinearLayout)getView().findViewById(R.id.lnr_account_info);
		mLnrAboutApp = (LinearLayout)getView().findViewById(R.id.lnr_about_app);
		mLnrTerm = (LinearLayout)getView().findViewById(R.id.lnr_term);
		mLnrPolicy = (LinearLayout)getView().findViewById(R.id.lnr_policy);
		mLnrSignOut = (LinearLayout)getView().findViewById(R.id.lnr_signout);
		LinearLayout lnrContactUs = (LinearLayout)getView().findViewById(R.id.lnr_contact_us);

		mLnrAccInfo.setOnClickListener(this);
		mLnrAboutApp.setOnClickListener(this);
		mLnrTerm.setOnClickListener(this);
		mLnrPolicy.setOnClickListener(this);
		mLnrSignOut.setOnClickListener(this);
		lnrContactUs.setOnClickListener(this);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.lnr_account_info:
			gotoFragment(new MsAccountInfoFragment());
			break;
		case R.id.lnr_about_app:
			MsAboutFragment aboutAppFragment = new MsAboutFragment();
			((WelfareActivity)activity).addFragment(aboutAppFragment);
			break;
		case R.id.lnr_term:
			gotoBrowserWithLang(WelfareConst.ACCOUNT_TERM_URL);
			break;
		case R.id.lnr_policy:
			gotoBrowser(WelfareConst.ACCOUNT_POLICY_URL);
			break;
		case R.id.lnr_signout:
			showSignOutConfirmDialog();
			break;
		case R.id.lnr_contact_us:
			gotoFragment(new MsContactUsFragment());
			break;
		default:
			break;
		}
	}

	private void showSignOutConfirmDialog(){
		final WfDialog dlgSignOut = new WfDialog(activity);
		dlgSignOut.setDialogTitleButton(getString(R.string.wf_signout_confirm), getString(R.string.chiase_common_ok), getString(R.string.chiase_common_cancel), new View.OnClickListener() {

			@Override
			public void onClick(View v){
				signOut();
				dlgSignOut.dismiss();
			}
		}).show();
	}

	public void signOut(){
		JSONObject jsonObject = new JSONObject();
		try{
			PreferencesSystemUtil prefSysUtil = new PreferencesSystemUtil(activity);
			jsonObject.put("deviceRegister", prefSysUtil.get(WelfareConst.REGISTRATION_ID_PARAM));
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_0004, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_0004.equals(url)){
			Toast.makeText(activity, "Signed out successfully", Toast.LENGTH_LONG).show();
			gotoSignIn();
            clearAllStamps();
		}else{
			super.successUpdate(response, url);
		}
	}

	private void clearAllStamps() {
        WFMStampCategoryModel.deleteAll();
        WFMStampModel.deleteAll();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().remove(MsConst.MESSAGE_STAMP_LAST_UPDATE_DATE).apply();
    }

}
