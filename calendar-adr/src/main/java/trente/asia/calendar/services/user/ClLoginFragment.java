package trente.asia.calendar.services.user;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.services.calendar.MonthlyFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.services.user.LoginFragment;

/**
 * ClLoginFragment
 *
 * @author TrungND
 */
public class ClLoginFragment extends LoginFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
		mImgLogo.setImageResource(R.drawable.cl_logo);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);

		if(WfUrlConst.WF_ACC_0003.equals(url)){
			prefAccUtil.set(ClConst.SELECTED_CALENDAR_STRING, response.optString("myCalendar"));
			myself = CCJsonUtil.convertToModel(response.optString("myself"), UserModel.class);
			prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, myself.key);
			SettingModel settingModel = CCJsonUtil.convertToModel(response.optString("setting"), SettingModel.class);
			prefAccUtil.saveSetting(settingModel);
			emptyBackStack();
			gotoFragment(new MonthlyFragment());
		}
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_CL;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.txt_forget_password:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(host + WfUrlConst.FORGET_PASSWORD)));
			break;
		default:
			break;
		}
	}
}
