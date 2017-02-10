package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.welfare.adr.activity.WelfareFragment;

/**
 * MonthlyPageFragment
 *
 * @author TrungND
 */
public class MonthlyPageFragment extends WelfareFragment{

	private Date							activeMonth;

	private LinearLayout					lnrMonthlyPage;
	private List<ScheduleModel>				lstSchedule		= new ArrayList<>();
	private List<MonthlyCalendarDayView>	lstCalendarDay	= new ArrayList<>();

	public void setActiveMonth(Date activeMonth){
		this.activeMonth = activeMonth;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lnrMonthlyPage = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_page);

		// add calendar title
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		for(DayModel dayModel : CsDateUtil.getAllDay4Week(Calendar.THURSDAY)){
			View titleItem = mInflater.inflate(R.layout.monthly_calendar_title_item, null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			titleItem.setLayoutParams(layoutParams);
			TextView txtTitleItem = (TextView)titleItem.findViewById(R.id.txt_id_row_content);
			if(Calendar.SUNDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.RED);
			}else if(Calendar.SATURDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.BLUE);
			}
			txtTitleItem.setText(dayModel.day);
			lnrRowTitle.addView(titleItem);
		}
		lnrMonthlyPage.addView(titleView);

		List<Date> lstDate = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(activeMonth));
		View rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < lstDate.size(); index++){
			Date itemDate = lstDate.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrMonthlyPage.addView(rowView);
			}

			MonthlyCalendarDayView dayView = new MonthlyCalendarDayView(activity);
			dayView.initialization(itemDate);
			lstCalendarDay.add(dayView);

			lnrRowContent.addView(dayView);
		}
	}

	@Override
	protected void initData(){
		makeDummyData();
	}

	private void makeDummyData(){
		ScheduleModel scheduleModel1 = new ScheduleModel("Leader meeting", "2017/02/08", ClConst.SCHEDULE_COLOR_BLUE);
		ScheduleModel scheduleModel2 = new ScheduleModel("Developer meeting", "2017/02/16", ClConst.SCHEDULE_COLOR_RED);
        ScheduleModel scheduleModel3 = new ScheduleModel("Learning meeting", "2017/02/07", ClConst.SCHEDULE_COLOR_RED);

		lstSchedule.add(scheduleModel1);
		lstSchedule.add(scheduleModel2);
        lstSchedule.add(scheduleModel3);

		for(ScheduleModel model : lstSchedule){
			MonthlyCalendarDayView activeView = ClUtil.findView4Day(lstCalendarDay, model.scheduleDate);
			if(activeView != null){
                activeView.addSchedule(model);
			}
		}
	}

}
