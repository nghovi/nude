package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.LinearLayout;

import asia.chiase.core.util.CCBooleanUtil;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.DailySummaryDialog;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarRowView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfDateUtil;

/**
 * MonthlyPageFragment
 *
 * @author TrungND
 */
public class MonthlyPageFragment extends SchedulesPageFragment implements DailyScheduleClickListener{

	private List<MonthlyCalendarDayView>	lstCalendarDay	= new ArrayList<>();
	private List<MonthlyCalendarRowView>	lstCalendarRow	= new ArrayList<>();

	private DailySummaryDialog				dialogDailySummary;

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

			boolean isAll1 = CCBooleanUtil.checkBoolean(schedule1.isAllDay);
			boolean isAll2 = CCBooleanUtil.checkBoolean(schedule2.isAllDay);

			if(isAll1 && !isAll2) return -1;
			if(!isAll1 && isAll2) return 1;
			return 0;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly_page, container, false);
		}else{

		}
		return mRootView;
	}

	protected List<Date> getAllDate(){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
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

	private void makeSummaryDialog(){
		dialogDailySummary = new DailySummaryDialog(activity, this, this, dates);
		dialogDailySummary.setData(lstSchedule, lstBirthdayUser, lstHoliday, lstWorkOffer);
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

		// add holiday,
		if(!CCCollectionUtil.isEmpty(lstHoliday)){
			for(HolidayModel holidayModel : lstHoliday){
				ScheduleModel scheduleModel = new ScheduleModel(holidayModel);
				scheduleModel.scheduleName = getString(R.string.cl_schedule_holiday_name, scheduleModel.scheduleName);
				lstSchedule.add(scheduleModel);
			}
		}

		// add birthday
		if(!CCCollectionUtil.isEmpty(lstBirthdayUser)){
			for(UserModel birthday : lstBirthdayUser){
				ScheduleModel scheduleModel = new ScheduleModel(birthday);
				scheduleModel.scheduleName = getString(R.string.cl_schedule_birth_day_name, scheduleModel.scheduleName);
				lstSchedule.add(scheduleModel);
			}
		}

		// add work offer
		if(!CCCollectionUtil.isEmpty(lstWorkOffer)){
			for(WorkOffer workOffer : lstWorkOffer){
				ScheduleModel scheduleModel = new ScheduleModel(workOffer);
				scheduleModel.scheduleName = getString(R.string.cl_schedule_offer_name, scheduleModel.scheduleName);
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
						Date startDate = WelfareUtil.makeDate(model.startDate);
						Date endDate = WelfareUtil.makeDate(model.endDate);
						boolean isStartBelongPeriod = ClUtil.belongPeriod(startDate, minDay, maxDay);
						boolean isEndBelongPeriod = ClUtil.belongPeriod(endDate, minDay, maxDay);
						boolean isOverPeriod = WfDateUtil.compareDate(WelfareFormatUtil.formatDate(startDate), minDay) < 0 && WfDateUtil.compareDate(WelfareFormatUtil.formatDate(endDate), maxDay) > 0;

						if(isStartBelongPeriod || isEndBelongPeriod || isOverPeriod){
							rowView.addSchedule(model, lstCategory);
						}
					}
				}else{
					List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, model.startDate, model.endDate);
					// if(ClRepeatUtil.isRepeat(model.repeatType)){
					// lstActiveCalendarDay = ClRepeatUtil.findView4RepeatSchedule(lstCalendarDay, model);
					// }else{
					// lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, model.startDate, model.endDate);
					// }

					if(!CCCollectionUtil.isEmpty(lstActiveCalendarDay)){
						for(MonthlyCalendarDayView calendarDayView : lstActiveCalendarDay){
							calendarDayView.addSchedule(model, lstCategory);
						}
					}
				}
			}
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.refreshLayout();
		}

		// make daily summary dialog
		makeSummaryDialog();
	}

	@Override
	protected void clearOldData(){
		for(MonthlyCalendarDayView dayView : lstCalendarDay){
			dayView.removeAllData();
		}

		for(MonthlyCalendarRowView rowView : lstCalendarRow){
			rowView.removeAllData();
		}
		// lstScheduleWithoutHoliday.clear();
	}

	@Override
	public void onDailyScheduleClickListener(String day){
		Date selectedDate = CCDateUtil.makeDateCustom(day, WelfareConst.WL_DATE_TIME_7);
		dialogDailySummary.show(selectedDate);
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		default:
			break;
		}
	}
}
