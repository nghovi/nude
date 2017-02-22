package trente.asia.calendar.commons.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import trente.asia.android.view.ChiaseImageView;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.DailyFragment;
import trente.asia.calendar.services.calendar.MonthlyFragment;
import trente.asia.calendar.services.calendar.WeeklyFragment;
import trente.asia.calendar.services.setting.ClSettingFragment;
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

        lnrContentId = R.id.lnr_id_content;

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
		if(footerItemId != 0){
			View.OnClickListener listener = new View.OnClickListener() {

				@Override
				public void onClick(View view){
					switch(view.getId()){
					case R.id.lnr_view_footer_monthly:
						onClickFooterItemMonthly();
						break;
					case R.id.lnr_view_footer_weekly:
						onClickFooterItemWeekly();
						break;
					case R.id.lnr_view_footer_daily:
						onClickFooterItemDaily();
						break;
					case R.id.lnr_view_footer_summary:
						onClickFooterItemSummary();
						break;
					case R.id.lnr_view_footer_setting:
						onClickFooterItemSetting();
						break;
					default:
						break;
					}
				}
			}; // end listener

			getView().findViewById(R.id.lnr_view_footer_monthly).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_footer_weekly).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_footer_daily).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_footer_summary).setOnClickListener(listener);
			getView().findViewById(R.id.lnr_view_footer_setting).setOnClickListener(listener);
			LinearLayout lnrFooter = (LinearLayout)getView().findViewById(footerItemId);
			setSelectedFooterItem(lnrFooter);
		}

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

	public void onClickFooterItemMonthly(){
		emptyBackStack();
		gotoFragment(new MonthlyFragment());
	}

	public void onClickFooterItemWeekly(){
		emptyBackStack();
		gotoFragment(new WeeklyFragment());
	}

	public void onClickFooterItemDaily(){
		emptyBackStack();
		gotoFragment(new DailyFragment());
	}

	public void onClickFooterItemSummary(){
		emptyBackStack();
		gotoFragment(new MonthlyFragment());
	}

	public void onClickFooterItemSetting(){
		emptyBackStack();
		gotoFragment(new ClSettingFragment());
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_CL;
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

    /**
     * emptyLocalData
     * remove local data when user sign out
     */
    protected void emptyLocalData(){
    }
}
