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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
	protected boolean				isExpanded			= false;
	protected TextView				txtMore;
	protected ImageView				imgExpand;
	protected ScrollView			scrollView;
	protected View					thisHourView;
	protected String				currentHour;

	protected static final int		MAX_ROW				= 2;
	protected Date					today;

	abstract protected List<Date> getAllDate();

	protected void clearOldData(){
	};

	@Override
	protected void initView(){
		super.initView();

		today = Calendar.getInstance().getTime();

		if(pageSharingHolder.isLoadingSchedules == false){
			lnrCalendarContainer = (LinearLayout)getView().findViewById(R.id.lnr_calendar_container);
			dates = getAllDate();
			initCalendarView();
		}

		inflater = LayoutInflater.from(activity);

		txtMore = (TextView)getView().findViewById(R.id.txt_more_to_come);
		imgExpand = (ImageView)getView().findViewById(R.id.ic_icon_expand);
		scrollView = (ScrollView)getView().findViewById(R.id.src_fragment_daily_page);
		// currentHour = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_HH_MM, Calendar.getInstance().getTime());
		// currentHour = currentHour.split(":")[0] + ":00";
		currentHour = "09:00";
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

	public JSONObject buildJsonObjForScheduleListRequest(PreferencesAccountUtil prefAccUtil, Date startDate, Date endDate){

		String targetUserList = null;
		String searchText = null;
		String filterType = prefAccUtil.get(ClConst.PREF_FILTER_TYPE);
		if(filterType.equals(ClConst.PREF_FILTER_TYPE_USER)){
			searchText = "-1";
			targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
			if(CCStringUtil.isEmpty(targetUserList)){
				targetUserList = "-1";
			}
		}else{
			targetUserList = "-1";
			searchText = prefAccUtil.get(ClConst.PREF_ACTIVE_ROOM);
			if(CCStringUtil.isEmpty(targetUserList)){
				searchText = "-1";
			}
		}

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("searchText", searchText);
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
			todos = LoganSquare.parseList(response.optString("todoList"), Todo.class);
			if(refreshDialogData && !newScheduleStrings.equals(scheduleStrings)){
				isChangedData = true;
			}
			scheduleStrings = newScheduleStrings;
			pageSharingHolder.isLoadingSchedules = false;

			lstSchedule = filterByPublicity();


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

			updateSchedules(lstSchedule, lstCategory);

		}catch(IOException e){
			e.printStackTrace();
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

	private boolean containUser(ScheduleModel scheduleModel, String key){
		if(CCStringUtil.isEmpty(scheduleModel.scheduleJoinUsers)){
			return false;
		}else{
			for(UserModel userModel : scheduleModel.scheduleJoinUsers){
				if(userModel.key.equals(key)){
					return true;
				}
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
		for(ScheduleModel scheduleModel : origins){
			if(!CCCollectionUtil.isEmpty(scheduleModel.scheduleJoinUsers)){
				for(UserModel userModel : scheduleModel.scheduleJoinUsers){
					ScheduleModel cloned = ScheduleModel.clone(scheduleModel, userModel);
					result.add(cloned);
				}
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
