package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public abstract class SchedulesPageListViewFragment extends SchedulesPageFragment implements CalendarDayView.OnDayClickListener{

	protected List<WeeklyCalendarHeaderRowView>	lstHeaderRow		= new ArrayList<>();
	protected List<CalendarDayModel>			calendarDayModels;

	protected List<CalendarDayView>				calendarDayViews	= new ArrayList<>();

	@Override
	protected int getNormalDayColor(){
		return Color.WHITE;
	}

	@Override
	protected void initDayViews(){
		dates = getAllDate();
		WeeklyCalendarHeaderRowView rowView = null;
		for(int index = 0; index < dates.size(); index++){
			Date date = dates.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = new WeeklyCalendarHeaderRowView(activity, index / CsConst.DAY_NUMBER_A_WEEK);
				rowView.initialization();
				lnrCalendarContainer.addView(rowView);
				lstHeaderRow.add(rowView);
			}

			CalendarDayView dayView = getCalendayDayView();
			boolean isInOtherMonth = CsDateUtil.isDiffMonth(date, selectedDate);
			dayView.initLayout(date, isInOtherMonth);
			rowView.lnrRowContent.addView(dayView);
			calendarDayViews.add(dayView);
		}
	}

	protected CalendarDayView getCalendayDayView(){
		return new CalendarDayView(activity);
	}

	abstract public void updateList(String dayStr);

	protected void onLoadSchedulesSuccess(JSONObject response){
		super.onLoadSchedulesSuccess(response);
		calendarDayModels = buildCalendarDayModels(lstSchedule);
		clearOldData();
		updateObservableScrollableView();
	}

	@Override
	protected void clearOldData(){
		for(CalendarDayView dayView : calendarDayViews){
			CalendarDayModel calendarDayModel = getCalendarDayModel(dayView.dayStr, calendarDayModels);
			dayView.setData(calendarDayModel, this, lstHoliday);
		}
	}

	abstract protected void updateObservableScrollableView();

	public List<CalendarDayModel> buildCalendarDayModels(List<ScheduleModel> schedules){
		List<CalendarDayModel> calendarDayModels = new ArrayList<>();
		for(ScheduleModel scheduleModel : schedules){
			CalendarDayModel calendarDayModel = getCalendarDayModel(scheduleModel.startDate, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = scheduleModel.startDate;
				calendarDayModel.schedules = new ArrayList<>();
				calendarDayModel.schedules.add(scheduleModel);
				calendarDayModels.add(calendarDayModel);
			}else{
				calendarDayModel.schedules.add(scheduleModel);
			}
		}

		for(HolidayModel holidayModel : lstHoliday){
			CalendarDayModel calendarDayModel = getCalendarDayModel(holidayModel.startDate, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = holidayModel.startDate;
				calendarDayModel.schedules = new ArrayList<>();
				calendarDayModel.holidayModels = new ArrayList<>();
				calendarDayModel.holidayModels.add(holidayModel);
				calendarDayModels.add(calendarDayModel);
			}else{
				calendarDayModel.holidayModels.add(holidayModel);
			}
		}

		Comparator<CalendarDayModel> comparator = new Comparator<CalendarDayModel>() {

			@Override
			public int compare(CalendarDayModel left, CalendarDayModel right){
				return CCDateUtil.makeDateCustom(left.date, WelfareConst.WL_DATE_TIME_7).compareTo(CCDateUtil.makeDateCustom(right.date, WelfareConst.WL_DATE_TIME_7));
			}
		};

		Collections.sort(calendarDayModels, comparator);
		return calendarDayModels;
	}

	public CalendarDayModel getCalendarDayModel(String day, List<CalendarDayModel> calendarDayModels){
		if(!CCCollectionUtil.isEmpty(calendarDayModels)){
			for(CalendarDayModel calendarDayModel : calendarDayModels){
				if(calendarDayModel.date.equals(day)){
					return calendarDayModel;
				}
			}
		}

		return null;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	protected List<CalendarDayModel> getDisplayedDayForList(){
		return calendarDayModels;
	}

	@Override
	public void onDayClick(String dayStr){
		updateDayViews(dayStr);
		updateList(dayStr);
	}

	protected void updateDayViews(String dayStr){
		pageSharingHolder.cancelPreviousClickedDayView();
		for(CalendarDayView view : calendarDayViews){
			if(dayStr.equals(view.dayStr)){
				view.setSelected(true);
				pageSharingHolder.setClickedDayView(view);
				return;
			}
		}
	}
}
