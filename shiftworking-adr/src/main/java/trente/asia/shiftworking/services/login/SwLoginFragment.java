package nguyenhoangviet.vpcorp.shiftworking.services.login;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import nguyenhoangviet.vpcorp.shiftworking.BuildConfig;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.activities.MainActivity;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.WorktimeCheckInFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.LoginFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwLoginFragment extends LoginFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
		mImgLogo.setImageResource(R.drawable.sw_logo);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);

		if(WfUrlConst.WF_ACC_0003.equals(url)){
            ((MainActivity)activity).startTimer();
			emptyBackStack();
			gotoFragment(new WorktimeCheckInFragment());
		}
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_SW;
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
