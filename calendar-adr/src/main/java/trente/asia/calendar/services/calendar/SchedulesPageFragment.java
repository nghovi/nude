package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
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
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.RoomModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkRequest;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.HolidayWorkingModel;
import trente.asia.welfare.adr.models.OvertimeRequestModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.models.VacationRequestModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * SchedulesPageFragment
 *
 * @author VietNH
 */
public abstract class SchedulesPageFragment extends ClPageFragment implements WeeklyScheduleListAdapter.OnScheduleItemClickListener,DailyScheduleClickListener{

	protected List<Date>				dates;

	protected LinearLayout				lnrCalendarContainer;
	protected List<ScheduleModel>		lstSchedule			= new ArrayList<>();
	protected List<HolidayModel>		lstHoliday;

	protected List<CalendarModel>		lstCalendar;
	protected List<UserModel>			lstCalendarUser;
	protected List<CategoryModel>		lstCategory;
	protected List<UserModel>			lstBirthdayUser;
	protected List<WorkRequest>			lstWorkRequest;
	protected boolean					refreshDialogData	= false;
	private String						scheduleStrings;
	protected boolean					isChangedData		= true;
	protected List<Todo>				todos;
	protected LayoutInflater			inflater;
	protected DailySummaryDialog		dialogDailySummary;
	protected boolean					isExpanded			= false;
	protected TextView					txtMore;
	// protected ImageView imgExpand;
	protected ObservableScrollView		scrSchedules;
	protected View						thisHourView;
	public static final String			GOLDEN_HOUR_STR		= "09:00";

	protected static final int			MAX_ROW				= 3;
	protected Date						today;
	private List<VacationRequestModel>	lstVacationRequest;
	private List<OvertimeRequestModel>	lstOvertimeRequest;
	private List<HolidayWorkingModel>	lstHolidayWorking;
	private List<RoomModel>				rooms;

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

		txtMore = (TextView)getView().findViewById(R.id.txt_more_to_come);
		// imgExpand = (ImageView)getView().findViewById(R.id.ic_icon_expand);
	}

	protected void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	abstract protected void initCalendarHeader();

	abstract protected void initDayViews();

	public void loadScheduleList(){
		JSONObject jsonObject = prepareJsonObject();
		requestLoad(ClConst.API_SCHEDULE_LIST, jsonObject, false);
	}

	protected JSONObject prepareJsonObject(){
		return buildJsonObjForScheduleListRequest(prefAccUtil, dates.get(0), dates.get(dates.size() - 1));
	}

	public JSONObject buildJsonObjForScheduleListRequest(PreferencesAccountUtil prefAccUtil, Date startDate, Date endDate){

		String targetUserList = null;
		String searchText = null;
		String filterType = prefAccUtil.get(ClConst.PREF_FILTER_TYPE);
		String searchType = null;
		if(filterType.equals(ClConst.PREF_FILTER_TYPE_USER)){
			searchType = "USER";
			searchText = "-1";
			targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
			if(CCStringUtil.isEmpty(targetUserList)){
				targetUserList = "-1";
			}
		}else{
			searchType = "FACI";
			targetUserList = "-1";
			searchText = prefAccUtil.get(ClConst.PREF_ACTIVE_ROOM);
			if(CCStringUtil.isEmpty(searchText)){
				searchText = "-1";
			}
		}

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("searchText", searchText);
			jsonObject.put("searchType", searchType);
			jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, startDate));
			jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, endDate));
			jsonObject.put("execType", getExecType());
		}catch(JSONException e){
			e.printStackTrace();
		}
		return jsonObject;
	}

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
			lstVacationRequest = LoganSquare.parseList(response.optString("vacationList"), VacationRequestModel.class);
			lstOvertimeRequest = LoganSquare.parseList(response.optString("overtimeList"), OvertimeRequestModel.class);
			lstHolidayWorking = LoganSquare.parseList(response.optString("holidayWorkList"), HolidayWorkingModel.class);
			lstBirthdayUser = LoganSquare.parseList(response.optString("birthdayList"), UserModel.class);
			lstCalendarUser = LoganSquare.parseList(response.optString("calendarUsers"), UserModel.class);
			rooms = LoganSquare.parseList(response.optString("rooms"), RoomModel.class);
			todos = LoganSquare.parseList(response.optString("todoList"), Todo.class);

			lstSchedule = filterByPublicity();
			adaptVacationRequestsToWorkOffers();
			adaptOvertimeRequestsToWorkRequestOvertime();
			adaptHolidayWorkingRequestsToWorkOffers();

			ScheduleModel.determinePeriod(lstSchedule);

			// if(pageSharingHolder.selectedPagePosition == pagePosition)
			//
			// log("finish parsing:");

			// if(pageSharingHolder.selectedPagePosition == pagePosition)
			//
			// log("finish 1st step sort");

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
						dialogDailySummary.setData(lstSchedule, lstBirthdayUser, lstHoliday, lstWorkRequest);
					}
				});

			}

			if(refreshDialogData && isChangedData){
				//// TODO: 4/27/2017 more check change data
				mRootView.post(new Runnable() {

					@Override
					public void run(){
						dialogDailySummary.setData(lstSchedule, lstBirthdayUser, lstHoliday, lstWorkRequest);
					}
				});
				isChangedData = false;
			}

			updateSchedules(lstSchedule, lstCategory);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void adaptHolidayWorkingRequestsToWorkOffers(){
		for(HolidayWorkingModel holidayWorkingModel : lstHolidayWorking){
			WorkRequest workRequest = new WorkRequest(holidayWorkingModel, getString(R.string.holiday_work));
			lstWorkRequest.add(workRequest);
		}
	}

	private void adaptOvertimeRequestsToWorkRequestOvertime(){
		for(OvertimeRequestModel otRequest : lstOvertimeRequest){
			String overTimeInfo = otRequest.overtimeType.equals(OvertimeRequestModel.OVERTIME_EARLY) ? getString(R.string.overtime_early) : getString(R.string.overtime_lately);
			WorkRequest workRequest = new WorkRequest(otRequest, overTimeInfo);
			lstWorkRequest.add(workRequest);
		}
	}

	private void adaptVacationRequestsToWorkOffers(){
		for(VacationRequestModel vacationRequestModel : lstVacationRequest){
			WorkRequest workRequest = new WorkRequest(vacationRequestModel);
			lstWorkRequest.add(workRequest);
		}
	}

	private List<UserModel> filterByUser(){
		List<UserModel> result = new ArrayList<>();
		String targetUserData = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
		List<String> targetUserIds = CCStringUtil.isEmpty(targetUserData) ? null : Arrays.asList(targetUserData.split(","));
		if(!CCCollectionUtil.isEmpty(targetUserIds)){
			for(UserModel userModel : lstBirthdayUser){
				if(targetUserIds.contains(userModel.key)){
					result.add(userModel);
				}
			}
		}else{
			return lstBirthdayUser;
		}
		return result;
	}

	private List<ScheduleModel> filterByPublicity(){
		List<ScheduleModel> result = new ArrayList<>();

		for(ScheduleModel scheduleModel : lstSchedule){
			if(ClConst.SCHEDULE_TYPE_PUB.equals(scheduleModel.scheduleType) || containUser(scheduleModel, myself.key)){
				result.add(scheduleModel);
			}else if(ClConst.SCHEDULE_TYPE_PRI.equals(scheduleModel.scheduleType)){
				scheduleModel.scheduleName = getString(R.string.schedule_mystery);
				result.add(scheduleModel);
			}
		}
		return result;
	}

	private boolean containUser(ScheduleModel scheduleModel, String userKey){
		for(UserModel userModel : scheduleModel.scheduleJoinUsers){
			if(userModel.key.equals(userKey)){
				return true;
			}
		}
		return false;
	}

	protected void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		Map<String, CategoryModel> categoryMap = ClUtil.convertCategory2Map(categories);
		for(ScheduleModel schedule : schedules){
			schedule.categoryModel = categoryMap.get(schedule.categoryId);
		}
	}

	protected List<ScheduleModel> multiplyWithUsers(List<ScheduleModel> origins){
		List<ScheduleModel> result = new ArrayList<>();
		String filterType = prefAccUtil.get(ClConst.PREF_FILTER_TYPE);
		if(filterType.equals(ClConst.PREF_FILTER_TYPE_USER)){
			String targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
			List<String> targetUserListId = Arrays.asList(targetUserList.split(","));
			for(ScheduleModel scheduleModel : origins){
				for(UserModel userModel : scheduleModel.scheduleJoinUsers){
					if(targetUserListId.contains(userModel.key)){
						ScheduleModel cloned = ScheduleModel.clone(scheduleModel, userModel);
						result.add(cloned);
					}
				}
			}
		}else{
			for(ScheduleModel scheduleModel : origins){
				if(!CCStringUtil.isEmpty(scheduleModel.roomModel.color)){
					scheduleModel.scheduleColor = scheduleModel.roomModel.color;
				}
				result.add(scheduleModel);
			}
		}

		return result;
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

	@Override
	public void benchmark(String msg){
		if(pageSharingHolder.selectedPagePosition == pagePosition){
			super.benchmark(msg);
		}
	}

}
