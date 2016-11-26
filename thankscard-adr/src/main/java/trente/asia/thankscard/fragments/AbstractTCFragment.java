package trente.asia.thankscard.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.common.TcLogInFragment;
import trente.asia.thankscard.services.mypage.MypageFragment;
import trente.asia.thankscard.services.posted.PostTCListFragment;
import trente.asia.thankscard.services.rank.RankingFragment;
import trente.asia.thankscard.services.rank.TCSettingFragment;
import trente.asia.thankscard.services.rank.view.MonthYearPickerDialog;
import trente.asia.thankscard.services.received.ReceiveTCListFragment;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 2/15/2016.
 */
public abstract class AbstractTCFragment extends WelfareFragment{

	// public static UserModel loginUser = new UserModel();
	protected MonthYearPickerDialog	monthYearPickerDialog;

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
		buildBackBtn();
		buildTitle();
		buildSettingBtn();
		buildBodyLayout();
		buildFooter();
	}

	protected void buildBackBtn(){
		ImageView imgBack = (ImageView)getView().findViewById(R.id.img_view_common_header_back);
		if(hasBackBtn() == true){
			imgBack.setVisibility(View.VISIBLE);
			imgBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					onClickBackBtn();
				}
			});
		}
	}

	/**
	 * All subclass must declare its layout file
	 * 
	 * @return layout resource id
	 */
	public abstract int getFragmentLayoutId();

	/**
	 * @return do we show back button at the header ?
	 */
	public abstract boolean hasBackBtn();

	/**
	 * @return do we show setting button at the header ?
	 */
	public abstract boolean hasSettingBtn();

	/**
	 * return 0 if we don't want to show footer
	 * 
	 * @return view id of the footer item. E.g. lnr_view_common_footer_mypage
	 */
	public abstract int getFooterItemId();

	/**
	 * @return string resource for fragment title
	 */
	public abstract int getTitle();

	public abstract void buildBodyLayout();

	// //////////////////////////////////* Fragment Header and Footer *///////////////////////
	public void buildTitle(){
		if(!CCStringUtil.isEmpty(getString(getTitle()))){
			TextView txtTitle = (TextView)getView().findViewById(R.id.txt_view_common_header_title);
			if(txtTitle != null) txtTitle.setText(getString(getTitle()));
		}
	}

	protected void buildSettingBtn(){
		if(hasSettingBtn() == true){
			ImageView imgSetting = (ImageView)getView().findViewById(R.id.img_view_common_header_setting);
			imgSetting.setVisibility(View.VISIBLE);
			imgSetting.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					TCSettingFragment tcSettingFragment = new TCSettingFragment();
					Bundle args = new Bundle();
					args.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, getFooterItemId());
					tcSettingFragment.setArguments(args);
					gotoFragment(tcSettingFragment);
				}
			});
		}
	}

	protected void buildFooter(){
		int footerItemId = getFooterItemId();
		if(footerItemId != 0){
			View.OnClickListener listener = new View.OnClickListener() {

				@Override
				public void onClick(View view){
					switch(view.getId()){
					case R.id.lnr_view_common_footer_mypage:
						onClickFooterItemMyPage();
						break;
					case R.id.lnr_view_common_footer_posted:
						onClickFooterItemPosted();
						break;
					case R.id.lnr_view_common_footer_receive:
						onClickFooterReceive();
						break;
					case R.id.lnr_view_common_footer_ranking:
						onClickFooterRanking();
						break;
					default:
						break;
					}
				}
			};
			getView().findViewById(R.id.lnr_view_common_footer_mypage).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_posted).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_receive).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_common_footer_ranking).setOnClickListener(listener);
			setSelectedFooterItem(footerItemId);
			TextView txtFooterItem = (TextView)((LinearLayout)getView().findViewById(footerItemId)).getChildAt(1);
			// txtFooterItem.setcom
			txtFooterItem.setTextColor(getResources().getColor(R.color.chiase_white));
		}
	}

	private void setSelectedFooterItem(int footerItemId){
		ImageView imgFooterItem = (ImageView)((LinearLayout)getView().findViewById(footerItemId)).getChildAt(0);
		switch(footerItemId){
		case R.id.lnr_view_common_footer_mypage:
			imgFooterItem.setImageResource(R.drawable.tc_icon_mypage_white);
			break;
		case R.id.lnr_view_common_footer_posted:
			imgFooterItem.setImageResource(R.drawable.tc_icon_post_white);
			break;
		case R.id.lnr_view_common_footer_receive:
			imgFooterItem.setImageResource(R.drawable.tc_icon_receive_white);
			break;
		case R.id.lnr_view_common_footer_ranking:
			imgFooterItem.setImageResource(R.drawable.tc_icon_ranking_white);
			break;
		default:
			break;
		}
	}

	public void onClickFooterItemMyPage(){
		emptyBackStack();
		gotoFragment(new MypageFragment());
	}

	public void onClickFooterItemPosted(){
		emptyBackStack();
		gotoFragment(new PostTCListFragment());
	}

	public void onClickFooterReceive(){
		emptyBackStack();
		gotoFragment(new ReceiveTCListFragment());
	}

	public void onClickFooterRanking(){
		emptyBackStack();
		gotoFragment(new RankingFragment());
	}

	// //////////////////////////////////* End Fragment Header and Footer *///////////////////////

	// //////////////////////////////// ////////////////////////////////////////////////////
	protected void showAlertDialog(String title, String message, String buttonStr, DialogInterface.OnClickListener listener){
		new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog)).setTitle(title).setMessage(message).setPositiveButton(buttonStr, listener).setIcon(android.R.drawable.ic_dialog_info).show();
	}

	protected void showAlertDialogWithOption(String title, String message, String positiveStr, String negativeStr, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener){
		new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Dialog)).setTitle(title).setMessage(message).setPositiveButton(positiveStr, positiveListener).setNegativeButton(negativeStr, negativeListener).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	protected void showMonthPickerDialog(DatePickerDialog.OnDateSetListener listener, int year, int month){
		showLimitedMonthPickerDialog(listener, year, month, 0, 0);
	}

	protected void showLimitedMonthPickerDialog(DatePickerDialog.OnDateSetListener listener, int year, int month, int maxYear, int maxMonth){
		if(monthYearPickerDialog == null){
			monthYearPickerDialog = new MonthYearPickerDialog();
		}
		if(maxYear != 0){
			monthYearPickerDialog.setMaxYear(maxYear);
		}
		if(maxMonth != 0){
			monthYearPickerDialog.setMaxMonth(maxMonth);
		}
		monthYearPickerDialog.setmSelectedTime(year, month);
		monthYearPickerDialog.setListener(listener);
		monthYearPickerDialog.show(getChildFragmentManager(), "MonthYearPickerDialog");
	}

	protected void gotoBrowser(String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_TC;
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new TcLogInFragment());
	}

}
