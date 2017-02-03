package trente.asia.calendar.commons.activites;

import android.os.Bundle;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.user.ClLoginFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

public class MainClActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
        UserModel userModel = prefAccUtil.getUserPref();
        Bundle mExtras = getIntent().getExtras();

//        commit3
        if(!CCStringUtil.isEmpty(userModel.key)){
            addFragment(new ClLoginFragment());
//            if(mExtras != null){
//                showFragment(mExtras);
//            }else{
//                addFragment(new MessageFragment());
//            }
        }else{
            addFragment(new ClLoginFragment());
        }
	}
}
