package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import android.widget.ImageView;
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
import trente.asia.calendar.commons.dialogs.ClDailySummaryDialog;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.services.calendar.listener.DailyScheduleClickListener;
import trente.asia.calendar.services.calendar.listener.OnChangeCalendarUserListener;
import trente.asia.calendar.services.calendar.model.HolidayModel;
import trente.asia.calendar.services.calendar.model.ScheduleModel;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarDayView;
import trente.asia.calendar.services.calendar.view.MonthlyCalendarRowView;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.SettingModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
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
	private List<MonthlyCalendarRowView>	lstCalendarRow	= new ArrayList<>();

	private ClDailySummaryDialog			dialogDailySummary;
	private OnChangeCalendarUserListener	changeCalendarUserListener;
	private List<Date>						lstDate4Month;

	public class ScheduleComparator implements Comparator<ScheduleModel>{

		@Override
		public int compare(ScheduleModel schedule1, ScheduleModel schedule2){
			Date startDate1 = WelfareUtil.makeDate(schedule1.startDate);
			Date endDate1 = WelfareUtil.makeDate(schedule1.endDate);

			Date startDate2 = WelfareUtil.makeDate(schedule2.startDate);
			Date endDate2 = WelfareUtil.makeDate(schedule2.endDate);

			boolean diff1 = WelfareFormatUtil.formatDate(startDate1).equals(WelfareFormatUtil.formatDate(endDate1));
			boolean diff2 = WelfareFormatUtil.formatDate(startDate2).equals(WelfareFormatUtil.formatDate(endDate2));

			if(!diff1 && diff2) return -1;
			if(diff1 && !diff2) return 1;
			return 0;
		}
	}

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
		// initDialog();
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
		MonthlyCalendarRowView rowView = null;
		LinearLayout lnrRowContent = null;
		for(int index = 0; index < lstDate4Month.size(); index++){
			Date itemDate = lstDate4Month.get(index);
			if(index % CsConst.DAY_NUMBER_A_WEEK == 0){
				rowView = (MonthlyCalendarRowView)mInflater.inflate(R.layout.monthly_calendar_row, null);
				lnrRowContent = (LinearLayout)rowView.findViewById(R.id.lnr_id_row_content);
				lnrMonthlyPage.addView(rowView);
				lstCalendarRow.add(rowView);
			}

			MonthlyCalendarDayView dayView = (MonthlyCalendarDayView)mInflater.inflate(R.layout.monthly_calendar_row_item, null);
			dayView.initialization(itemDate, this);
			dayView.dayOfTheWeek = index % CsConst.DAY_NUMBER_A_WEEK;
			lstCalendarDay.add(dayView);
			rowView.lstCalendarDay.add(dayView);

			lnrRowContent.addView(dayView);
		}
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	private void initDialog(){
		dialogDailySummary = new ClDailySummaryDialog(activity, lstSchedule, lstDate4Month);
		ImageView imgAdd = (ImageView)dialogDailySummary.findViewById(R.id.img_id_add);
		imgAdd.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		String activeDateString = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, activeMonth);
		if(activeDateString.equals(prefAccUtil.get(ClConst.PREF_ACTIVE_DATE))){
			loadScheduleList();
			// try{
			// JSONObject jsonObject = new JSONObject(string);
			// successLoad(jsonObject, WfUrlConst.WF_CL_WEEK_SCHEDULE);
			// }catch(JSONException e){
			// e.printStackTrace();
			// }
		}
	}

	public void loadScheduleList(){
		WfSlideMenuLayout slideMenuLayout = (WfSlideMenuLayout)activity.findViewById(R.id.drawer_layout);
		if(slideMenuLayout.isMenuShown()){
			slideMenuLayout.toggleMenu();
		}
		JSONObject jsonObject = new JSONObject();
		try{
			// jsonObject.put("targetUserList", prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST));
			jsonObject.put("targetUserList", "");
			// jsonObject.put("targetMonth", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_5, activeMonth));
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
			List<HolidayModel> lstHoliday = CCJsonUtil.convertToModelList(response.optString("holidayList"), HolidayModel.class);

			// clear old data
			for(MonthlyCalendarDayView dayView : lstCalendarDay){
				dayView.removeAllData();
			}

			if(!CCCollectionUtil.isEmpty(lstSchedule)){
				// Todo Trung: test in here
				// for(ScheduleModel scheduleModel : lstSchedule){
				// lstCalendarRow.get(4).addSchedule(scheduleModel);
				// }
				// lstCalendarRow.get(4).refreshLayout();

				Collections.sort(lstSchedule, new ScheduleComparator());
				for(ScheduleModel model : lstSchedule){
					if(model.isPeriodSchedule()){
                        for(MonthlyCalendarRowView rowView : lstCalendarRow){
                            String minDay = rowView.lstCalendarDay.get(0).day;
                            String maxDay = rowView.lstCalendarDay.get(rowView.lstCalendarDay.size() - 1).day;
                            if(ClUtil.belongPeriod(WelfareUtil.makeDate(model.startDate), minDay, maxDay) || ClUtil.belongPeriod(WelfareUtil.makeDate(model.endDate), minDay, maxDay)){
                                rowView.addSchedule(model);
                            }
                        }
					}else{
						List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, model.startDate, model.endDate);

						for(MonthlyCalendarDayView calendarDayView : lstActiveCalendarDay){
							calendarDayView.addSchedule(model);
						}
					}
				}
			}

			// add holiday
			if(!CCCollectionUtil.isEmpty(lstHoliday)){
				for(HolidayModel holidayModel : lstHoliday){
					List<MonthlyCalendarDayView> lstActiveCalendarDay = ClUtil.findView4Day(lstCalendarDay, holidayModel.startDate, holidayModel.endDate);
					ScheduleModel scheduleModel = new ScheduleModel(holidayModel);
					for(MonthlyCalendarDayView calendarDayView : lstActiveCalendarDay){
						calendarDayView.addSchedule(scheduleModel);
					}
				}
			}

            for(MonthlyCalendarRowView rowView : lstCalendarRow){
                rowView.refreshLayout();
            }

			List<UserModel> lstCalendarUser = CCJsonUtil.convertToModelList(response.optString("calendarUsers"), UserModel.class);
			if(changeCalendarUserListener != null){
				changeCalendarUserListener.onChangeCalendarUserListener(lstCalendarUser);
			}

			// make daily summary dialog
			initDialog();
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onDailyScheduleClickListener(String day){
		dialogDailySummary.setActiveDate(CCDateUtil.makeDateCustom(day, WelfareConst.WL_DATE_TIME_7));
		dialogDailySummary.show();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_add:
			dialogDailySummary.dismiss();
			((WelfareActivity)activity).addFragment(new ScheduleFormFragment());
			break;
		default:
			break;
		}
	}
}
