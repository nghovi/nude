package nguyenhoangviet.vpcorp.team360.common.activities;

import android.os.Bundle;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.team360.R;
import nguyenhoangviet.vpcorp.team360.services.member.TmMemberViewFragment;
import nguyenhoangviet.vpcorp.team360.services.login.TmLoginFragment;

import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

public class MainActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();
		// Bundle mExtras = getIntent().getExtras();
		if(!CCStringUtil.isEmpty(userModel.key)){
			addFragment(new TmMemberViewFragment());
			//showLogInFragment();
		}else{
			showLogInFragment();
		}
	}

	private void showLogInFragment(){
		TmLoginFragment loginFragment = new TmLoginFragment();
		addFragment(loginFragment);
	}
}
