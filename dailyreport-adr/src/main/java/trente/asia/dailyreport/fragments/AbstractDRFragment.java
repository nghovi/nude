package trente.asia.dailyreport.fragments;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import trente.asia.android.define.CsConst;
import trente.asia.dailyreport.BuildConfig;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.activities.ActivityFragment;
import trente.asia.dailyreport.services.kpi.ActualPlanAddFragment;
import trente.asia.dailyreport.services.kpi.UserActualFragment;
import trente.asia.dailyreport.services.other.OthersFragment;
import trente.asia.dailyreport.services.report.MyReportFragment;
import trente.asia.dailyreport.services.setting.DRSettingFragment;
import trente.asia.dailyreport.services.user.DrLoginFragment;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfErrorConst;

/**
 * Created by viet on 7/8/2016.
 */
public abstract class AbstractDRFragment extends WelfareFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(getFragmentLayoutId(), container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		buildBodyLayout();
		buildFooter();
	}

	/**
	 * All subclass must declare its layout file
	 *
	 * @return layout resource id
	 */
	public abstract int getFragmentLayoutId();

	/**
	 * return 0 if we don't want to show footer
	 *
	 * @return view id of the footer item. E.g. lnr_view_common_footer_mypage
	 */
	public abstract int getFooterItemId();

	/**
	 * @return string resource for fragment title
	 */
	// public abstract int getTitle();

	public abstract void buildBodyLayout();

	@Override
	protected void commonNotSuccess(JSONObject response){
		String returnCd = response.optString(CsConst.RETURN_CODE_PARAM);
		if(WfErrorConst.DR_ERR_CODE_DEPT_CHANGED.equals(returnCd)){
			emptyBackStack();
			gotoFragment(new MyReportFragment());
		}else{
			super.commonNotSuccess(response);
		}
	}

	private int activeFooterItemId;

	protected void buildFooter(){
		final int footerItemId = getFooterItemId();
		if(footerItemId != 0){
			View.OnClickListener listener = new View.OnClickListener() {

				@Override
				public void onClick(View view){
					switch(view.getId()){
					case R.id.lnr_view_common_footer_myreport:
						onClickFooterItemMyReport();
						break;
					case R.id.lnr_view_common_footer_others:
						onClickFooterOthersReport();
						break;
					case R.id.lnr_view_common_footer_ap:
						onClickFooterActionPlans();
						break;
					case R.id.lnr_view_common_footer_kpi:
						onClickFooterItemKpi();
						break;
					case R.id.lnr_view_common_footer_setting:
						onClickFooterItemSetting();
						break;
					default:
						break;
					}
				}
			}; // end listener

			getView().findViewById(R.id.lnr_view_common_footer_myreport).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_others).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_ap).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_kpi).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_setting).setOnClickListener(listener);
			setSelectedFooterItem(footerItemId);

		}
	}

	private void setSelectedFooterItem(int footerItemId){
		ImageView imgFooterItem = (ImageView)((RelativeLayout)getView().findViewById(footerItemId)).getChildAt(0);
		imgFooterItem.setAlpha((float)1);

		TextView txtFooterItem = (TextView)((RelativeLayout)getView().findViewById(footerItemId)).getChildAt(1);
		txtFooterItem.setTextColor(Color.WHITE);

		switch(footerItemId){
		case R.id.lnr_view_common_footer_myreport:
			imgFooterItem.setImageResource(R.drawable.dr_footer_report);
			break;
		case R.id.lnr_view_common_footer_others:
			imgFooterItem.setImageResource(R.drawable.dr_footer_report_all);
			break;
		case R.id.lnr_view_common_footer_kpi:
			imgFooterItem.setImageResource(R.drawable.dr_footer_kpi);
			break;
		case R.id.lnr_view_common_footer_ap:
			imgFooterItem.setImageResource(R.drawable.dr_footer_activity);
			break;
		case R.id.lnr_view_common_footer_setting:
			imgFooterItem.setImageResource(R.drawable.dr_footer_setting);
			break;
		default:
			break;
		}
	}

	public void onClickFooterItemSetting(){
		if(activeFooterItemId != R.id.lnr_view_common_footer_setting){
			activeFooterItemId = R.id.lnr_view_common_footer_setting;
			emptyBackStack();
			gotoFragment(new DRSettingFragment());
		}
	}

	public void onClickFooterItemMyReport(){
		if(activeFooterItemId != R.id.lnr_view_common_footer_myreport){
			activeFooterItemId = R.id.lnr_view_common_footer_myreport;
			emptyBackStack();
			gotoFragment(new MyReportFragment());
		}
	}

	public void onClickFooterOthersReport(){
		if(activeFooterItemId != R.id.lnr_view_common_footer_others){
			activeFooterItemId = R.id.lnr_view_common_footer_others;
			emptyBackStack();
			gotoFragment(new OthersFragment());
		}
	}

	public void onClickFooterItemKpi(){
		if(activeFooterItemId != R.id.lnr_view_common_footer_kpi){
			activeFooterItemId = R.id.lnr_view_common_footer_kpi;
			emptyBackStack();
			gotoFragment(new UserActualFragment());
		}
	}

	public void onClickFooterActionPlans(){
		if(activeFooterItemId != R.id.lnr_view_common_footer_ap){
			activeFooterItemId = R.id.lnr_view_common_footer_ap;
			emptyBackStack();
			gotoFragment(new ActualPlanAddFragment());
		}
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_DR;
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new DrLoginFragment());
	}
}
