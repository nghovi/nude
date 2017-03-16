package trente.asia.welfare.adr.activity;

import java.io.File;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.android.define.CsConst;
import trente.asia.android.exception.CAException;
import trente.asia.android.util.AndroidUtil;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfErrorConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * WelfareFragment
 *
 * @author TrungND
 */
public abstract class WelfareFragment extends ChiaseFragment implements WelfareActivity.OnDeviceBackButtonClickListener{

	protected PreferencesAccountUtil	prefAccUtil;
	protected boolean					isChangedData			= false;
	protected UserModel					myself;
	public boolean						isClickNotification		= false;
	protected Integer					lnrContentId;
	public static final int				MARGIN_LEFT_RIGHT		= WelfareUtil.dpToPx(16);
	public static final int				MARGIN_TEXT_TOP_BOTTOM	= WelfareUtil.dpToPx(4);

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
	}

	@Override
	protected void initView(){
		super.initView();
		((WelfareActivity)activity).setOnActivityResultListener(null);
		prefAccUtil = new PreferencesAccountUtil(activity);
		myself = prefAccUtil.getUserPref();
		if(!((WelfareActivity)activity).isClearRegistrationId && CCStringUtil.isEmpty(myself.key)){
			clearRegistrationId();
			((WelfareActivity)activity).isClearRegistrationId = true;
		}
	}

	protected void initHeader(Integer leftIconId, String title, Integer rightIconId){
		ImageView imgLeftIcon = (ImageView)activity.findViewById(R.id.img_id_header_left_icon);
		if(leftIconId != null){
			imgLeftIcon.setImageResource(leftIconId);
			imgLeftIcon.setVisibility(View.VISIBLE);
			imgLeftIcon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					onClickBackBtn();
				}
			});
		}else{
			imgLeftIcon.setVisibility(View.INVISIBLE);
		}

		if(!CCStringUtil.isEmpty(title)){
			TextView txtTitle = (TextView)activity.findViewById(R.id.txt_id_header_title);
			txtTitle.setText(title);
		}

		ImageView imgRightIcon = (ImageView)activity.findViewById(R.id.img_id_header_right_icon);
		if(rightIconId != null){
			// LinearLayout lnrRightIcon = (LinearLayout)activity.findViewById(R.id.lnr_header_right_icon);
			imgRightIcon.setVisibility(View.VISIBLE);
			imgRightIcon.setImageResource(rightIconId);
		}else{
			imgRightIcon.setVisibility(View.INVISIBLE);
		}
	}

	protected void updateHeader(String title){
		if(!CCStringUtil.isEmpty(title)){
			TextView txtTitle = (TextView)activity.findViewById(R.id.txt_id_header_title);
			txtTitle.setText(title);
		}
	}

	protected String getServiceCd(){
		return null;
	}

	/**
	 * On success load.
	 *
	 * @param response the response
	 */
	@Override
	protected void onSuccessLoad(JSONObject response, boolean isAlert, String url){
		if(response == null){
			response = createSystemErrorResponse();
		}
		String status = response.optString(CsConst.STATUS);
		String returnCd = response.optString(WelfareConst.RETURN_CODE_PARAM);
		if(CsConst.STATUS_OK.equals(status) && (CCStringUtil.isEmpty(returnCd) || CCConst.NONE.equals(returnCd))){
			UserModel userModel = CCJsonUtil.convertToModel(response.optString("myself"), UserModel.class);
			if(userModel != null && !CCStringUtil.isEmpty(userModel.key)){
				if(userModel.dept == null){
					userModel.dept = myself.dept;
				}
				myself = userModel;
				prefAccUtil.saveUserPref(myself);
			}
			SettingModel settingModel = CCJsonUtil.convertToModel(response.optString("setting"), SettingModel.class);
			prefAccUtil.saveSetting(settingModel);
			successLoad(response, url);

			if(lnrContentId != null && getView() != null){
				View lnrContent = getView().findViewById(lnrContentId);
				if(lnrContent != null && lnrContent.getVisibility() == View.INVISIBLE){
					lnrContent.setVisibility(View.VISIBLE);
				}
			}
			dismissLoad();
		}else{
			loadingDialog.continueShowing = false;
			dismissLoad();
			commonNotSuccess(response);
		}
	}

	private JSONObject createSystemErrorResponse(){
		JSONObject resule = new JSONObject();
		try{
			resule.put(CsConst.STATUS, CsConst.STATUS_NG);
			resule.put(WelfareConst.RETURN_CODE_PARAM, WfErrorConst.ERR_CODE_SERVER_SYSTEM_EROR);
			resule.put(CsConst.MESSAGES, getString(R.string.system_error_msg));
		}catch(JSONException e){

		}
		return resule;
	}

	protected void commonNotSuccess(JSONObject response){
		String returnCd = response.optString(WelfareConst.RETURN_CODE_PARAM);
		if(WfErrorConst.ERR_CODE_INVALID_ACCOUNT.equals(returnCd)){
			gotoSignIn();
		}else if(WfErrorConst.ERR_CODE_INVALID_VERSION_AND_UPDATE.equals(returnCd)){
			// show dialog
			WfDialog dlgUpgrade = new WfDialog(activity);
			dlgUpgrade.setDialogUpgradeVersion();
			dlgUpgrade.show();
			gotoSignIn();
		}else{
			String msg = response.optString(CsConst.MESSAGES);
			alertDialog.setMessage(Html.fromHtml(msg).toString());
			alertDialog.show();
		}
	}

	protected void cancelNotification(Context context){
		String notificationService = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(notificationService);
		// notificationManager.cancel(notificationId);
		notificationManager.cancelAll();
	}

	protected void gotoSignIn(){
		cancelNotification(activity);
		prefAccUtil.clear();
		emptyBackStack();
	}

	/**
	 * Success load.
	 *
	 * @param response the response
	 */
	protected void successLoad(JSONObject response, String url){
	}

	/**
	 * On success load.
	 *
	 * @param response the response
	 */
	@Override
	protected void onSuccessUpdate(JSONObject response, boolean isAlert, String url){
		String status = response.optString(CsConst.STATUS);
		if(CsConst.STATUS_OK.equals(status)){
			dismissLoad();
			successUpdate(response, url);
		}else{
			loadingDialog.continueShowing = false;
			dismissLoad();
			commonNotSuccess(response);
		}
	}

	/**
	 * Success update.
	 *
	 * @param response the response
	 */
	protected void successUpdate(JSONObject response, String url){
	}

	/**
	 * On success upload.
	 *
	 * @param response the response
	 */
	@Override
	protected void onSuccessUpLoad(JSONObject response, boolean isAlert, String url){
		String status = response.optString(CsConst.STATUS);
		if(CsConst.STATUS_OK.equals(status)){
			dismissLoad();
			successUpload(response, url);
		}else{
			loadingDialog.continueShowing = false;
			dismissLoad();
			commonNotSuccess(response);
		}
	}

	/**
	 * Success upload.
	 *
	 * @param response the response
	 */
	protected void successUpload(JSONObject response, String url){
	}

	@Override
	public void onClickDeviceBackButton(){
		onClickBackBtn();
	}

	protected void onClickBackBtn(){
		if(getFragmentManager().getBackStackEntryCount() <= 1){
			((WelfareActivity)activity).setDoubleBackPressedToFinish();
		}else{
			if(isChangedData){
				((ChiaseActivity)activity).isInitData = true;
			}
			getFragmentManager().popBackStack();
		}
	}

	@Override
	protected void initParams(JSONObject jsonObject){
		super.initParams(jsonObject);

		if(prefAccUtil != null){
			UserModel userModel = prefAccUtil.getUserPref();

			try{
				jsonObject.put("loginUserId", CCStringUtil.toString(userModel.key));
				jsonObject.put("companyId", CCStringUtil.toString(userModel.companyId));
				jsonObject.put(CsConst.ARG_TOKEN, userModel.token);
				jsonObject.put("language", Resources.getSystem().getConfiguration().locale.getLanguage());
				TimeZone timeZone = TimeZone.getDefault();
				jsonObject.put(CsConst.ARG_TIMEZONE, timeZone.getID());
				jsonObject.put(CsConst.ARG_DEVICE, "A");
				jsonObject.put(CsConst.ARG_VERSION, AndroidUtil.getVersionName(getContext()));
				jsonObject.put("serviceCd", getServiceCd());
			}catch(JSONException ex){
				new CAException(ex);
			}
		}
	}

	protected void addTextWatcher(final View btn, final List<View> views){
		btn.setEnabled(false);
		TextWatcher tw = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}

			@Override
			public void afterTextChanged(Editable s){
				for(View view : views){
					if(CCStringUtil.isEmpty(((TextView)view).getText().toString())){
						btn.setEnabled(false);
						return;
					}
				}

				btn.setEnabled(true);
			}
		};

		for(View view : views){
			((TextView)(view)).addTextChangedListener(tw);
		}
	}

	protected void focusEditText(EditText editText){
		editText.requestFocus();
		editText.setSelection(editText.getText().length());
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	protected void gotoBrowser(String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	private void clearRegistrationId(){
		JSONObject jsonObject = new JSONObject();
		try{
			PreferencesSystemUtil prefSysUtil = new PreferencesSystemUtil(activity);
			jsonObject.put(WelfareConst.REGISTRATION_ID_PARAM, prefSysUtil.get(WelfareConst.REGISTRATION_ID_PARAM));
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_DVC_0002, jsonObject, false);
	}

	protected void deleteAppFolder(File appFolder){
		if(appFolder == null){
			return;
		}
		if(appFolder.isDirectory()){
			for(File file : appFolder.listFiles()){
				file.delete();
			}
		}
	}

	public interface OnAvatarClickListener{

		public void OnAvatarClick(String userName, String avatarPath);
	}

	@Override
	public void onPause(){
		super.onPause();
		lnrContentId = null;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		myself = null;
		prefAccUtil = null;
	}
}
