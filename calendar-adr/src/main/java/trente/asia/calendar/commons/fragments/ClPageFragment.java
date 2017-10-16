package trente.asia.calendar.commons.fragments;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.DailySummaryDialog;
import trente.asia.calendar.commons.views.PageSharingHolder;
import trente.asia.calendar.services.calendar.ScheduleFormFragment;

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
		// String selectedCalendarString = prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING);
		// if(!CCStringUtil.isEmpty(selectedCalendarString)){
		gotoScheduleFormFragment(date);
		// }else{
		// alertDialog.setMessage(getString(R.string.cl_common_validate_no_calendar_msg));
		// alertDialog.show();
		// }
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

	@Override
	public int getFooterItemId(){
		return 0;
	}
}
