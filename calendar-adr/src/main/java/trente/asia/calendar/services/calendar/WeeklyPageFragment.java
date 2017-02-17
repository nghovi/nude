package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
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

import org.json.JSONException;
import org.json.JSONObject;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.CalendarModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.CalendarView;
import trente.asia.calendar.services.calendar.view.HorizontalUserListView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarHeaderRowView;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.view.WfUserChooseDialog;

import static trente.asia.calendar.services.calendar.CalendarListFragment.SELECTED_CALENDAR_STRING;
import static trente.asia.welfare.adr.utils.WelfareFormatUtil.convertList2Map;

/**
 * WeeklyPageFragment
 *
 * @author TrungND
 */
public class WeeklyPageFragment extends WelfareFragment implements ObservableScrollViewCallbacks,CalendarView.OnCalendarDaySelectedListener{

	private ObservableListView					lstCalendarDay;
	private Date								selectedDate;

	private LinearLayout						lnrContentHeader;
	private List<WeeklyCalendarHeaderRowView>	lstHeaderRow	= new ArrayList<>();
	private HorizontalUserListView				horizontalUserListView;
	private List<UserModel>						filteredUsers	= new ArrayList<>();

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
		lstCalendarDay = (ObservableListView)getView().findViewById(R.id.lst_calendar_day);
		lstCalendarDay.setScrollViewCallbacks(this);
		lstCalendarDay.setDivider(null);
		horizontalUserListView = (HorizontalUserListView)getView().findViewById(R.id.view_horizontal_user_list);
		horizontalUserListView.setOnSelectedUsersChangedListener(new WfUserChooseDialog.onSelectedUsersChangedListener() {

			@Override
			public void onChange(){
				requestLoadWeeklyScheule();
			}
		});
		initContentHeader();
	}

	private void requestLoadWeeklyScheule(){
		Calendar c = Calendar.getInstance();
		c.setTime(selectedDate);

		String selectedCalendarStr = prefAccUtil.get(SELECTED_CALENDAR_STRING);
		String targetUserList = horizontalUserListView.getSelectedUserListString();
		JSONObject jsonObject = new JSONObject();
		try{
			// jsonObject.put("targetUserId", myself.key);
			jsonObject.put("targetUserList", targetUserList);
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
		// add calendar title
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		for(DayModel dayModel : CsDateUtil.getAllDay4Week(Calendar.THURSDAY)){
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

		List<Date> lstDate = CsDateUtil.getAllDate4Week(CCDateUtil.makeCalendar(selectedDate));
		WeeklyCalendarHeaderRowView rowView = null;
		for(int index = 0; index < lstDate.size(); index++){
			Date itemDate = lstDate.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = new WeeklyCalendarHeaderRowView(activity, index / CsConst.DAY_NUMBER_A_WEEK);
				rowView.initialization();
				lnrContentHeader.addView(rowView);
				lstHeaderRow.add(rowView);
			}

			WeeklyCalendarDayView dayView = new WeeklyCalendarDayView(activity);
			dayView.initialization(itemDate);
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
		// rooms = CCJsonUtil.convertToModelList(response.optString("rooms"), ApiObjectModel.class);
		List<ApiObjectModel> categories = CCJsonUtil.convertToModelList(response.optString("categories"), ApiObjectModel.class);
		List<CalendarModel> calendars = CCJsonUtil.convertToModelList(response.optString("calendars"), CalendarModel.class);
		List<UserModel> calendarUsers = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);
		filteredUsers = initFilteredUser(calendarUsers);
		horizontalUserListView.show(filteredUsers, calendarUsers, false, 32, 10);
		updateSchedules(schedules, categories);
		List<CalendarDayModel> dummy = getCalendarDayModels(schedules);
		CalendarDayListAdapter adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, dummy, new CalendarDayListAdapter.OnScheduleClickListener() {

			@Override
			public void onClick(ScheduleModel schedule){
				onClickSchedule(schedule);
			}
		});
		lstCalendarDay.setAdapter(adapter);
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

	private List<CalendarDayModel> getCalendarDayModels(List<ScheduleModel> schedules){
		List<CalendarDayModel> calendarDayModels = new ArrayList<>();
		for(ScheduleModel scheduleModel : schedules){
			CalendarDayModel calendarDayModel = getCalendarDayModel(scheduleModel.startDate, calendarDayModels);
			if(calendarDayModel == null){
				calendarDayModel = new CalendarDayModel();
				calendarDayModel.date = scheduleModel.startDate;
				calendarDayModel.schedules = new ArrayList<>();
			}
			calendarDayModel.schedules.add(scheduleModel);
			calendarDayModels.add(calendarDayModel);
		}
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
}
