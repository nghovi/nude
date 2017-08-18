package asia.trente.officeletter.commons.fragment;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import asia.trente.officeletter.BuildConfig;
import asia.trente.officeletter.R;
import asia.trente.officeletter.services.document.DocumentListFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.services.user.LoginFragment;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 2/15/2016.
 */
public class OLLogInFragment extends LoginFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
		mImgLogo.setImageResource(R.drawable.ol_logo);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mImgLogo.getLayoutParams();
		params.width = WelfareUtil.dpToPx(50);
		params.height = WelfareUtil.dpToPx(50);
		params.gravity = Gravity.CENTER_HORIZONTAL;
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);

		if(WfUrlConst.WF_ACC_0003.equals(url)){
			emptyBackStack();
			gotoFragment(new DocumentListFragment());
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
		return WelfareConst.SERVICE_CD_OL;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void log(String msg){
		Log.e("TcLoginFragment", msg);
	}
}
