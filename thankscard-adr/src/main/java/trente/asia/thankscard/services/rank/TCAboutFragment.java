package nguyenhoangviet.vpcorp.thankscard.services.rank;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.commons.defines.TcConst;
import nguyenhoangviet.vpcorp.thankscard.fragments.AbstractTCFragment;

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

		if (getFooterItemId() == 0) {
			getView().findViewById(R.id.footer).setVisibility(View.GONE);
		}
	}

}
