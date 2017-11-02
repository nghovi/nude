package nguyenhoangviet.vpcorp.calendar.services.user;

import java.io.IOException;

import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import nguyenhoangviet.vpcorp.calendar.BuildConfig;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.defines.ClConst;
import nguyenhoangviet.vpcorp.calendar.services.calendar.MonthlyFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.SettingModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.LoginFragment;

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
			try{
				prefAccUtil.set(ClConst.SELECTED_CALENDAR_STRING, response.optString("myCalendar"));
				myself = LoganSquare.parse(response.optString("myself"), UserModel.class);
				prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, myself.key);
				prefAccUtil.set(ClConst.PREF_FILTER_TYPE, ClConst.PREF_FILTER_TYPE_USER);
				prefAccUtil.set(ClConst.PREF_ACTIVE_ROOM, "0");
				SettingModel settingModel = LoganSquare.parse(response.optString("setting"), SettingModel.class);
				prefAccUtil.saveSetting(settingModel);
				emptyBackStack();
				gotoFragment(new MonthlyFragment());
			}catch(IOException e){
				e.printStackTrace();
			}
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
