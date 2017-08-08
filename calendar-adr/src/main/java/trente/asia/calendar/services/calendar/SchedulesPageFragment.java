package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.DailySummaryDialog;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserFacilityView;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * SchedulesPageFragment
 *
 * @author TrungND
 */
public abstract class SchedulesPageFragment extends ClPageFragment implements WeeklyScheduleListAdapter.OnScheduleItemClickListener,DailyScheduleClickListener{

	protected List<Date>			dates;

	protected LinearLayout			lnrCalendarContainer;
	protected List<ScheduleModel>	lstSchedule			= new ArrayList<>();
	protected List<HolidayModel>	lstHoliday;

	protected List<CalendarModel>	lstCalendar;
	protected List<UserModel>		lstCalendarUser;
	protected List<CategoryModel>	lstCategory;
	protected List<UserModel>		lstBirthdayUser;
	protected List<WorkOffer>		lstWorkOffer;
	protected boolean				refreshDialogData	= false;
	protected String				dayStr;
	private String					scheduleStrings;
	protected boolean				isChangedData		= true;
	protected List<Todo>			todos;
	protected LayoutInflater		inflater;
	protected DailySummaryDialog	dialogDailySummary;

	abstract protected List<Date> getAllDate();

	protected void clearOldData(){
	};

	@Override
	protected void initView(){
		super.initView();

		UserFacilityView userFacilityView = (UserFacilityView)getView().findViewById(R.id.user_facility_view);
		userFacilityView.initChildren(new UserFacilityView.OnTabClickListener() {

			@Override
			public void onBtnUserClicked(){
				gotoUserFilterFragment();
			}

			@Override
			public void onBtnFacilityClicked(){
				gotoRoomFilterFragment();
			}
		});

		if(pageSharingHolder.isLoadingSchedules == false){
			lnrCalendarContainer = (LinearLayout)getView().findViewById(R.id.lnr_calendar_container);
			dates = getAllDate();
			initCalendarView();
		}

		inflater = LayoutInflater.from(activity);
	}

	protected void gotoRoomFilterFragment(){
		RoomFilterFragment roomFilterFragment = new RoomFilterFragment();
		((AbstractClFragment)getParentFragment()).gotoFragment(roomFilterFragment);
	}

	protected void gotoUserFilterFragment(){
		UserFilterFragment userFilterFragment = new UserFilterFragment();
		((AbstractClFragment)getParentFragment()).gotoFragment(userFilterFragment);
	}

	protected void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	abstract protected void initCalendarHeader();

	abstract protected void initDayViews();

	public void loadScheduleList(){
		// if(pageSharingHolder.selectedPagePosition != pagePosition || (pageSharingHolder.selectedPagePosition ={
		// pageSharingHolder.isLoadingSchedules = true;
		JSONObject jsonObject = prepareJsonObject();
		requestLoad(ClConst.API_SCHEDULE_LIST, jsonObject, false);
		// }
	}

	protected JSONObject prepareJsonObject(){
		return buildJsonObjForScheduleListRequest(prefAccUtil, dates.get(0), dates.get(dates.size() - 1));
	}

	public static JSONObject buildJsonObjForScheduleListRequest(PreferencesAccountUtil prefAccUtil, Date startDate, Date endDate){
		String targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));

			jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, startDate));
			jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, endDate));

		}catch(JSONException e){
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(ClConst.API_SCHEDULE_LIST.equals(url)){
			onLoadSchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	protected void commonNotSuccess(JSONObject response){
		pageSharingHolder.isLoadingSchedules = false;
		super.commonNotSuccess(response);
	}

	public static List<Date> getAllDateForMonth(PreferencesAccountUtil prefAccUtil, Date selectedDate){
		int firstDay = Calendar.SUNDAY;
		if(!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)){

			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);

		}
		Date firstDateOfMonth = CCDateUtil.makeDateWithFirstday(selectedDate);
		List<Date> dates = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(firstDateOfMonth), firstDay);
		return dates;
	}

	protected void onLoadSchedulesSuccess(JSONObject response){
		try{
			String newScheduleStrings = response.optString("schedules");
			lstSchedule = LoganSquare.parseList(newScheduleStrings, ScheduleModel.class);
			lstCalendar = LoganSquare.parseList(response.optString("calendars"), CalendarModel.class);
			lstHoliday = LoganSquare.parseList(response.optString("holidayList"), HolidayModel.class);
			lstCategory = LoganSquare.parseList(response.optString("categories"), CategoryModel.class);
			lstWorkOffer = LoganSquare.parseList(response.optString("workOfferList"), WorkOffer.class);
			lstBirthdayUser = LoganSquare.parseList(response.optString("birthdayList"), UserModel.class);
			lstCalendarUser = LoganSquare.parseList(response.optString("calendarUsers"), UserModel.class);
			todos = LoganSquare.parseList(response.optString("todos"), Todo.class);
			if(refreshDialogData && !newScheduleStrings.equals(scheduleStrings)){
				isChangedData = true;
			}
			scheduleStrings = newScheduleStrings;
			pageSharingHolder.isLoadingSchedules = false;

			// add holiday,
			if(!CCCollectionUtil.isEmpty(lstHoliday)){
				for(HolidayModel holidayModel : lstHoliday){
					ScheduleModel scheduleModel = new ScheduleModel(holidayModel);
					// scheduleModel.scheduleName = getString(R.string.cl_schedule_holiday_name, scheduleModel.scheduleName);
					lstSchedule.add(scheduleModel);
				}
			}

			// add birthday
			if(!CCCollectionUtil.isEmpty(lstBirthdayUser)){
				for(UserModel birthday : lstBirthdayUser){
					ScheduleModel scheduleModel = new ScheduleModel(birthday);
					// scheduleModel.scheduleName = getString(R.string.cl_schedule_birth_day_name, scheduleModel.scheduleName);
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

			// make daily summary dialog
			if(dialogDailySummary == null){
				dialogDailySummary = new DailySummaryDialog(activity, this, this, dates);
				dialogDailySummary.setData(lstSchedule, lstBirthdayUser, lstHoliday, lstWorkOffer);
			}

			if(refreshDialogData && isChangedData){
				//// TODO: 4/27/2017 more check change data
				dialogDailySummary.setData(lstSchedule, lstBirthdayUser, lstHoliday, lstWorkOffer);
				isChangedData = false;
			}

		}catch(IOException e){
			e.printStackTrace();
		}
		updateSchedules(lstSchedule, lstCategory);
	}

	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		Map<String, CategoryModel> categoryMap = ClUtil.convertCategory2Map(categories);
		for(ScheduleModel schedule : schedules){
			schedule.categoryModel = categoryMap.get(schedule.categoryId);
		}
	}

	@Override
	public void onClickScheduleItem(ScheduleModel schedule, Date selectedDate){
		gotoScheduleDetail((WelfareActivity)activity, schedule, selectedDate);
	}

	public static void gotoScheduleDetail(WelfareActivity activity, ScheduleModel schedule, Date selectedDate){
		ScheduleDetailFragment fragment = new ScheduleDetailFragment();
		fragment.setSchedule(schedule);
		fragment.setSelectedDate(selectedDate);
		activity.addFragment(fragment);
	}

	@Override
	public void onDailyScheduleClickListener(String day){
		if(dialogDailySummary != null && !dialogDailySummary.isShowing()){
			dayStr = day;
			dialogDailySummary.show(CCDateUtil.makeDateCustom(dayStr, WelfareConst.WF_DATE_TIME_DATE));
			refreshDialogData = true;
			loadScheduleList();
		}
	}

	@Override
	protected void loadData(){
		loadScheduleList();
	}

	protected int getNormalDayColor(){
		return ContextCompat.getColor(activity, R.color.wf_common_color_text);
	}

	protected int getHeaderBgColor(){
		return ContextCompat.getColor(activity, R.color.wf_app_color_base);
	}

	protected int getCalendarHeaderItem(){
		return 0;
	};

}
