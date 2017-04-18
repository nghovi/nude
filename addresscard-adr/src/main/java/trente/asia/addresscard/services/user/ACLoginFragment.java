package trente.asia.addresscard.services.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;

import org.json.JSONObject;

import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.services.user.LoginFragment;

/**
 * Created by tien on 4/17/2017.
 */

public class ACLoginFragment extends LoginFragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        host = BuildConfig.HOST;
    }

    @Override
    protected void initView(){
        super.initView();
        mImgLogo.setImageResource(R.drawable.dr_logo);
    }

    @Override
    protected void successUpdate(JSONObject response, String url){
        super.successUpdate(response, url);
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
