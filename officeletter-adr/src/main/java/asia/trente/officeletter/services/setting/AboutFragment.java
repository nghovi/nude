package asia.trente.officeletter.services.setting;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;

/**
 * Created by viet on 2/15/2016.
 */
public class AboutFragment extends AbstractOLFragment{

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_about;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	protected void initView() {
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.wf_about_app), null);
		buildBodyLayout();
	}

	public void buildBodyLayout(){
		TextView version = (TextView)getView().findViewById(R.id.txt_fragment_about_version);
		version.setText(getString(R.string.wf_about_version, AndroidUtil.getVersionName(activity)));

		WebView webView = (WebView)getView().findViewById(R.id.webview_fragment_about);
		webView.loadUrl("file:///android_asset/license_info.html");
	}

}
