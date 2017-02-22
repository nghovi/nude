package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
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
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
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
public class DailyPageFragment extends WelfareFragment implements DailyScheduleClickListener,ObservableScrollViewCallbacks{

	private LinearLayout					lnrMonthlyPage;
	private List<ScheduleModel>				scheduleModels		= new ArrayList<>();
	private List<MonthlyCalendarDayView>	calendarDayViews	= new ArrayList<>();

	private ClDialog						dialogScheduleList;
	private ClFilterUserListDialog			filterDialog;
	private ObservableListView				observableListView;
	private NavigationHeader				navigationHeader;
	private Date							selectedDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_daily_page, container, false);
		}
		return mRootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		host = BuildConfig.HOST;
	}

	@Override
	protected void initView(){
		super.initView();

		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		lnrMonthlyPage = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_page);

		observableListView = (ObservableListView)getView().findViewById(R.id.lst_calendar_day);
		observableListView.setScrollViewCallbacks(this);
		observableListView.setDivider(null);

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
		lnrMonthlyPage.addView(titleView);

		List<Date> lstDate = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(selectedDate), CCNumberUtil.toInteger(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK));

		View rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < lstDate.size(); index++){
			Date itemDate = lstDate.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrMonthlyPage.addView(rowView);
			}

			MonthlyCalendarDayView dayView = new MonthlyCalendarDayView(activity);
			dayView.initialization(itemDate, this);
			calendarDayViews.add(dayView);

			lnrRowContent.addView(dayView);
		}

		initDialog();
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
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserList", "");
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, selectedDate));
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

		// clear old data
		for(MonthlyCalendarDayView dayView : calendarDayViews){
			dayView.removeAllData();
		}

		if(!CCCollectionUtil.isEmpty(scheduleModels)){
			for(ScheduleModel model : scheduleModels){
				// Date startDate = WelfareUtil.makeDate(model.startDate);
				List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(calendarDayViews, model);

				for(MonthlyCalendarDayView calendarDayView : lstActiveCalendarDay){
					calendarDayView.addSchedule(model);
				}
			}
		}
		List<UserModel> lstCalendarUser = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);

		UserListLinearLayout lnrUserList = (UserListLinearLayout)activity.findViewById(R.id.lnr_id_user_list);
		lnrUserList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				filterDialog.show();

			}
		});
		// lnrUserList.removeAllViews();
		if(!CCCollectionUtil.isEmpty(lstCalendarUser)){
			lnrUserList.show(lstCalendarUser, (int)getResources().getDimension(R.dimen.margin_30dp));
			filterDialog = new ClFilterUserListDialog(activity, lnrUserList);
			filterDialog.updateUserList(lstCalendarUser);
		}
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

	}

	public void setNavigationHeader(NavigationHeader navigationHeader){
		this.navigationHeader = navigationHeader;
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}
}
