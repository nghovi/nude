package nguyenhoangviet.vpcorp.calendar.commons.fragments;

import java.util.Date;

import android.os.Bundle;

import nguyenhoangviet.vpcorp.android.activity.ChiaseFragment;
import nguyenhoangviet.vpcorp.calendar.BuildConfig;
import nguyenhoangviet.vpcorp.calendar.commons.dialogs.DailySummaryDialog;
import nguyenhoangviet.vpcorp.calendar.commons.views.PageSharingHolder;
import nguyenhoangviet.vpcorp.calendar.services.calendar.ScheduleFormFragment;

/**
 * ClPageFragment
 *
 * @author VietNH
 */
public abstract class ClPageFragment extends AbstractClFragment implements DailySummaryDialog.OnAddBtnClickedListener{

	protected Date				selectedDate;
	protected int				pagePosition;
	protected PageSharingHolder	pageSharingHolder;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();
	}

	@Override
	protected void initData(){
		if(pageSharingHolder != null){
			loadData();
		}
	}

	protected abstract void loadData();

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}

	public void setPageSharingHolder(PageSharingHolder pageSharingHolder){
		this.pageSharingHolder = pageSharingHolder;
	}

	public void setPagePosition(int pagePosition){
		this.pagePosition = pagePosition;
	}

	@Override
	public void onAddBtnClick(Date date){
		gotoScheduleFormFragment(date);
	}

	private void gotoScheduleFormFragment(Date date){
		ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
		if(date == null){
			date = pageSharingHolder.getClickedDate();
		}
		scheduleFormFragment.setSelectedDate(date);
		ChiaseFragment parentFragment = (ChiaseFragment)getParentFragment();
		parentFragment.gotoFragment(scheduleFormFragment);
	}

	public boolean isActivePage(){
		return pageSharingHolder.selectedPagePosition == pagePosition;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}
}
