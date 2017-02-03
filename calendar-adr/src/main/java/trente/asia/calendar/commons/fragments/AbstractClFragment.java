package trente.asia.calendar.commons.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.android.view.ChiaseImageView;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.user.ClLoginFragment;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractClFragment extends WelfareFragment implements View.OnClickListener{

	// test
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		buildFooter();
	}

	@Override
	protected void initView(){
		super.initView();

		// int footerItemId = getFooterItemId();
		// if(footerItemId != 0){
		// TextView txtUnread = (TextView)activity.findViewById(R.id.txt_id_unread_message);
		// String uncheckedCount = ((MainActivity)activity).uncheckedCount;
		// if(!CCStringUtil.isEmpty(uncheckedCount) && !CCConst.NONE.equals(uncheckedCount)){
		// txtUnread.setText(uncheckedCount);
		// txtUnread.setVisibility(View.VISIBLE);
		// }else{
		// txtUnread.setVisibility(View.GONE);
		// }
		// }
	}

	/**
	 * return 0 if we don't want to show footer
	 *
	 * @return view id of the footer item. E.g. lnr_view_common_footer_mypage
	 */
	public abstract int getFooterItemId();

	protected void buildFooter(){
		final int footerItemId = getFooterItemId();
		// if(footerItemId != 0){
		// View.OnClickListener listener = new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View view){
		// switch(view.getId()){
		// case R.id.lnr_view_footer_work_time:
		// onClickFooterItemWorkTime();
		// break;
		// case R.id.lnr_view_footer_shift_working:
		// onClickFooterItemShiftWorking();
		// break;
		// case R.id.lnr_view_footer_offer:
		// onClickFooterItemOffer();
		// break;
		// case R.id.lnr_view_footer_work_status:
		// onClickFooterItemWorkStatus();
		// break;
		// case R.id.lnr_view_footer_setting:
		// onClickFooterItemSetting();
		// break;
		// default:
		// break;
		// }
		// }
		// }; // end listener

		// getView().findViewById(R.id.lnr_view_footer_work_time).setOnClickListener(listener);
		// getView().findViewById(R.id.lnr_view_footer_shift_working).setOnClickListener(listener);
		// getView().findViewById(R.id.lnr_view_footer_offer).setOnClickListener(listener);
		// getView().findViewById(R.id.lnr_view_footer_work_status).setOnClickListener(listener);
		// getView().findViewById(R.id.lnr_view_footer_setting).setOnClickListener(listener);
		// LinearLayout lnrFooter = (LinearLayout)getView().findViewById(footerItemId);
		// setSelectedFooterItem(lnrFooter);
//	}

	}

	private void setSelectedFooterItem(ViewGroup footer){
		for(int i = 0; i < footer.getChildCount(); i++){
			View view = footer.getChildAt(i);
			if(view instanceof ChiaseImageView){
				view.setAlpha((float)1);
			}else if(view instanceof ChiaseTextView){
				((ChiaseTextView)view).setTextColor(Color.WHITE);
			}else if(view instanceof ViewGroup){
				setSelectedFooterItem((ViewGroup)view);
			}
		}
	}

	// public void onClickFooterItemWorkTime(){
	// emptyBackStack();
	// gotoFragment(new WorktimeCheckInFragment());
	// }
	//
	// public void onClickFooterItemShiftWorking(){
	// emptyBackStack();
	// gotoFragment(new ShiftWorkingFragment());
	// }
	//
	// public void onClickFooterItemOffer(){
	// emptyBackStack();
	// gotoFragment(new WorkOfferListFragment());
	// }
	//
	// public void onClickFooterItemWorkStatus(){
	// emptyBackStack();
	// gotoFragment(new WorkStatusFragment());
	// }
	//
	// public void onClickFooterItemSetting(){
	// emptyBackStack();
	// gotoFragment(new SwSettingFragment());
	// }

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_SW;
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new ClLoginFragment());
	}

	protected String makeAppFile(String fileName){
		String filePath = ClUtil.getFilesFolderPath() + "/" + fileName;
		return filePath;
	}
}
