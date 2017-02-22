package trente.asia.calendar.services.calendar;

import static trente.asia.calendar.services.calendar.CalendarListFragment
		.SELECTED_CALENDAR_STRING;
import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview
		.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends WelfareFragment implements ObservableScrollViewCallbacks,CalendarView.OnCalendarDaySelectedListener{

	private ObservableListView					observableListView;
	private Date								selectedDate;

	private LinearLayout						lnrContentHeader;
	private List<WeeklyCalendarHeaderRowView>	lstHeaderRow	= new ArrayList<>();
	private List<UserModel>						filteredUsers	= new ArrayList<>();
	private ClFilterUserListDialog				filterDialog;
	private List<CalendarDayModel>				calendarDayModels;
	private CalendarDayListAdapter				adapter;
	private UserListLinearLayout				userListLinearLayout;
	private List<Date>							week;
	private NavigationHeader					navigationHeader;
	List<CalendarModel>							calendars;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_weekly_page, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		lnrContentHeader = (LinearLayout)getView().findViewById(R.id.lnr_id_content_header);
		observableListView = (ObservableListView)getView().findViewById(R.id.lst_calendar_day);
		observableListView.setScrollViewCallbacks(this);
		observableListView.setDivider(null);
		this.week = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar(selectedDate), CCNumberUtil.toInteger(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));
		initContentHeader();
	}

	private void requestLoadWeeklyScheule(){
		Calendar c = Calendar.getInstance();
		c.setTime(this.week.get(0));

		String selectedCalendarStr = prefAccUtil.get(SELECTED_CALENDAR_STRING);
		String targetUserList = "";
		if(userListLinearLayout != null){
			targetUserList = userListLinearLayout.formatUserList();
		}
		JSONObject jsonObject = new JSONObject();
		try{
			if(!CCStringUtil.isEmpty(targetUserList)){
				jsonObject.put("targetUserList", targetUserList);
			}
			jsonObject.put("calendars", selectedCalendarStr);
			jsonObject.put("startDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, c.getTime()));
			c.add(Calendar.DATE, 7);
			jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, c.getTime()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
	}

	private void initContentHeader(){
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
			}
			txtTitleItem.setText(dayModel.day);
			lnrRowTitle.addView(titleItem);
		}
		lnrContentHeader.addView(titleView);

	}

	private void inflateDates(){
		WeeklyCalendarDayView.OnDayClickListener listener = new WeeklyCalendarDayView.OnDayClickListener() {

			@Override
			public void onClick(CalendarDayModel calendarDayModel){
				int selectedPosition = adapter.findPosition4Code(calendarDayModel.date);
				observableListView.setSelection(selectedPosition);
			}
		};
		WeeklyCalendarHeaderRowView rowView = null;
		for(int index = 0; index < this.week.size(); index++){
			Date itemDate = this.week.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = new WeeklyCalendarHeaderRowView(activity, index / CsConst.DAY_NUMBER_A_WEEK);
				rowView.initialization();
				lnrContentHeader.addView(rowView);
				lstHeaderRow.add(rowView);
			}

			WeeklyCalendarDayView dayView = new WeeklyCalendarDayView(activity);
			CalendarDayModel calendarDayModel = getCalendarDayModelByDate(itemDate);
			dayView.initialization(itemDate, calendarDayModel, listener);
			rowView.lnrRowContent.addView(dayView);

		}
	}

	private void onClickSchedule(ScheduleModel schedule){
		ScheduleDetailFragment fragment = new ScheduleDetailFragment();
		fragment.setSchedule(schedule);
		android.support.v4.app.FragmentManager manager = getParentFragment().getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(trente.asia.android.R.id.ipt_id_body, fragment);
		transaction.addToBackStack(null);
		transaction.commit();

	}

	@Override
	protected void initData(){
		requestLoadWeeklyScheule();
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_WEEK_SCHEDULE.equals(url)){
			onLoadWeeklySchedulesSuccess(response);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onLoadWeeklySchedulesSuccess(JSONObject response){
		List<ScheduleModel> schedules = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		separateDateTime(schedules);
		List<ApiObjectModel> categories = CCJsonUtil.convertToModelList(response.optString("categories"), ApiObjectModel.class);
		calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		List<UserModel> calendarUsers = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);
		filteredUsers = initFilteredUser(calendarUsers);
		updateSchedules(schedules, categories);
		calendarDayModels = buildCalendarDayModels(schedules);
		adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, calendarDayModels, new CalendarDayListAdapter.OnScheduleClickListener() {

			@Override
			public void onClick(ScheduleModel schedule){
				onClickSchedule(schedule);
			}
		});
		observableListView.setAdapter(adapter);

		inflateDates();

		userListLinearLayout = (UserListLinearLayout)activity.findViewById(R.id.lnr_id_user_list);
		userListLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				filterDialog.show();

			}
		});

		if(!CCCollectionUtil.isEmpty(calendarUsers)){
			userListLinearLayout.show(calendarUsers, (int)getResources().getDimension(R.dimen.margin_30dp));
			filterDialog = new ClFilterUserListDialog(activity, userListLinearLayout);
			filterDialog.updateUserList(calendarUsers);
		}

		if(!navigationHeader.isUpdated){
			String title = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, this.week.get(0));
			List<String> selectedCalendarIds = Arrays.asList(prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING).split(","));
			int selectedCalendarSize = selectedCalendarIds.size();
			String subtitle = selectedCalendarSize > 1 ? selectedCalendarSize + " calendars" : getCalendarName(selectedCalendarIds.get(0));
			navigationHeader.updateHeaderTitles(title, subtitle);
			navigationHeader.isUpdated = true;
		}

	}

	private String getCalendarName(String calendarId){
		for(CalendarModel calendarModel : calendars){
			if(calendarModel.key.equals(calendarId)){
				return calendarModel.calendarName;
			}
		}
		return "";
	}

	private List<UserModel> initFilteredUser(List<UserModel> allUsers){
		List<UserModel> userModels = new ArrayList<>();
		for(UserModel userModel : allUsers){
			userModels.add(userModel);
		}
		return userModels;
	}

	public static void updateSchedules(List<ScheduleModel> schedules, List<ApiObjectModel> categories){
		Map<String, String> categoriesMap = convertList2Map(categories);
		for(ScheduleModel schedule : schedules){
			schedule.categoryName = categoriesMap.get(schedule.categoryId);
		}
	}

	private List<CalendarDayModel> buildCalendarDayModels(List<ScheduleModel> schedules){
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

		Comparator<CalendarDayModel> comparator = new Comparator<CalendarDayModel>() {

			@Override
			public int compare(CalendarDayModel left, CalendarDayModel right){
				return CCDateUtil.makeDateCustom(left.date, WelfareConst.WL_DATE_TIME_7).compareTo(CCDateUtil.makeDateCustom(right.date, WelfareConst.WL_DATE_TIME_7));
			}
		};

		Collections.sort(calendarDayModels, comparator); // use the comparator as much as u want

		return calendarDayModels;
	}

	private static void separateDateTime(List<ScheduleModel> scheduleModels){
		for(ScheduleModel scheduleModel : scheduleModels){
			Date startDate = CCDateUtil.makeDateCustom(scheduleModel.startDate, WelfareConst.WL_DATE_TIME_1);
			Date endDate = CCDateUtil.makeDateCustom(scheduleModel.endDate, WelfareConst.WL_DATE_TIME_1);
			scheduleModel.startDate = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, startDate);
			scheduleModel.endDate = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, endDate);
			scheduleModel.startTime = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_9, startDate);
			scheduleModel.endTime = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_9, endDate);
		}
	}

	private CalendarDayModel getCalendarDayModelByDate(Date date){
		for(CalendarDayModel calendarDayModel : calendarDayModels){
			if(calendarDayModel.date.equals(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, date))){
				return calendarDayModel;
			}
		}
		return null;
	}

	private CalendarDayModel getCalendarDayModel(String day, List<CalendarDayModel> calendarDayModels){
		for(CalendarDayModel calendarDayModel : calendarDayModels){
			if(calendarDayModel.date.equals(day)){
				return calendarDayModel;
			}
		}
		return null;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging){

	}

	@Override
	public void onDownMotionEvent(){

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState){
		if(scrollState == ScrollState.UP){
			for(WeeklyCalendarHeaderRowView rowView : lstHeaderRow){
				if(rowView.index != 0){
					rowView.setVisibility(View.GONE);
				}
			}
		}else if(scrollState == ScrollState.DOWN){
			for(WeeklyCalendarHeaderRowView rowView : lstHeaderRow){
				rowView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onCalendarDaySelected(CalendarDayModel reportModel){
		// // TODO: 2/8/2017
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}

	public void setNavigationHeader(NavigationHeader navigationHeader){
		this.navigationHeader = navigationHeader;
	}
}
