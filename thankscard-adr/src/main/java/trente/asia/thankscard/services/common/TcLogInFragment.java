package nguyenhoangviet.vpcorp.thankscard.services.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import io.realm.Realm;
import nguyenhoangviet.vpcorp.thankscard.BuildConfig;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.services.mypage.MypageFragment;
import nguyenhoangviet.vpcorp.thankscard.services.mypage.model.StampCategoryModel;
import nguyenhoangviet.vpcorp.thankscard.services.mypage.model.StampModel;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfUrlConst;
import nguyenhoangviet.vpcorp.welfare.adr.services.user.LoginFragment;

/**
 * Created by viet on 2/15/2016.
 */
public class TcLogInFragment extends LoginFragment{

	private Realm mRealm;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mRealm = Realm.getDefaultInstance();
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
		mImgLogo.setImageResource(R.drawable.tc_logo);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);

		try{
			prefAccUtil.set(TcConst.PREF_TEMPLATE_ID, response.getJSONObject("myselfInfo").optString("DEF_TEMPLATE_ID"));
			prefAccUtil.set(TcConst.PREF_TEMPLATE_PATH, response.getJSONObject("myselfInfo").optString("DEF_TEMPLATE_PATH"));
			prefAccUtil.set(TcConst.PREF_POINT_GOLD, response.getJSONObject("myselfInfo").optString("POINT_GOLD"));
			prefAccUtil.set(TcConst.PREF_POINT_SILVER, response.getJSONObject("myselfInfo").optString("POINT_SILVER"));
			prefAccUtil.set(TcConst.PREF_POINT_BRONZE, response.getJSONObject("myselfInfo").optString("POINT_BRONZE"));
			log(response.optJSONObject("myselfInfo").toString());
		}catch(JSONException ex){
			ex.printStackTrace();
		}

		if(WfUrlConst.WF_ACC_0003.equals(url)){
			emptyBackStack();
//			saveStamps(response);
			gotoFragment(new MypageFragment());
		}
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

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_TC;
	}

	private void saveStamps(JSONObject response){
		List<StampCategoryModel> stampCategories = CCJsonUtil.convertToModelList(response.optString("stampCategories"), StampCategoryModel.class);
		String lastUpdateDate = response.optString("lastUpdateDate");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		// preferences.edit().putString(TcConst.MESSAGE_STAMP_LAST_UPDATE_DATE, lastUpdateDate).apply();

		mRealm.beginTransaction();
		for(StampCategoryModel category : stampCategories){
			if(category.deleteFlag){
				StampCategoryModel.deleteStampCategory(mRealm, category.key);
			}else{
				StampCategoryModel wfmStampCategory = StampCategoryModel.getCategory(mRealm, category.key);
				if(wfmStampCategory == null){
					mRealm.copyToRealm(category);
				}else{
					wfmStampCategory.updateStampCategory(category);
					for(StampModel stamp : category.stamps){
						if(stamp.deleteFlag){
							StampModel.deleteStamp(mRealm, stamp.key);
						}else{
							StampModel wfmStamp = StampModel.getStamp(mRealm, stamp.key);
							if(wfmStamp == null){
								wfmStampCategory.stamps.add(stamp);
							}else{
								wfmStamp.updateStamp(wfmStamp);
							}
						}
					}
				}
			}
		}
		mRealm.commitTransaction();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mRealm.close();
	}

	private void log(String msg){
		Log.e("TcLoginFragment", msg);
	}
}
