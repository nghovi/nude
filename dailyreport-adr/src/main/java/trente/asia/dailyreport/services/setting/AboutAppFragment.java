package trente.asia.dailyreport.services.setting;

import android.webkit.WebView;
import android.widget.TextView;

import trente.asia.android.util.AndroidUtil;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;

/**
 * Created by viet on 2/15/2016.
 */
public class AboutAppFragment extends AbstractDRFragment{

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_about;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_setting;
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.wf_about_app), null);

		TextView version = (TextView)getView().findViewById(R.id.txt_fragment_about_version);
		version.setText(getString(R.string.fragment_about_version, AndroidUtil.getVersionName(activity)));

		WebView webView = (WebView)getView().findViewById(R.id.webview_fragment_about);
		webView.loadUrl("file:///android_asset/license_info.html");
	}

}
