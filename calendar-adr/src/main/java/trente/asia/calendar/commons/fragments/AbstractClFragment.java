package nguyenhoangviet.vpcorp.calendar.commons.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import nguyenhoangviet.vpcorp.android.view.ChiaseImageView;
import nguyenhoangviet.vpcorp.android.view.ChiaseTextView;
import nguyenhoangviet.vpcorp.calendar.BuildConfig;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.utils.ClUtil;
import nguyenhoangviet.vpcorp.calendar.services.calendar.DailyFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.MonthlyFragment;
import nguyenhoangviet.vpcorp.calendar.services.calendar.WeeklyFragment;
import nguyenhoangviet.vpcorp.calendar.services.setting.ClSettingFragment;
import nguyenhoangviet.vpcorp.calendar.services.todo.TodoListFragment;
import nguyenhoangviet.vpcorp.calendar.services.user.ClLoginFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

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
						benchmark("onClicked");
						onClickFooterItemMonthly();
						break;
					case R.id.lnr_view_footer_weekly:
						onClickFooterItemWeekly();
						break;
					case R.id.lnr_view_footer_daily:
						onClickFooterItemDaily();
						break;
					case R.id.lnr_view_footer_todo:
						onClickFooterItemTodo();
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
			getView().findViewById(R.id.lnr_view_footer_todo).setOnClickListener(listener);
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

	public void onClickFooterItemTodo(){
		emptyBackStack();
		gotoFragment(new TodoListFragment());
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
