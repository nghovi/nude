package trente.asia.welfare.adr.services.user;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class LoginFragment extends WelfareFragment implements View.OnClickListener{

	protected Button			mBtnSignIn;
	protected ChiaseEditText	mEdtUserName;
	protected ChiaseEditText	mEdtPassWord;
	protected LinearLayout		mLnrSignin;

	protected TextView			mTxtForgetPass;
	protected CheckBox			mCbxRemember;
	protected ImageView			mImgLogo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_login, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		mBtnSignIn = (Button)getView().findViewById(R.id.btn_signIn);
		mEdtUserName = (ChiaseEditText)getView().findViewById(R.id.edt_username);
		mEdtPassWord = (ChiaseEditText)getView().findViewById(R.id.edt_password);
		mLnrSignin = (LinearLayout)getView().findViewById(R.id.lnr_id_signin);

		mCbxRemember = (CheckBox)getView().findViewById(R.id.chk_id_remember);
		mImgLogo = (ImageView)getView().findViewById(R.id.img_id_logo);
		mTxtForgetPass = (TextView)getView().findViewById(R.id.txt_forget_password);
		mTxtForgetPass.setOnClickListener(this);

		addTextWatcher(mBtnSignIn, Arrays.asList((View)mEdtUserName, mEdtPassWord));
		mBtnSignIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				signIn();
			}
		});

		// care remember logic
		PreferencesSystemUtil preferencesSystemUtil = new PreferencesSystemUtil(activity);
		if(!CCStringUtil.isEmpty(preferencesSystemUtil.get(PreferencesSystemUtil.KEY_PREF_USER_NAME))){
			mEdtUserName.setText(preferencesSystemUtil.get(PreferencesSystemUtil.KEY_PREF_USER_NAME));
			mCbxRemember.setChecked(true);
		}

	}

	@Override
	protected void initData(){
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		default:
			break;
		}
	}

	protected void signIn(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(mLnrSignin, null);
		try{
			PreferencesSystemUtil prefSysUtil = new PreferencesSystemUtil(activity);
			jsonObject.put("deviceType", WelfareConst.ANDROID_DEVICE_CODE);
			jsonObject.put(WelfareConst.REGISTRATION_ID_PARAM, prefSysUtil.get(WelfareConst.REGISTRATION_ID_PARAM));
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_ACC_0003, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_ACC_0003.equals(url)){
			UserModel userModel = CCJsonUtil.convertToModel(response.optString("myself"), UserModel.class);
			prefAccUtil.saveUserPref(userModel);

			// check remember logic
			PreferencesSystemUtil preferencesSystemUtil = new PreferencesSystemUtil(activity);
			if(mCbxRemember.isChecked()){
				preferencesSystemUtil.set(PreferencesSystemUtil.KEY_PREF_USER_NAME, userModel.userAccount);
			}else{
				preferencesSystemUtil.removeKey(PreferencesSystemUtil.KEY_PREF_USER_NAME);
			}
		}else{
			super.successUpdate(response, url);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mBtnSignIn = null;
		mEdtUserName = null;
		mEdtPassWord = null;
		mLnrSignin = null;

		mTxtForgetPass = null;
		mCbxRemember = null;
		mImgLogo = null;
	}
}
