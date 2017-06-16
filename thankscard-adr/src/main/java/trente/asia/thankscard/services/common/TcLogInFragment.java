package trente.asia.thankscard.services.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.mypage.MypageFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.services.user.LoginFragment;

/**
 * Created by viet on 2/15/2016.
 */
public class TcLogInFragment extends LoginFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
		mImgLogo.setImageResource(R.drawable.tc_logo);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);

		try{
			prefAccUtil.set(TcConst.PREF_TEMPLATE_ID, response.getJSONObject("myselfInfo").optString("DEF_TEMPLATE_ID"));
			prefAccUtil.set(TcConst.PREF_TEMPLATE_PATH, response.getJSONObject("myselfInfo").optString("DEF_TEMPLATE_PATH"));
		}catch (JSONException ex){
			ex.printStackTrace();
		}

		if(WfUrlConst.WF_ACC_0003.equals(url)){
			emptyBackStack();
			gotoFragment(new MypageFragment());
		}
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

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_TC;
	}
}
