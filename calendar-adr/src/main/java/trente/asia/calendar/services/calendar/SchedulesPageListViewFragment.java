package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.CalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

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
	public int getCalendarHeaderItem() {
		return R.layout.weekly_calendar_title;
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

	@Override
	protected int getHeaderBgColor() {
		return ContextCompat.getColor(activity, R.color.wf_app_color_base);
	}

	protected CalendarDayView getCalendayDayView(){
		return new CalendarDayView(activity);
	}

	abstract public void updateList(String dayStr);

	@Override
	protected void onLoadSchedulesSuccess(JSONObject response){
		super.onLoadSchedulesSuccess(response);
		calendarDayModels = buildCalendarDayModels(lstSchedule);
		clearOldData();
		updateObservableScrollableView();
		updateHeaderTitles();
	}

	@Override
	protected void clearOldData(){
		for(CalendarDayView dayView : calendarDayViews){
			CalendarDayModel calendarDayModel = getCalendarDayModel(dayView.getDate(), calendarDayModels);
			dayView.setData(calendarDayModel, this, lstHoliday);
		}
	}

	abstract protected void updateObservableScrollableView();

	public static List<CalendarDayModel> buildCalendarDayModelsFromSchedules(List<ScheduleModel> schedules, List<Date> dates){
		List<CalendarDayModel> calendarDayModels = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();

		for(ScheduleModel scheduleModel : schedules){
			Date startDate = WelfareUtil.makeDate(scheduleModel.startDate);
			if(scheduleModel.isPeriodSchedule()){
				Date endDate = WelfareUtil.makeDate(scheduleModel.endDate);
				Date endDisplayed = dates.get(dates.size() - 1);
				Date limit = endDate.compareTo(endDisplayed) <= 0 ? endDate : endDisplayed;
				while(CCDateUtil.compareDate(startDate, limit, false) <= 0){
					addCalendarDayModel(startDate, scheduleModel, calendarDayModels);
					calendar.setTime(startDate);
					calendar.add(Calendar.DATE, 1);
					startDate = calendar.getTime();
				}
			}else{
				addCalendarDayModel(startDate, scheduleModel, calendarDayModels);
			}
		}
		return calendarDayModels;
	}

	// // TODO: 3/13/2017 Refactor this function!!!
	public List<CalendarDayModel> buildCalendarDayModels(List<ScheduleModel> schedules){

		// build from schedules
		List<CalendarDayModel> calendarDayModels = buildCalendarDayModelsFromSchedules(schedules, dates);

		for(HolidayModel holidayModel : lstHoliday){
			Date date = WelfareUtil.makeDate(holidayModel.startDate);
			CalendarDayModel calendarDayModel = getCalendarDayModel(date, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = date;
				calendarDayModel.holidayModels.add(holidayModel);
				calendarDayModels.add(calendarDayModel);
			}else{
				calendarDayModel.holidayModels.add(holidayModel);
			}
		}

		// birthdays
		Calendar currentTime = Calendar.getInstance();
		int selectedYear = CCDateUtil.makeCalendar(selectedDate).get(Calendar.YEAR);
		for(UserModel userModel : lstBirthdayUser){
			Date birthday = WelfareUtil.makeDate(userModel.dateBirth);
			currentTime.setTime(birthday);
			currentTime.set(Calendar.YEAR, selectedYear);
			Date date = currentTime.getTime();
			CalendarDayModel calendarDayModel = getCalendarDayModel(date, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = date;
				calendarDayModel.birthdayUsers.add(userModel);
				calendarDayModels.add(calendarDayModel);
			}else{
				calendarDayModel.birthdayUsers.add(userModel);
			}
		}

		// work offers
		for(WorkOffer offer : lstWorkOffer){
			Date date = WelfareUtil.makeDate(offer.startDate);
			CalendarDayModel calendarDayModel = getCalendarDayModel(date, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = date;
				calendarDayModel.workOffers.add(offer);
				calendarDayModels.add(calendarDayModel);
			}else{
				calendarDayModel.workOffers.add(offer);
			}
		}

		Comparator<CalendarDayModel> comparator = new Comparator<CalendarDayModel>() {

			@Override
			public int compare(CalendarDayModel left, CalendarDayModel right){
				return left.date.compareTo(right.date);
			}
		};

		Collections.sort(calendarDayModels, comparator);
		return calendarDayModels;
	}

	public static void addCalendarDayModel(Date date, ScheduleModel scheduleModel, List<CalendarDayModel> calendarDayModels){
		CalendarDayModel calendarDayModel = getCalendarDayModel(date, calendarDayModels);
		if(calendarDayModel == null){
			calendarDayModel = new CalendarDayModel();
			calendarDayModel.date = date;
			calendarDayModel.schedules = new ArrayList<>();
			calendarDayModel.holidayModels = new ArrayList<>();
			calendarDayModel.schedules.add(scheduleModel);
			calendarDayModels.add(calendarDayModel);
		}else{
			calendarDayModel.schedules.add(scheduleModel);
		}
	}

	public static CalendarDayModel getCalendarDayModel(Date date, List<CalendarDayModel> calendarDayModels){
		if(!CCCollectionUtil.isEmpty(calendarDayModels)){
			for(CalendarDayModel calendarDayModel : calendarDayModels){
				if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendarDayModel.date).equals(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date))){
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
	public void onDayClick(String dayString){
		dayStr = dayString;
		updateDayViews(dayString);
		updateList(dayString);
	}

	protected void updateDayViews(String dayString){
		pageSharingHolder.cancelPreviousClickedDayView();
		for(CalendarDayView view : calendarDayViews){
			if(view.dayStr.equals(dayString)){
				view.setSelected(true);
				pageSharingHolder.setClickedDayView(view);
				return;
			}
		}
	}
}
