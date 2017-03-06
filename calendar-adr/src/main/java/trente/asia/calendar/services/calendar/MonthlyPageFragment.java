package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.define.CsConst;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClDailySummaryDialog;
import trente.asia.calendar.commons.utils.ClRepeatUtil;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarRowView;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * MonthlyPageFragment
 *
 * @author TrungND
 */
public class MonthlyPageFragment extends SchedulesPageFragment implements DailyScheduleClickListener{

	private List<MonthlyCalendarDayView>	lstCalendarDay				= new ArrayList<>();
	private List<MonthlyCalendarRowView>	lstCalendarRow				= new ArrayList<>();

	private ClDailySummaryDialog			dialogDailySummary;
	private List<ScheduleModel>				lstScheduleWithoutHoliday	= new ArrayList<>();

	public class ScheduleComparator implements Comparator<ScheduleModel>{

		@Override
		public int compare(ScheduleModel schedule1, ScheduleModel schedule2){
			Date startDate1 = WelfareUtil.makeDate(schedule1.startDate);
			Date endDate1 = WelfareUtil.makeDate(schedule1.endDate);

			Date startDate2 = WelfareUtil.makeDate(schedule2.startDate);
			Date endDate2 = WelfareUtil.makeDate(schedule2.endDate);

			boolean diff1 = WelfareFormatUtil.formatDate(startDate1).equals(WelfareFormatUtil.formatDate(endDate1));
			boolean diff2 = WelfareFormatUtil.formatDate(startDate2).equals(WelfareFormatUtil.formatDate(endDate2));

			if(!diff1 && diff2) return -1;
			if(diff1 && !diff2) return 1;
			return 0;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly_page, container, false);
		}else{
			if(CCBooleanUtil.checkBoolean(((WelfareActivity)activity).dataMap.get(ClConst.IS_UPDATE_SCHEDULE))){
				((WelfareActivity)activity).dataMap.clear();
				((ChiaseActivity)activity).isInitData = true;
			}
		}
		return mRootView;
	}

	protected List<Date> getAllDate(){
		int firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		return CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(selectedDate), firstDay);
	}

	@Override
	protected void initDayViews(){
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MonthlyCalendarRowView rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < dates.size(); index++){
			Date itemDate = dates.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = (MonthlyCalendarRowView)mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrCalendarContainer.addView(rowView);
				lstCalendarRow.add(rowView);
			}

			MonthlyCalendarDayView dayView = (MonthlyCalendarDayView)mInflater.inflate(R.layout.monthly_calendar_row_item, null);
			dayView.initialization(itemDate, this, CsDateUtil.isDiffMonth(itemDate, selectedDate));
			dayView.dayOfTheWeek = index % CsConst.DAY_NUMBER_A_WEEK;
			lstCalendarDay.add(dayView);
			rowView.lstCalendarDay.add(dayView);

			lnrRowContent.addView(dayView);
		}
	}

	private void initDialog(){
		dialogDailySummary = new ClDailySummaryDialog(activity, getLayoutInflater(null), this);
		ImageView imgAdd = (ImageView)dialogDailySummary.findViewById(R.id.img_id_add);
		imgAdd.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		String activeMonth = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, selectedDate);
		Date activeDate = WelfareFormatUtil.makeDate(prefAccUtil.get(ClConst.PREF_ACTIVE_DATE));
		if(activeMonth.equals(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, activeDate))){
			loadScheduleList();
		}
	}

	@Override
	protected void onLoadSchedulesSuccess(JSONObject response){
		super.onLoadSchedulesSuccess(response);
		clearOldData();

		if(lstSchedule == null){
			lstSchedule = new ArrayList<>();
		}
		lstScheduleWithoutHoliday.addAll(lstSchedule);

		// add holiday
		if(!CCCollectionUtil.isEmpty(lstHoliday)){
			for(HolidayModel holidayModel : lstHoliday){
				ScheduleModel scheduleModel = new ScheduleModel(holidayModel);
				lstSchedule.add(scheduleModel);
			}
		}

		if(!CCCollectionUtil.isEmpty(lstSchedule)){
			Collections.sort(lstSchedule, new ScheduleComparator());
			for(ScheduleModel model : lstSchedule){
				if(model.isPeriodSchedule()){
					for(MonthlyCalendarRowView rowView : lstCalendarRow){
						String minDay = rowView.lstCalendarDay.get(0).day;
						String maxDay = rowView.lstCalendarDay.get(rowView.lstCalendarDay.size() - 1).day;
						if(ClUtil.belongPeriod(WelfareUtil.makeDate(model.startDate), minDay, maxDay) || ClUtil.belongPeriod(WelfareUtil.makeDate(model.endDate), minDay, maxDay)){
							rowView.addSchedule(model, lstCategories);
						}
					}
				}else{
					List<MonthlyCalendarDayView> lstActiveCalendarDay = null;
					if(ClRepeatUtil.isRepeat(model.repeatType)){
						lstActiveCalendarDay = ClRepeatUtil.findView4RepeatSchedule(lstCalendarDay, model);
					}else{
						lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, model.startDate, model.endDate);
					}

					if(!CCCollectionUtil.isEmpty(lstActiveCalendarDay)){
						for(MonthlyCalendarDayView calendarDayView : lstActiveCalendarDay){
							calendarDayView.addSchedule(model, lstCategories);
						}
					}
				}
			}
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.refreshLayout();
		}

		// make daily summary dialog
		initDialog();
	}

	@Override
	protected void clearOldData(){
		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			dayView.removeAllData();
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.removeAllData();
		}
		lstScheduleWithoutHoliday.clear();
	}

	@Override
	public void onDailyScheduleClickListener(String day){
		Date selectedDate = CCDateUtil.makeDateCustom(day, WelfareConst.WL_DATE_TIME_7);
		dialogDailySummary.show(selectedDate, lstSchedule);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_add:
			dialogDailySummary.dismiss();
			((WelfareActivity)activity).addFragment(new ScheduleFormFragment());
			break;
		default:
			break;
		}
	}
}
