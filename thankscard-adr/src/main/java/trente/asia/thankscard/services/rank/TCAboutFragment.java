package trente.asia.thankscard.services.rank;

import android.webkit.WebView;
import android.widget.TextView;

import trente.asia.android.util.AndroidUtil;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.fragments.AbstractTCFragment;

/**
 * Created by viet on 2/15/2016.
 */
public class TCAboutFragment extends AbstractTCFragment{

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_about;
	}

	@Override
	public boolean hasBackBtn(){
		return true;
	}

	@Override
	public boolean hasSettingBtn(){
		return false;
	}

	@Override
	public int getFooterItemId(){
		return getArguments().getInt(TcConst.ACTIVE_FOOTER_ITEM_ID, 0);
	}

	@Override
	public int getTitle(){
		return R.string.wf_about_app;
	}

	@Override
	public void buildBodyLayout(){
		TextView version = (TextView)getView().findViewById(R.id.txt_fragment_about_version);
		version.setText(getString(R.string.wf_about_version, AndroidUtil.getVersionName(activity)));

		WebView webView = (WebView)getView().findViewById(R.id.webview_fragment_about);
		webView.loadUrl("file:///android_asset/license_info.html");
	}

}
