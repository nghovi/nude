package nguyenhoangviet.vpcorp.calendar.services.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.AbstractClFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClAboutAppFragment extends AbstractClFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_clabout_app, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_setting;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.wf_back_white, getString(R.string.wf_about_app), null);

		TextView version = (TextView)getView().findViewById(R.id.txt_fragment_about_version);
		version.setText(getString(R.string.chiase_about_app_version, AndroidUtil.getVersionName(activity)));

		WebView webView = (WebView)getView().findViewById(R.id.webview_fragment_about);
		webView.loadUrl("file:///android_asset/license_info.html");
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}
}
