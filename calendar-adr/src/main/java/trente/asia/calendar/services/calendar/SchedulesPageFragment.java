package trente.asia.calendar.services.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.ClPageFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.model.WorkOffer;
import trente.asia.calendar.services.calendar.view.WeeklyScheduleListAdapter;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * SchedulesPageFragment
 *
 * @author TrungND
 */
public abstract class SchedulesPageFragment extends ClPageFragment implements WeeklyScheduleListAdapter.OnScheduleItemClickListener{

	protected List<Date>			dates;

	protected LinearLayout			lnrCalendarContainer;
	protected List<ScheduleModel>	lstSchedule;
	protected List<HolidayModel>	lstHoliday;

	protected List<CalendarModel>	lstCalendar;
	protected List<UserModel>		lstCalendarUser;
	protected List<CategoryModel>	lstCategory;
	protected List<UserModel>		lstBirthdayUser;
	protected List<WorkOffer>		lstWorkOffer;
	protected boolean				refreshWithoutShowingLoading	= false;
	protected String				dayStr;
	private int headerBgColor;

	abstract protected List<Date> getAllDate();

	abstract protected void clearOldData();

	@Override
	protected void initView(){
		super.initView();
		lnrCalendarContainer = (LinearLayout)getView().findViewById(R.id.lnr_calendar_container);
		dates = getAllDate();
		initCalendarView();
	}

	private void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	private void initCalendarHeader() {
		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View titleView = mInflater.inflate(getCalendarHeaderItem(), null);
		LinearLayout lnrRowTitle = (LinearLayout) titleView.findViewById(R.id.lnr_id_row_title);
		int firstDay = Calendar.SUNDAY;
		if (!CCStringUtil.isEmpty(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK)) {
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		List<DayModel> dayModels = CsDateUtil.getAllDay4Week(firstDay);
		for (DayModel dayModel : dayModels) {
			View titleItem = mInflater.inflate(R.layout.monthly_calendar_title_item, null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			titleItem.setLayoutParams(layoutParams);
			TextView txtTitleItem = (TextView) titleItem.findViewById(R.id.monthly_calendar_title_day_label);
			titleItem.findViewById(R.id.lnr_title_background).setBackgroundColor(getHeaderBgColor());
			if (Calendar.SUNDAY == dayModel.dayOfWeek) {
				txtTitleItem.setTextColor(Color.RED);
			} else if (Calendar.SATURDAY == dayModel.dayOfWeek) {
				txtTitleItem.setTextColor(Color.BLUE);
			} else {
				txtTitleItem.setTextColor(getNormalDayColor());
			}
			txtTitleItem.setText(CCStringUtil.toUpperCase(dayModel.day));
			lnrRowTitle.addView(titleItem);
		}
		lnrCalendarContainer.addView(titleView);
	}

	protected void initDayViews(){
	}

	protected void loadScheduleList(){
		JSONObject jsonObject = prepareJsonObject();
		requestLoad(WfUrlConst.API_CL_SCHEDULE_LIST, jsonObject, !refreshWithoutShowingLoading);
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
		if(WfUrlConst.API_CL_SCHEDULE_LIST.equals(url)){
			onLoadSchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
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
		lstSchedule = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		lstCalendar = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		lstHoliday = CCJsonUtil.convertToModelList(response.optString("holidayList"), HolidayModel.class);
		lstCategory = CCJsonUtil.convertToModelList(response.optString("categories"), CategoryModel.class);
		lstWorkOffer = CCJsonUtil.convertToModelList(response.optString("workOfferList"), WorkOffer.class);
		lstBirthdayUser = CCJsonUtil.convertToModelList(response.optString("birthdayList"), UserModel.class);
		lstCalendarUser = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);

		if(changeCalendarUserListener != null){
			changeCalendarUserListener.onChangeCalendarUserListener(lstCalendarUser);
		}
		updateSchedules(lstSchedule, lstCategory);
	}

	public static void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
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
	protected void loadData(){
		loadScheduleList();
	}

	protected int getNormalDayColor(){
		return ContextCompat.getColor(activity, R.color.wf_common_color_text);
	}

	protected String getUpperTitle(){
		return CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MMMM_YY, dates.get(0));
	}

	protected int getHeaderBgColor() {
		return ContextCompat.getColor(activity, R.color.wf_login_background_color);
	}

	abstract int getCalendarHeaderItem();


}
