package trente.asia.messenger.services.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.services.message.MessageFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.services.user.LoginFragment;

/**
 * MsgLoginFragment
 *
 * @author TrungND
 */
public class MsgLoginFragment extends LoginFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        host = BuildConfig.HOST;
    }

    @Override
    protected void initView() {
        super.initView();
        mImgLogo.setImageResource(R.drawable.ms_logo);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        super.successUpdate(response, url);

        if (WfUrlConst.WF_ACC_0003.equals(url)) {
            try{
                prefAccUtil.set(MsConst.PREF_ACTIVE_BOARD_ID, response.getJSONObject("myselfInfo").optString("DPET_BOARD_ID"));
            }catch (JSONException ex){
                ex.printStackTrace();
            }

            emptyBackStack();
            gotoFragment(new MessageFragment());
        }
    }

    @Override
    protected String getServiceCd() {
        return WelfareConst.SERVICE_CD_MS;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forget_password:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(host + WfUrlConst.FORGET_PASSWORD)));
                break;
            default:
                break;
        }
    }
}
