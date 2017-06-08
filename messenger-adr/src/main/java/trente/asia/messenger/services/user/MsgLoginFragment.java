package trente.asia.messenger.services.user;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.io.IOException;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.services.message.MessageFragment;
import trente.asia.messenger.services.message.model.SSStampCategoryModel;
import trente.asia.messenger.services.message.model.WFMStampCategoryModel;
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
            loadStamps();
        }
    }

    private void loadStamps() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String lastUpdateDate = preferences.getString(MsConst.MESSAGE_STAMP_LAST_UPDATE_DATE, null);
        JSONObject jsonObject = new JSONObject();
        // if(lastUpdateDate != null){
        // try{
        // jsonObject.put("lastUpdateDate", lastUpdateDate);
        // }catch(JSONException e){
        // e.printStackTrace();
        // }
        // }
        requestLoad(MsConst.API_MESSAGE_STAMP_CATEGORY_LIST, jsonObject, false);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (MsConst.API_MESSAGE_STAMP_CATEGORY_LIST.equals(url)) {
            saveStamps(response);
            emptyBackStack();
            gotoFragment(new MessageFragment());
        } else {
            super.successLoad(response, url);
        }
    }

    private void saveStamps(JSONObject response){
        List<SSStampCategoryModel> stampCategories = CCJsonUtil.convertToModelList(response.optString("stampCategories"), SSStampCategoryModel.class);
        String lastUpdateDate = response.optString("lastUpdateDate");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().putString(MsConst.MESSAGE_STAMP_LAST_UPDATE_DATE, lastUpdateDate).apply();

//        log("categories size: " + stampCategories.size());
        if(!CCCollectionUtil.isEmpty(stampCategories)) {
            // save local database
            ActiveAndroid.beginTransaction();
            for (SSStampCategoryModel category : stampCategories) {
                WFMStampCategoryModel categoryModel = new WFMStampCategoryModel(category);
                categoryModel.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            ActiveAndroid.endTransaction();
        }

        List<WFMStampCategoryModel> lstCategory = new Select().from(WFMStampCategoryModel.class).execute();
//        log("Number of categories: " + lstCategory.size());
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
