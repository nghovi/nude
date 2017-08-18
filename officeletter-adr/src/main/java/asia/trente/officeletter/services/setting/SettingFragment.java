package asia.trente.officeletter.services.setting;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.fragment.AbstractListFragment;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import asia.trente.officeletter.commons.fragment.OLLogInFragment;
import io.realm.Realm;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by tien on 8/18/2017.
 */

public class SettingFragment extends AbstractOLFragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_setting, container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(null, getString(R.string.chiase_setting_title), null);
        getView().findViewById(R.id.lnr_account_info).setOnClickListener(this);
        getView().findViewById(R.id.lnr_about_app).setOnClickListener(this);
        getView().findViewById(R.id.lnr_contact_us).setOnClickListener(this);
        getView().findViewById(R.id.lnr_term).setOnClickListener(this);
        getView().findViewById(R.id.lnr_policy).setOnClickListener(this);
        getView().findViewById(R.id.lnr_signout).setOnClickListener(this);
    }

    @Override
    protected int getFooterItemId() {
        return R.id.lnr_view_common_footer_setting;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnr_account_info:
                gotoFragment(new AccountInfoFragment());
                break;
            case R.id.lnr_about_app:
                break;
            case R.id.lnr_contact_us:
                break;
            case R.id.lnr_term:
                break;
            case R.id.lnr_policy:
                break;
            case R.id.lnr_signout:
                showSignOutConfirmDialog();
                break;
        }
    }

    protected void gotoBrowser(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    protected void gotoBrowserWithLang(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + "/" + Resources.getSystem().getConfiguration().locale.getLanguage()));
        startActivity(browserIntent);
    }


    private void showSignOutConfirmDialog(){
        final WfDialog dlgSignOut = new WfDialog(activity);
        dlgSignOut.setDialogTitleButton(getString(R.string.wf_signout_confirm), getString(R.string.chiase_common_ok), getString(R.string.chiase_common_cancel), new View.OnClickListener() {

            @Override
            public void onClick(View v){
                signOut();
                dlgSignOut.dismiss();
            }
        }).show();
    }

    public void signOut(){
        JSONObject jsonObject = new JSONObject();
        try{
            PreferencesSystemUtil prefSysUtil = new PreferencesSystemUtil(activity);
            jsonObject.put("deviceRegister", prefSysUtil.get(WelfareConst.REGISTRATION_ID_PARAM));
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        requestUpdate(WfUrlConst.WF_ACC_0004, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url){
        if(WfUrlConst.WF_ACC_0004.equals(url)){
            Toast.makeText(activity, "Signed out successfully", Toast.LENGTH_LONG).show();
            gotoSignIn();
        }else{
            super.successUpdate(response, url);
        }
    }

    @Override
    protected void gotoSignIn() {
        super.gotoSignIn();
        gotoFragment(new OLLogInFragment());
    }


}
