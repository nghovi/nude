package nguyenhoangviet.vpcorp.android.activity;

import java.io.File;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import asia.chiase.core.define.CCConst;
import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.define.CsConst;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.android.view.ChiaseLoadingDialog;
import nguyenhoangviet.vpcorp.android.view.util.CAObjectSerializeUtil;

/**
 * ChiaseFragment
 */
public class ChiaseFragment extends Fragment implements HttpCallback{

	/**
	 * The Activity.
	 */
	protected Activity				activity;

	/**
	 * The Loading dialog.
	 */
	protected ChiaseLoadingDialog	loadingDialog;

	/**
	 * The Alert dialog builder.
	 */
	// protected AlertDialog.Builder alertDialogBuilder;
	// protected ChiaseAlertDialog alertDialog;

	/**
	 * The Host.
	 */
	protected String				host;

	/**
	 * The Swipe rfresh.
	 */
	protected SwipeRefreshLayout	swipeRfresh;

	/**
	 * The Pref.
	 */
	// protected CAPreferences pref;

	protected Boolean				isDestroy;

	protected View					mRootView;

	public void setmIsNewFragment(boolean mIsNewFragment){
		this.mIsNewFragment = mIsNewFragment;
	}

	private boolean		mIsNewFragment	= true;
	protected ViewGroup	mFooterView;
	protected ViewGroup	mHeaderView;

	/**
	 * Instantiates a new Chiase fragment.
	 */
	public ChiaseFragment(){
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		activity = getActivity();
		loadingDialog = new ChiaseLoadingDialog(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		// alertDialogBuilder = new AlertDialog.Builder(activity);
		// alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		//
		// public void onClick(DialogInterface dialog, int id){
		// }
		// });
		try{

			isDestroy = false;

			if(mIsNewFragment){
				// alertDialog = new ChiaseAlertDialog(activity);
				mIsNewFragment = false;
				initView();
				initData();
			}else{
				ChiaseActivity act = (ChiaseActivity)activity;
				if(act != null && act.isInitData){
					((ChiaseActivity)activity).isInitData = false;
					initData();
				}
			}

			if(mFooterView != null){
				LinearLayout lnrFooter = (LinearLayout)activity.findViewById(R.id.lnr_id_footer);
				LinearLayout lnrContentFooter = (LinearLayout)activity.findViewById(R.id.qk_common_footer);
				if(lnrFooter != null && lnrContentFooter == null){
					((ViewGroup)mFooterView.getParent()).removeView(mFooterView);
					lnrFooter.addView(mFooterView);
				}
			}
			if(mHeaderView != null){
				LinearLayout lnrContentHeader = (LinearLayout)activity.findViewById(R.id.lnr_id_header_title);
				if(CCConst.ZERO.equals(lnrContentHeader.getChildCount())){
					((ViewGroup)mHeaderView.getParent()).removeView(mHeaderView);
					lnrContentHeader.addView(mHeaderView);
				}
			}
		}catch(OutOfMemoryError error){
			// if(alertDialog != null){
			// alertDialog.setMessage("Memory is not enough. Please check again.");
			// alertDialog.show();
			// }else{
			error.printStackTrace();
			// }
		}
	}

	protected void initView(){
	}

	protected void initData(){
	}

	protected void initParams(JSONObject jsonObject){
		CAObjectSerializeUtil.disabledButton((ViewGroup)mRootView, true);
	}

	/**
	 * Request load.
	 *
	 * @param url the url
	 * @param jsonObject the json object
	 * @param isAlert the is alert
	 */
	protected void requestLoad(final String url, JSONObject jsonObject, final boolean isAlert){

		if(AndroidUtil.invalidInternet(activity)){
			errorNetwork(url);
			return;
		}

		if(isAlert){
			loadingDialog.show();
		}

		initParams(jsonObject);

		HttpDelegate http = new HttpDelegate(host);
		http.get(ChiaseFragment.this, url, jsonObject, isAlert);

	}

	@Override
	public void callbackLoad(int responseCode, String url, JSONObject response, final boolean isAlert){

		if(isDestroy == null) return;
		if(swipeRfresh != null) swipeRfresh.setRefreshing(false);

		onSuccessLoad(response, isAlert, url);

	}

	public static JSONObject createSystemErrorResponse(Context context){
		JSONObject resule = new JSONObject();
		try{
			resule.put(CsConst.STATUS, CsConst.STATUS_NG);
			resule.put(CsConst.RETURN_CODE_PARAM, CsConst.ERR_CODE_SERVER_SYSTEM_EROR);
			resule.put(CsConst.MESSAGES, context.getString(R.string.system_error_msg));
		}catch(JSONException e){

		}
		return resule;
	}

	/**
	 * Request update.
	 *
	 * @param url the url
	 * @param jsonObject the json object
	 * @param isAlert the is alert
	 */
	protected void requestUpdate(final String url, JSONObject jsonObject, final boolean isAlert){

		if(AndroidUtil.invalidInternet(activity)){
			errorNetwork(url);
			return;
		}

		if(isAlert){
			loadingDialog.show();
		}

		initParams(jsonObject);

		HttpDelegate http = new HttpDelegate(host);
		http.post(ChiaseFragment.this, url, jsonObject, isAlert);

	}

	@Override
	public void callbackUpdate(int responseCode, String url, JSONObject response, final boolean isAlert){

		if(isDestroy == null) return;
		onSuccessUpdate(response, isAlert, url);

	}

	/**
	 * Request upload.
	 *
	 * @param url the url
	 * @param jsonObject the json object
	 * @param files the files
	 * @param isAlert the is alert
	 */
	protected void requestUpload(final String url, JSONObject jsonObject, Map<String, File> files, final boolean isAlert){

		if(AndroidUtil.invalidInternet(activity)){
			errorNetwork(url);
			return;
		}
		if(isAlert){
			loadingDialog.show();
		}

		initParams(jsonObject);
		Log.e("ChiaseFragment", "JsonObject: " + jsonObject.toString());
		HttpDelegate http = new HttpDelegate(host);
		http.upload(ChiaseFragment.this, url, jsonObject, files, isAlert);
	}

	@Override
	public void callbackUpload(int responseCode, String url, JSONObject response, final boolean isAlert){

		if(isDestroy == null) return;
		onSuccessUpLoad(response, isAlert, url);

	}

	/**
	 * Success load.
	 *
	 * @param response the response
	 */
	protected void onSuccessLoad(JSONObject response, boolean isAlert, String url){
	}

	/**
	 * Success upload.
	 *
	 * @param response the response
	 */
	protected void onSuccessUpLoad(JSONObject response, boolean isAlert, String url){
	}

	/**
	 * Success update.
	 *
	 * @param response the response
	 */
	protected void onSuccessUpdate(JSONObject response, boolean isAlert, String url){
	}

	/**
	 * errorNetwork
	 */
	protected void errorNetwork(String url){
		activity.runOnUiThread(new Runnable() {

			public void run(){
				Toast.makeText(activity, "No internet connection", Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * Error request.
	 */
	protected void errorRequest2(){
		Toast.makeText(activity, "http error", Toast.LENGTH_LONG).show();
	}

	/**
	 * errorMessage.
	 */
	protected void errorMessage(){
	}

	@Override
	public void onPause(){
		super.onPause();
		hideKeyBoard(activity);
	}

	public static void hideKeyBoard(Activity activity){
		// hide soft keyboard
		if(activity != null && activity.getCurrentFocus() != null){
			InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/*
	 * dismiss loading dialog and swipeRefresh
	 */
	protected void dismissLoad(){
		if(loadingDialog.isShowing() && !loadingDialog.continueShowing) loadingDialog.dismiss();
		if(swipeRfresh != null){
			swipeRfresh.setRefreshing(false);
		}
	}

	/**
	 * Goto fragment.
	 *
	 * @param fragment the fragment
	 */
	public void gotoFragment(Fragment fragment){

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.ipt_id_body, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	protected void emptyBackStack(){
		// remove fragments in back stack
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		activity = null;
		if(loadingDialog != null){
			loadingDialog.dismiss();
			loadingDialog = null;
		}
		// alertDialog = null;
		swipeRfresh = null;
		isDestroy = null;
		mRootView = null;
	}

}
