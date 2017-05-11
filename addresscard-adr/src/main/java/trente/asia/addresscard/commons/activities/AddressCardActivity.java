package trente.asia.addresscard.commons.activities;

import android.os.Bundle;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.card.CardDetailShowFragment;
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
            addFragment(new CardDetailShowFragment());
        }else{
            addFragment(new ACLoginFragment());
        }

//        addFragment(new ACLoginFragment());
    }
}
