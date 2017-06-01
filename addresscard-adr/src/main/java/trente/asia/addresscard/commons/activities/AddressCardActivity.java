package trente.asia.addresscard.commons.activities;

import android.os.Bundle;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.business.view.BusinessCardListFragment;
import trente.asia.addresscard.services.user.ACLoginFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

public class AddressCardActivity extends WelfareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_card);
        PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
        UserModel userModel = prefAccUtil.getUserPref();

        if(!CCStringUtil.isEmpty(userModel.key)){
            addFragment(new BusinessCardListFragment());
        }else{
            addFragment(new ACLoginFragment());
        }

//        addFragment(new BusinessCardListFragment());
    }
}
