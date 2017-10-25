package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.DailySummaryDialog;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CalendarBirthdayModel;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.RoomModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
import trente.asia.calendar.services.calendar.view.DailyScheduleList;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * SchedulesPageFragment
 *
 * @author VietNH
 */
public abstract class SchedulesPageFragment extends ClPageFragment implements DailyScheduleList.OnScheduleItemClickListener,DailyScheduleClickListener{

	protected List<Date>					dates;

	protected LinearLayout					lnrCalendarContainer;
	protected List<ScheduleModel>			lstSchedule			= new ArrayList<>();
	protected List<HolidayModel>			lstHoliday;

	protected List<CalendarModel>			lstCalendar;
	protected List<UserModel>				lstCalendarUser;
	protected List<CategoryModel>			lstCategory;
	protected List<CalendarBirthdayModel>	calendarBirthdayModels;
	protected List<WorkRequest>				lstWorkRequest;
	protected boolean						refreshDialogData	= false;
	private String							scheduleStrings;
	protected boolean						isChangedData		= true;
	protected List<Todo>					todos;
	protected LayoutInflater				inflater;
	protected DailySummaryDialog			dialogDailySummary;
	protected boolean						isExpanded			= false;
	protected ObservableScrollView			scrSchedules;
	public static final String				GOLDEN_HOUR_STR		= "09:00";

	protected static final int				MAX_ROW				= 3;
	public static final String				SCREEN_MODE_WEEK	= "WE";
	public static final String				SCREEN_MODE_MONTH	= "MO";
	public static final String				SCREEN_MODE_DAY		= "DA";
	public static final String				SCREEN_MODE_DS		= "DS";
	protected Date							today;
	private List<RoomModel>					rooms;

	abstract protected List<Date> getAllDate();

	protected void clearOldData(){
	}

	@Override
	protected void initView(){
		super.initView();
		scrSchedules = (ObservableScrollView)getView().findViewById(R.id.scr_schedules);
		today = Calendar.getInstance().getTime();

		inflater = LayoutInflater.from(activity);

		if(pageSharingHolder != null && pageSharingHolder.isLoadingSchedules == false){
			lnrCalendarContainer = (LinearLayout)getView().findViewById(R.id.lnr_calendar_container);
			dates = getAllDate();
			initCalendarView();
		}

		// imgExpand = (ImageView)getView().findViewById(R.id.ic_icon_expand);
	}

	protected void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	abstract protected void initCalendarHeader();

	abstract protected void initDayViews();

	public void loadScheduleList(){
		JSONObject jsonObject = prepareJsonObject(getScreenMode());
		requestLoad(ClConst.API_SCHEDULE_LIST, jsonObject, false);
	}

	public void loadScheduleListForDialog(){
		JSONObject jsonObject = prepareJsonObject(SCREEN_MODE_DS);
		requestLoad(ClConst.API_SCHEDULE_LIST, jsonObject, false);
	}

	protected JSONObject prepareJsonObject(String screenMode){
		return buildJsonObjForScheduleListRequest(prefAccUtil, dates.get(0), dates.get(dates.size() - 1), screenMode);
	}

	public JSONObject buildJsonObjForScheduleListRequest(PreferencesAccountUtil prefAccUtil, Date startDate, Date endDate, String screenMode){

		String targetUserList = null;
		String searchText = null;
		String filterType = prefAccUtil.get(ClConst.PREF_FILTER_TYPE);
		String searchType = null;

		if(filterType.equals(ClConst.PREF_FILTER_TYPE_USER)){
			searchType = "USER";
			searchText = "-1";
			targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
			if(CCStringUtil.isEmpty(targetUserList)){
				targetUserList = "";
			}
		}else{
			searchType = "FACI";
			targetUserList = "-1";
			searchText = prefAccUtil.get(ClConst.PREF_ACTIVE_ROOM);
			if(CCStringUtil.isEmpty(searchText)){
				searchText = "";
			}
		}

		String duplicatedSchedule = getDuplicateMode();

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("searchText", searchText);
			jsonObject.put("searchType", searchType);
			jsonObject.put("screenMode", screenMode);
			jsonObject.put("duplicatedSchedule", duplicatedSchedule);
			jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, startDate));
			jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, endDate));
			jsonObject.put("execType", getExecType());
		}catch(JSONException e){
			e.printStackTrace();
		}
		return jsonObject;
	}

	protected abstract String getDuplicateMode();

	protected abstract String getScreenMode();

	abstract protected String getExecType();

	@Override
	protected void successLoad(JSONObject response, String url){
		if(ClConst.API_SCHEDULE_LIST.equals(url)){
			onLoadSchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	protected void commonNotSuccess(JSONObject response, String url){
		pageSharingHolder.isLoadingSchedules = false;
		super.commonNotSuccess(response, url);
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
			lstWorkRequest = LoganSquare.parseList(response.optString("workOfferList"), WorkRequest.class);
			calendarBirthdayModels = LoganSquare.parseList(response.optString("birthdayList"), CalendarBirthdayModel.class);
			lstCalendarUser = LoganSquare.parseList(response.optString("calendarUsers"), UserModel.class);
			rooms = LoganSquare.parseList(response.optString("rooms"), RoomModel.class);
			todos = LoganSquare.parseList(response.optString("todoList"), Todo.class);

			lstSchedule = filterByPublicity();

			ScheduleModel.determinePeriod(lstSchedule);

			if(refreshDialogData && !newScheduleStrings.equals(scheduleStrings)){
				isChangedData = true;
			}
			scheduleStrings = newScheduleStrings;
			pageSharingHolder.isLoadingSchedules = false;

			// make daily summary dialog
			if(dialogDailySummary == null){
				dialogDailySummary = new DailySummaryDialog(activity, this, this, dates);
				mRootView.post(new Runnable() {

					@Override
					public void run(){
						dialogDailySummary.setData(lstSchedule, calendarBirthdayModels, lstHoliday, lstWorkRequest, getScreenMode());
					}
				});

			}

			if(refreshDialogData && isChangedData){
				//// TODO: 4/27/2017 more check change data
				mRootView.post(new Runnable() {

					@Override
					public void run(){
						dialogDailySummary.setData(lstSchedule, calendarBirthdayModels, lstHoliday, lstWorkRequest, getScreenMode());
					}
				});
				isChangedData = false;
			}

			Calendar selectedCal = CCDateUtil.makeCalendar(selectedDate);
			for(ScheduleModel scheduleModel : lstSchedule){
				if(ScheduleModel.EVENT_TYPE_BIRTHDAY.equals(scheduleModel.eventType)){
					Calendar cStart = CCDateUtil.makeCalendar(scheduleModel.startDate);
					cStart.set(Calendar.YEAR, selectedCal.get(Calendar.YEAR));
					scheduleModel.startDate = cStart.getTime();
					scheduleModel.endDate = scheduleModel.startDate;
				}
			}

			updateSchedules(lstSchedule, lstCategory);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private List<ScheduleModel> filterByPublicity(){
		List<ScheduleModel> result = new ArrayList<>();

		for(ScheduleModel scheduleModel : lstSchedule){
			if(CCStringUtil.isEmpty(scheduleModel.scheduleType) || ScheduleModel.SCHEDULE_TYPE_PUB.equals(scheduleModel.scheduleType) || scheduleModel.publicMode){
				result.add(scheduleModel);
			}
		}
		return result;
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
	public void onDailyScheduleClickListener(Date date){
		if(dialogDailySummary != null && !dialogDailySummary.isShowing()){
			dialogDailySummary.show(date);
			refreshDialogData = true;
			loadScheduleListForDialog();
		}
	}

	@Override
	protected void loadData(){
		loadScheduleList();
	}

	protected int getNormalDayColor(){
		return ContextCompat.getColor(activity, R.color.wf_common_color_text);
	}

	@Override
	public void benchmark(String msg){
		if(pageSharingHolder.selectedPagePosition == pagePosition){
			super.benchmark(msg);
		}
	}

}
