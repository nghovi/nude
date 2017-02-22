package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CsConst;
import trente.asia.android.model.DayModel;
import trente.asia.android.util.CsDateUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.dialogs.ClDialog;
import trente.asia.calendar.commons.dialogs.ClFilterUserListDialog;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * MonthlyPageFragment
 *
 * @author TrungND
 */
public class MonthlyPageFragment extends AbstractClFragment implements DailyScheduleClickListener{

	private Date							activeMonth;

	private LinearLayout					lnrMonthlyPage;
	private List<ScheduleModel>				lstSchedule		= new ArrayList<>();
	private List<MonthlyCalendarDayView>	lstCalendarDay	= new ArrayList<>();

	private ClDialog						dialogScheduleList;
//	private ClFilterUserListDialog			filterDialog;
	private OnChangeCalendarUserListener	changeCalendarUserListener;
	private List<Date>						lstDate4Month;

	public void setChangeCalendarUserListener(OnChangeCalendarUserListener changeCalendarUserListener){
		this.changeCalendarUserListener = changeCalendarUserListener;
	}

	public void setActiveMonth(Date activeMonth){
		this.activeMonth = activeMonth;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_monthly_page, container, false);
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

		lnrMonthlyPage = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_page);
        initCalendar();
		initDialog();
	}

	private void initCalendar(){
		lnrMonthlyPage.removeAllViews();
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// add calendar title
		View titleView = mInflater.inflate(R.layout.monthly_calendar_title, null);
		LinearLayout lnrRowTitle = (LinearLayout)titleView.findViewById(R.id.lnr_id_row_title);
		SettingModel settingModel = prefAccUtil.getSetting();
		int firstDay;
		if(CCStringUtil.isEmpty(settingModel.CL_START_DAY_IN_WEEK)){
			firstDay = Calendar.SUNDAY;
			settingModel.CL_START_DAY_IN_WEEK = String.valueOf(firstDay);
			prefAccUtil.saveSetting(settingModel);
			SettingModel st = prefAccUtil.getSetting();
			String t = st.CL_START_DAY_IN_WEEK;
		}else{
			firstDay = Integer.parseInt(prefAccUtil.getSetting().CL_START_DAY_IN_WEEK);
		}
		for(DayModel dayModel : CsDateUtil.getAllDay4Week(firstDay)){
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
		lstDate4Month = CsDateUtil.getAllDate4Month(CCDateUtil.makeCalendar(activeMonth), firstDay);
		View rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < lstDate4Month.size(); index++){
			Date itemDate = lstDate4Month.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrMonthlyPage.addView(rowView);
			}

			MonthlyCalendarDayView dayView = new MonthlyCalendarDayView(activity);
			dayView.initialization(itemDate, this);
			lstCalendarDay.add(dayView);

			lnrRowContent.addView(dayView);
		}
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	private void initDialog(){
		dialogScheduleList = new ClDialog(activity);
		dialogScheduleList.setDialogScheduleList();
	}

	@Override
	protected void initData(){
		String activeDateString = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeMonth);
		if(activeDateString.equals(prefAccUtil.get(ClConst.PREF_ACTIVE_DATE))){
			loadScheduleList();
		}
	}

	public void loadScheduleList(){
		WfSlideMenuLayout slideMenuLayout = (WfSlideMenuLayout)activity.findViewById(R.id.drawer_layout);
		if(slideMenuLayout.isMenuShown()){
			slideMenuLayout.toggleMenu();
		}
		JSONObject jsonObject = new JSONObject();
		try{
//			jsonObject.put("targetUserList", prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST));
            jsonObject.put("targetUserList", "");
			jsonObject.put("targetMonth", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, activeMonth));
			jsonObject.put("calendars", prefAccUtil.get(ClConst.SELECTED_CALENDAR_STRING));
            jsonObject.put("startDateString", WelfareFormatUtil.formatDate(lstDate4Month.get(0)));
            jsonObject.put("endDateString", WelfareFormatUtil.formatDate(lstDate4Month.get(lstDate4Month.size() - 1)));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CL_WEEK_SCHEDULE, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_CL_WEEK_SCHEDULE.equals(url)){
			lstSchedule = CCJsonUtil.convertToModelList(response.optString("schedules"), ScheduleModel.class);

			// clear old data
			for(MonthlyCalendarDayView dayView : lstCalendarDay){
				dayView.removeAllData();
			}

			if(!CCCollectionUtil.isEmpty(lstSchedule)){
				for(ScheduleModel model : lstSchedule){
					List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, model);

					for(MonthlyCalendarDayView calendarDayView : lstActiveCalendarDay){
						calendarDayView.addSchedule(model);
					}
				}
			}
			List<UserModel> lstCalendarUser = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);
//			UserListLinearLayout lnrUserList = (UserListLinearLayout)activity.findViewById(R.id.lnr_id_user_list);
//			lnrUserList.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v){
//					filterDialog.show();
//
//				}
//			});
//
//			if(!CCCollectionUtil.isEmpty(lstCalendarUser)){
//				filterDialog = new ClFilterUserListDialog(activity, lnrUserList);
//				filterDialog.updateUserList(lstCalendarUser);
//			}
			if(changeCalendarUserListener != null){
				changeCalendarUserListener.onChangeCalendarUserListener(lstCalendarUser);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onDailyScheduleClickListener(String day){
		dialogScheduleList.show();
	}

	@Override
	public void onClick(View v){

	}
}
