package trente.asia.addresscard.setting;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

public class SettingFragment extends AbstractAddressCardFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_setting, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_setting;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, getString(R.string.menu_setting_title), null);

		LinearLayout lnrAccInfo = (LinearLayout)getView().findViewById(R.id.lnr_account_info);
		LinearLayout lnrAboutApp = (LinearLayout)getView().findViewById(R.id.lnr_about_app);
		LinearLayout lnrTerm = (LinearLayout)getView().findViewById(R.id.lnr_term);
		LinearLayout lnrPolicy = (LinearLayout)getView().findViewById(R.id.lnr_policy);
		LinearLayout lnrSignOut = (LinearLayout)getView().findViewById(R.id.lnr_signout);
		LinearLayout lnrContactUs = (LinearLayout)getView().findViewById(R.id.lnr_contact_us);

		lnrAccInfo.setOnClickListener(this);
		lnrAboutApp.setOnClickListener(this);
		lnrTerm.setOnClickListener(this);
		lnrPolicy.setOnClickListener(this);
		lnrSignOut.setOnClickListener(this);
		lnrContactUs.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_account_info:
			gotoFragment(new AccountInfoFragment());
			break;
		case R.id.lnr_about_app:
			AboutAppFragment aboutAppFragment = new AboutAppFragment();
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
			gotoFragment(new ContactUsFragment());
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
				callSignOutApi();
				dlgSignOut.dismiss();
			}
		}).show();
	}

	public void callSignOutApi(){
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
			Toast.makeText(activity, getString(R.string.chiase_common_success_signout), Toast.LENGTH_LONG).show();
			gotoSignIn();
		}else{
			super.successUpdate(response, url);
		}
	}
}