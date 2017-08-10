package trente.asia.thankscard.services.rank;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import io.realm.Realm;
import trente.asia.thankscard.R;
import trente.asia.thankscard.activities.MainActivity;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.mypage.model.StampCategoryModel;
import trente.asia.thankscard.services.setting.TcContactUsFragment;
import trente.asia.thankscard.services.setting.TcStageInformationFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

public class TCSettingFragment extends AbstractTCFragment implements View.OnClickListener{

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_tcsetting;
	}

	@Override
	public boolean hasBackBtn(){
		return true;
	}

	@Override
	public boolean hasSettingBtn(){
		return false;
	}

	@Override
	public int getFooterItemId(){
		return getArguments().getInt(TcConst.ACTIVE_FOOTER_ITEM_ID, 0);
	}

	@Override
	public int getTitle(){
		return R.string.fragment_setting_title;
	}

	@Override
	public void buildBodyLayout(){
		LinearLayout lnrAccountInfo = (LinearLayout)getView().findViewById(R.id.lnr_account_info);
		LinearLayout lnrAbout = (LinearLayout)getView().findViewById(R.id.lnr_about_app);
		LinearLayout lnrPolicy = (LinearLayout)getView().findViewById(R.id.lnr_policy);
		LinearLayout lnrTerm = (LinearLayout)getView().findViewById(R.id.lnr_term);
		LinearLayout lnrSignOut = (LinearLayout)getView().findViewById(R.id.lnr_signout);
		LinearLayout lnrContactUs = (LinearLayout)getView().findViewById(R.id.lnr_contact_us);
		TextView lnrStageInformation = (TextView)getView().findViewById(R.id.txt_stage_infor);

		lnrAccountInfo.setOnClickListener(this);
		lnrAbout.setOnClickListener(this);
		lnrPolicy.setOnClickListener(this);
		lnrTerm.setOnClickListener(this);
		lnrSignOut.setOnClickListener(this);
		lnrContactUs.setOnClickListener(this);
		lnrStageInformation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_account_info:
			TCAccountInfoFragment fragment = new TCAccountInfoFragment();
			Bundle args = new Bundle();
			args.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, getFooterItemId());
			fragment.setArguments(args);
			((MainActivity)getActivity()).addFragment(fragment);
			break;
		case R.id.lnr_about_app:
			TCAboutFragment aboutAppFragment = new TCAboutFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, getFooterItemId());
			aboutAppFragment.setArguments(bundle);
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
			gotoFragment(new TcContactUsFragment());
			break;
		case R.id.txt_stage_infor:
			gotoFragment(new TcStageInformationFragment());
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
			Realm realm = Realm.getDefaultInstance();
			realm.beginTransaction();
			realm.where(StampCategoryModel.class).findAll().deleteAllFromRealm();
			realm.commitTransaction();
			realm.close();
		}else{
			super.successUpdate(response, url);
		}
	}
}
