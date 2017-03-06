package trente.asia.calendar.services.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseFragment;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.CategoryModel;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.calendar.services.calendar.view.PageSharingHolder;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public abstract class SchedulesPageFragment extends AbstractClFragment implements CalendarDayListAdapter.OnScheduleItemClickListener,NavigationHeader.OnAddBtnClickedListener{

	protected Date							selectedDate;
	protected List<Date>					dates;
	protected int							pagePosition;
	protected PageSharingHolder				pageSharingHolder;

	protected LinearLayout					lnrCalendarContainer;
	protected List<ScheduleModel>			lstSchedule;
	protected List<HolidayModel>			lstHoliday;

	private OnChangeCalendarUserListener	changeCalendarUserListener;
	protected List<CalendarModel>			lstCalendar;
	protected List<UserModel>				lstCalendarUser;
	protected List<CategoryModel>			lstCategories;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	abstract protected List<Date> getAllDate();

	abstract protected void clearOldData();

	@Override
	protected void initView(){
		super.initView();
		pageSharingHolder.navigationHeader.setOnHeaderActionsListener(this);
		lnrCalendarContainer = (LinearLayout)getView().findViewById(R.id.lnr_calendar_container);
		dates = getAllDate();
		initCalendarView();
	}

	private void initCalendarView(){
		initCalendarHeader();
		initDayViews();
	}

	private void initCalendarHeader(){
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		List<DayModel> dayModels = CsDateUtil.getAllDay4Week(CCNumberUtil.toInteger(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));
		for(DayModel dayModel : dayModels){
			View titleItem = mInflater.inflate(R.layout.monthly_calendar_title_item, null);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			titleItem.setLayoutParams(layoutParams);
			TextView txtTitleItem = (TextView)titleItem.findViewById(R.id.txt_id_row_content);
			if(Calendar.SUNDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.RED);
			}else if(Calendar.SATURDAY == dayModel.dayOfWeek){
				txtTitleItem.setTextColor(Color.BLUE);
			}else{
				txtTitleItem.setTextColor(getNormalDayColor());
			}
			txtTitleItem.setText(dayModel.day);
			lnrRowTitle.addView(titleItem);
		}
		lnrCalendarContainer.addView(titleView);
	}

	abstract void initDayViews();

	// protected String getTargetUserList(){
	// return this.pageSharingHolder.userListLinearLayout.formatUserList();
	// }

	protected void loadScheduleList(){
		JSONObject jsonObject = prepareJsonObject();
		requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
	}

	protected JSONObject prepareJsonObject(){
		String targetUserList = prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", targetUserList);
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));

			jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, dates.get(0)));
			jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, dates.get(dates.size() - 1)));

		}catch(JSONException e){
			e.printStackTrace();
		}

		return jsonObject;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_WEEK_SCHEDULE.equals(url)){
			onLoadSchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	protected void onLoadSchedulesSuccess(JSONObject response){
		lstSchedule = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		lstCalendar = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		lstHoliday = CCJsonUtil.convertToModelList(response.optString("holidayList"), HolidayModel.class);
		lstCategories = CCJsonUtil.convertToModelList(response.optString("categories"), CategoryModel.class);

		lstCalendarUser = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);
		// check is my calendar
		// if(CCCollectionUtil.isEmpty(lstCalendarUser)){
		// lstCalendarUser = new ArrayList<>();
		// lstCalendarUser.add(prefAccUtil.getUserPref());
		// }
		if(changeCalendarUserListener != null){
			changeCalendarUserListener.onChangeCalendarUserListener(lstCalendarUser);
		}
		separateDateTime(lstSchedule);
		updateSchedules(lstSchedule, lstCategories);
	}

	public void updateSchedules(List<ScheduleModel> schedules, List<CategoryModel> categories){
		for(ScheduleModel schedule : schedules){
			for(CategoryModel categoryModel : categories){
				if(categoryModel.key.equals(schedule.categoryId)){
					schedule.categoryModel = categoryModel;
					break;
				}
			}
		}
	}

	private void separateDateTime(List<ScheduleModel> scheduleModels){
		for(ScheduleModel scheduleModel : scheduleModels){
			Date startDate = CCDateUtil.makeDateCustom(scheduleModel.startDate, WelfareConst.WL_DATE_TIME_1);
			Date endDate = CCDateUtil.makeDateCustom(scheduleModel.endDate, WelfareConst.WL_DATE_TIME_1);
			scheduleModel.startDate = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, startDate);
			scheduleModel.endDate = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, endDate);
			scheduleModel.startTime = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_9, startDate);
			scheduleModel.endTime = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_9, endDate);
		}
	}

	@Override
	public void onClickScheduleItem(ScheduleModel schedule){
		ScheduleDetailFragment fragment = new ScheduleDetailFragment();
		fragment.setSchedule(schedule);
		((WelfareActivity)activity).addFragment(fragment);
	}

	@Override
	protected void initData(){
		loadScheduleList();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}

	public void setPageSharingHolder(PageSharingHolder pageSharingHolder){
		this.pageSharingHolder = pageSharingHolder;
	}

	protected String getUpperTitle(){
		return CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, dates.get(0));
	}

	public void setPagePosition(int pagePosition){
		this.pagePosition = pagePosition;
	}

	public void setChangeCalendarUserListener(OnChangeCalendarUserListener changeCalendarUserListener){
		this.changeCalendarUserListener = changeCalendarUserListener;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	protected int getNormalDayColor(){
		return ContextCompat.getColor(activity, R.color.wf_common_color_text);
	}

	private void gotoScheduleFormFragment(Date date){
		ScheduleFormFragment scheduleFormFragment = new ScheduleFormFragment();
		if(date == null){
			date = pageSharingHolder.getClickedDate();
		}
		scheduleFormFragment.setSelectedDate(date);
		ChiaseFragment parentFragment = (ChiaseFragment)getParentFragment();
		parentFragment.gotoFragment(scheduleFormFragment);
	}

	@Override
	public void onAddBtnClick(Date date){
		String selectedCalendarString = prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING);
		if(!CCStringUtil.isEmpty(selectedCalendarString)){
			gotoScheduleFormFragment(date);
		}else{
			alertDialog.setMessage(getString(R.string.cl_common_validate_no_calendar_msg));
			alertDialog.show();
		}
	}
}
