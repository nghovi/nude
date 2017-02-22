package trente.asia.calendar.services.calendar;

import static trente.asia.calendar.services.calendar.WeeklyPageFragment.buildCalendarDayModels;

import static trente.asia.calendar.services.calendar.WeeklyPageFragment.getCalendarDayModelByDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.model.CalendarDayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.CalendarDayListAdapter;
import trente.asia.calendar.services.calendar.view.WeeklyCalendarDayView;
import trente.asia.calendar.services.calendar.view.NavigationHeader;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * DailyPageFragment
 *
 * @author Vietnh
 */
public class DailyPageFragment extends WelfareFragment implements DailyScheduleClickListener,ObservableScrollViewCallbacks,WeeklyCalendarDayView.OnDayClickListener{

	private LinearLayout				lnrCalendarSection;
	private List<ScheduleModel>			scheduleModels		= new ArrayList<>();
	private List<WeeklyCalendarDayView>	calendarDayViews	= new ArrayList<>();

	private ClDialog					dialogScheduleList;
	private ClFilterUserListDialog		filterDialog;
	private ObservableListView			observableListView;
	private NavigationHeader			navigationHeader;
	private Date						selectedDate;
	private UserListLinearLayout		userListLinearLayout;
	private CalendarDayListAdapter		adapter;
	private List<CalendarDayModel>		calendarDayModels;
	LayoutInflater						mInflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_daily_page, container, false);
		}
		return mRootView;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		if(navigationHeader != null && !navigationHeader.isUpdated){
			// String title = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_12, this.week.get(0));
			// List<String> selectedCalendarIds = Arrays.asList(prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING).split(","));
			// int selectedCalendarSize = selectedCalendarIds.size();
			// String subtitle = selectedCalendarSize > 1 ? selectedCalendarSize + " calendars" : getCalendarName(selectedCalendarIds.get(0));
			// navigationHeader.updateHeaderTitles(title, subtitle);
			// navigationHeader.isUpdated = true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();

		userListLinearLayout = (UserListLinearLayout)activity.findViewById(R.id.lnr_id_user_list);
		userListLinearLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				filterDialog.show();

			}
		});

		mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lnrCalendarSection = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_page);

		observableListView = (ObservableListView)getView().findViewById(R.id.lst_calendar_day);
		observableListView.setScrollViewCallbacks(this);
		observableListView.setDivider(null);

		// add calendar title
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		for(DayModel dayModel : CsDateUtil.getAllDay4Week(Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK))){
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
		lnrCalendarSection.addView(titleView);

		initDayViews();

		initDialog();
	}

	public void initDayViews(){
		WeeklyCalendarDayView.OnDayClickListener listener = new WeeklyCalendarDayView.OnDayClickListener() {

			@Override
			public void onClick(CalendarDayModel calendarDayModel){
				int selectedPosition = adapter.findPosition4Code(calendarDayModel.date);
				observableListView.setSelection(selectedPosition);
			}
		};
		View rowView = null;
		LinearLayout lnrRowContent = null;
		Date firstDateOfMonth = CCDateUtil.makeDateWithFirstday(selectedDate);
		List<Date> lstDate = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(firstDateOfMonth), CCNumberUtil.toInteger(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));

		for(int index = 0; index < lstDate.size(); index++){
			Date itemDate = lstDate.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrCalendarSection.addView(rowView);
			}

			WeeklyCalendarDayView dayView = new WeeklyCalendarDayView(activity);
			dayView.initialization(itemDate);
			calendarDayViews.add(dayView);

			lnrRowContent.addView(dayView);
		}
	}

	private void initDialog(){
		dialogScheduleList = new ClDialog(activity);
		dialogScheduleList.setDialogScheduleList();
	}

	@Override
	protected void initData(){
		loadDailyScheduleList();
	}

	public void loadDailyScheduleList(){
		String targetUserList = "";
		if(userListLinearLayout != null){
			targetUserList = userListLinearLayout.formatUserList();
		}

		JSONObject jsonObject = new JSONObject();
		try{
			if(!CCStringUtil.isEmpty(targetUserList)){
				jsonObject.put("targetUserList", targetUserList);
			}

			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));
			// jsonObject.put("endDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CL_SCHEDULE_DAY_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_SCHEDULE_MONTH_LIST.equals(url)){
			onLoadDailySchedulesSuccess(response, url);
		}else{
			super.successLoad(response, url);
		}
	}

	private void onLoadDailySchedulesSuccess(JSONObject response, String url){
		scheduleModels = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);
		calendarDayModels = buildCalendarDayModels(scheduleModels);

		// clear old data
		for(WeeklyCalendarDayView dayView : calendarDayViews){
			CalendarDayModel calendarDayModel = getCalendarDayModelByDate(dayView.getDate(), calendarDayModels);
			dayView.setData(calendarDayModel, this);
		}

		List<UserModel> calendarUsers = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);

		if(!CCCollectionUtil.isEmpty(calendarUsers)){
			userListLinearLayout.show(calendarUsers, (int)getResources().getDimension(R.dimen.margin_30dp));
			filterDialog = new ClFilterUserListDialog(activity, userListLinearLayout);
			filterDialog.updateUserList(calendarUsers);
		}

		UserListLinearLayout lnrUserList = (UserListLinearLayout)activity.findViewById(R.id.lnr_id_user_list);
		lnrUserList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				filterDialog.show();

			}
		});

		adapter = new CalendarDayListAdapter(activity, R.layout.item_calendar_day, calendarDayModels, new CalendarDayListAdapter.OnScheduleClickListener() {

			@Override
			public void onClick(ScheduleModel schedule){
				onClickSchedule(schedule);
			}
		});
		observableListView.setAdapter(adapter);

	}

	private void onClickSchedule(ScheduleModel schedule){

	}

	@Override
	public void onDailyScheduleClickListener(String day){
		dialogScheduleList.show();
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging){
		// // TODO: 2/22/2017 copy from weekly
	}

	@Override
	public void onDownMotionEvent(){

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState){
		if(scrollState == ScrollState.UP){
			lnrCalendarSection.setVisibility(View.GONE);
		}else if(scrollState == ScrollState.DOWN){
			lnrCalendarSection.setVisibility(View.VISIBLE);
		}
	}

	public void setNavigationHeader(NavigationHeader navigationHeader){
		this.navigationHeader = navigationHeader;
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}

	@Override
	public void onClick(CalendarDayModel calendarDayModel){
		int selectedPosition = adapter.findPosition4Code(calendarDayModel.date);
		observableListView.setSelection(selectedPosition);
	}
}
