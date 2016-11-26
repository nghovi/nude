package trente.asia.messenger.services.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import trente.asia.android.util.AndroidUtil;
import trente.asia.messenger.R;
import trente.asia.messenger.fragment.AbstractMsgFragment;

public class MsAboutFragment extends AbstractMsgFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		// Inflate the layout for this fragment
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_about, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		initHeader(trente.asia.welfare.adr.R.drawable.wf_back_white, getString(R.string.wf_about_app), null);

		TextView version = (TextView)getView().findViewById(R.id.txt_fragment_about_version);
		version.setText(getString(R.string.wf_about_version, AndroidUtil.getVersionName(activity)));

		WebView webView = (WebView)getView().findViewById(R.id.webview_fragment_about);
		webView.loadUrl("file:///android_asset/license/license-dependency.html");
	}
}
