package asia.trente.officeletter.commons.activities;

import android.os.Bundle;
import android.util.Log;

import org.w3c.dom.Document;

import asia.chiase.core.util.CCStringUtil;
import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.fragment.OLLogInFragment;
import asia.trente.officeletter.services.document.DocumentListFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * Created by tien on 8/18/2017.
 */

public class MainActivity extends WelfareActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
        UserModel userModel = prefAccUtil.getUserPref();
        if(CCStringUtil.isEmpty(userModel.key)){
            addFragment(new OLLogInFragment());
        }else{
            addFragment(new DocumentListFragment());
            log("document");
            log(userModel.key);
        }
    }

    private void log(String msg) {
        Log.e("MainAtivity", msg);
    }
}
