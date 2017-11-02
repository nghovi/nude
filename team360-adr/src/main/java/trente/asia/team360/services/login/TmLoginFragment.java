package nguyenhoangviet.vpcorp.team360.services.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import org.json.JSONObject;

import nguyenhoangviet.vpcorp.team360.BuildConfig;
import nguyenhoangviet.vpcorp.team360.R;
import nguyenhoangviet.vpcorp.team360.services.member.TmMemberViewFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.LoginFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TmLoginFragment extends LoginFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
		mImgLogo.setVisibility(View.INVISIBLE);
		// mImgLogo.setImageResource(R.drawable.dr_logo);
		/* for example */
		mCbxRemember.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);

		if(WfUrlConst.WF_ACC_0003.equals(url)){
			emptyBackStack();
			gotoFragment(new TmMemberViewFragment());
		}
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_DR;
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
