package trente.asia.dailyreport.services.other;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsDateUtil;
import trente.asia.dailyreport.BuildConfig;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.other.view.OtherReportListAdapter;
import trente.asia.dailyreport.services.report.MyReportFragment;
import trente.asia.dailyreport.services.report.model.DRDeptModel;
import trente.asia.dailyreport.services.report.model.DRUserModel;
import trente.asia.dailyreport.services.report.model.Holiday;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.model.WorkingSymbolModel;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.dailyreport.view.DRCalendarHeader;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfProfileDialog;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * Created by viet on 2/15/2016.
 */
public class OthersFragment extends AbstractDRFragment{

	private ListView							lstReports;
	private LinearLayout						lnrReports;
	private DRCalendarHeader					calendarHeader;
	private List<ReportModel>					filteredReports;
	final private static int					FIRST_DAY			= Calendar.MONDAY;

	private List<DRUserModel>					lstUser				= new ArrayList<>();	// user list for
	// calendar view
	private List<Date>							lstDate;
	private LinearLayout						mLnrUser;
	// private LinearLayout mLnrReport;
	private LinearLayout						mLnrReportHeader;
	private LinearLayout						mLnrReportContent;
	// HorizontalScrollView horizontalScrollViewHeader;
	HorizontalScrollView						horizontalScrollViewContent;
	ViewTreeObserver.OnScrollChangedListener	listenerForHeader;
	ViewTreeObserver.OnScrollChangedListener	listenerForContent;
	LayoutInflater								inflater;

	private WfSpinner							wfSpinnerDept;
	// private WfSpinner wfSpinnerUser;
	private List<DRDeptModel>					drDeptModels;								// dept list for spinner
	private List<DeptModel>						deptModels;									// dept list for spinner

	private DRDeptModel							selectedDept;
	// private DRUserModel selectedUser;
	private boolean								changedFragment		= false;				// todo don't know why deptSpinner
	private List<Holiday>						holidays;
	private List<DRUserModel>					drUserModels;
	private boolean								deptSpinnerInited	= false;
	private TextView							txtDate;
	private WfProfileDialog						mDlgProfile;
	private Date								selectedDate;
	private Map<String, Map<String, String>>	workingSymbolMap;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_others_report;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_others;
	}

	@Override
	public void initData(){
		deptSpinnerInited = false;
		changedFragment = false;

		// 2017.04.02 Tak set default dept
		UserModel userModel = prefAccUtil.getUserPref();
		selectedDept = new DRDeptModel(userModel.getDept().getKey(), userModel.getDept().getDeptName());

		requestDailyReportAllUser();
	}

	private void initDynamicData(){
		lstUser = new ArrayList<>();
		// if(selectedUser != null){
		// if(!selectedUser.key.equals(DRUserModel.KEY_ALL)){
		// lstUser.add(selectedUser);
		// }else{ // select all user
		// if(selectedDept != null){
		// lstUser.addAll(selectedDept.users);
		// }
		// }
		// }
		lstUser.addAll(selectedDept.users);
		// Delete special user: All_USER
		Iterator<DRUserModel> drUserModelIterator = lstUser.iterator();
		while(drUserModelIterator.hasNext()){
			if(drUserModelIterator.next().key.equals(DRUserModel.KEY_ALL)){
				drUserModelIterator.remove();
			}
		}
	}

	private void buildCalendarViewHeader(){
		Calendar c = Calendar.getInstance();
		mLnrReportHeader.removeAllViews();

		Date selectedDate = calendarHeader.getSelectedDate();
		List<Date> dates = CsDateUtil.getAllDatesOfWeek(CCDateUtil.makeCalendar(selectedDate), FIRST_DAY);

		lstDate = CCDateUtil.makeDateList(dates.get(0), dates.get(dates.size() - 1));

		for(Date date : lstDate){
			View dateView = LayoutInflater.from(activity).inflate(R.layout.item_calendar_other_header, null);
			buildCalendarCellHeader(date, dateView, c);
			dateView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
			mLnrReportHeader.addView(dateView);
		}
		mLnrReportHeader.setBackgroundColor(ContextCompat.getColor(activity, R.color.core_gray));
	}

	private void buildCalendarCellHeader(Date date, View dateView, Calendar calendarNow){
		LinearLayout lnrContent = (LinearLayout)dateView.findViewById(R.id.item_calendar_other_main_content);
		TextView txtDate = (TextView)dateView.findViewById(R.id.item_other_txt_date);
		TextView txtDay = (TextView)dateView.findViewById(R.id.item_other_txt_day);
		String dateStr = DRUtil.getDateString(date, DRConst.DATE_FORMAT_DD);
		String dayStr = getDayString(date, activity);
		txtDate.setText(dateStr);
		txtDay.setText(dayStr);

		// Build background
		if(checkHoliday(date)){// holiday
			lnrContent.setBackgroundColor(ContextCompat.getColor(activity, R.color.dr_bg_holiday));
			txtDate.setTextColor(ContextCompat.getColor(activity, R.color.dr_day_color_sun));
			txtDay.setTextColor(ContextCompat.getColor(activity, R.color.dr_day_color_sun));
		}else if(DRUtil.getDateString(calendarNow.getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD).equals(DRUtil.getDateString(date, DRConst.DATE_FORMAT_YYYY_MM_DD))){// today
			lnrContent.setBackgroundResource(R.drawable.dr_item_calendar_background_normal_day);
			txtDate.setTextColor(ContextCompat.getColor(activity, R.color.core_white));
			txtDate.setBackgroundResource(R.drawable.dr_background_base_color_circle);
		}else if(dayStr.equals(getString(R.string.dlr_sunday))){
			lnrContent.setBackgroundColor(ContextCompat.getColor(activity, R.color.dr_bg_sunday));
			txtDate.setTextColor(ContextCompat.getColor(activity, R.color.dr_day_color_sun));
			txtDay.setTextColor(ContextCompat.getColor(activity, R.color.dr_day_color_sun));
		}else if(dayStr.equals(getString(R.string.dlr_saturday))){
			lnrContent.setBackgroundColor(ContextCompat.getColor(activity, R.color.dr_bg_saturday));
			txtDate.setTextColor(ContextCompat.getColor(activity, R.color.dr_text_day_color_sat));
			txtDay.setTextColor(ContextCompat.getColor(activity, R.color.dr_text_day_color_sat));
		}else{// normal day
			lnrContent.setBackgroundColor(ContextCompat.getColor(activity, R.color.dr_bg_normal_day));
		}
	}

	public static String getDayString(Date date, Context context){
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		switch(c.get(Calendar.DAY_OF_WEEK)){
		case Calendar.MONDAY:
			return context.getResources().getString(R.string.dlr_monday);
		case Calendar.TUESDAY:
			return context.getResources().getString(R.string.dlr_tue);
		case Calendar.WEDNESDAY:
			return context.getResources().getString(R.string.dlr_wed);
		case Calendar.THURSDAY:
			return context.getResources().getString(R.string.dlr_thu);
		case Calendar.FRIDAY:
			return context.getResources().getString(R.string.dlr_fri);
		case Calendar.SATURDAY:
			return context.getResources().getString(R.string.dlr_saturday);
		case Calendar.SUNDAY:
			return context.getResources().getString(R.string.dlr_sunday);
		}
		return null;
	}

	private void buildCalendarCellImage(Date date, ReportModel reportModel, View dateView, Calendar c){
		LinearLayout lnrContent = (LinearLayout)dateView.findViewById(R.id.item_calendar_other_main_content);
		String dayStr = DRUtil.getDateString(date, "EEE");

		// Build background
		if(reportModel.holiday != null || checkHoliday(date)){// holiday
			lnrContent.setBackgroundResource(R.drawable.dr_item_calendar_background_holiday);
		}else if(DRUtil.getDateString(c.getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD).equals(DRUtil.getDateString(date, DRConst.DATE_FORMAT_YYYY_MM_DD))){//
			lnrContent.setBackgroundResource(R.drawable.dr_item_calendar_background_normal_day);
		}else if(dayStr.contains("Sun")){
			lnrContent.setBackgroundResource(R.drawable.dr_item_calendar_background_sun);
		}else if(dayStr.contains("Sat")){
			lnrContent.setBackgroundResource(R.drawable.dr_item_calendar_background_sat);
		}else{
			lnrContent.setBackgroundResource(R.drawable.dr_item_calendar_background_normal_day);
		}
	}

	private boolean checkHoliday(Date date){
		for(Holiday holiday : holidays){
			if(holiday.key.equals(DRUtil.getDateString(date, DRConst.DATE_FORMAT_YYYY_MM_DD))){
				return true;
			}
		}
		return false;
	}

	private void addCalendarCellUser(LayoutInflater inflater, final UserModel user){
		View userView = inflater.inflate(R.layout.item_calendar_other_user, null);
		((LinearLayout)userView.findViewById(R.id.view_user_other_repport_lnr_content)).setBackgroundColor(ContextCompat.getColor(activity, R.color.core_white));
		SelectableRoundedImageView avatar = (SelectableRoundedImageView)userView.findViewById(R.id.img_id_avatar);
		TextView txtName = (TextView)userView.findViewById(R.id.txt_id_name);
		if(user != null){
			WfPicassoHelper.loadImage2(activity, host, avatar, user.avatarPath);
			avatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					mDlgProfile.show(BuildConfig.HOST, user.userName, user.avatarPath);
				}
			});
			txtName.setText(user.userName);
		}else{
			avatar.setVisibility(View.INVISIBLE);
			txtName.setVisibility(View.INVISIBLE);
		}

		mLnrUser.addView(userView);
	}

	private void buildCalendarViewContent(Calendar cToday){
		Calendar c = Calendar.getInstance();
		for(final UserModel user : lstUser){
			addCalendarCellUser(inflater, user);
			LinearLayout reportRow = new LinearLayout(activity);
			reportRow.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			reportRow.setLayoutParams(LLParams);
			Map<String, String> userWorkingSymbolMap = workingSymbolMap.get(user.key);
			for(Date date : lstDate){
				c.setTime(date);
				final ReportModel reportModel = MyReportFragment.getReportByDayAndUser(c, filteredReports, user);
				MyReportFragment.appendHolidayInfo(reportModel, holidays);
				View cell;
				boolean clickable = clickAble(reportModel);
				if(clickable){
					cell = inflater.inflate(R.layout.item_calendar_other, null);
					cell.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View view){
							onReportModelSelected(reportModel);
						}
					});
				}else{
					cell = inflater.inflate(R.layout.item_calendar_other_empty, null);

					if(userWorkingSymbolMap != null){
						String reportDateKey = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_CODE, date);
						String workingSymbol = userWorkingSymbolMap.get(reportDateKey);
						if(!CCStringUtil.isEmpty(workingSymbol)){
							ImageView imgSymbol = (ImageView)cell.findViewById(R.id.item_other_img_status);
							WfPicassoHelper.loadImage2(activity, BuildConfig.HOST, imgSymbol, workingSymbol);
						}
					}
				}
				cell.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
				buildCalendarCellImage(date, reportModel, cell, cToday);

				reportRow.addView(cell);
			}
			mLnrReportContent.addView(reportRow);
		}
		// scrollToToday();
	}

	// private void scrollToToday(){
	// final Calendar c = Calendar.getInstance();
	// if(calendarHeader.getSelectedMonth() == c.get(Calendar.MONTH) + 1){
	// horizontalScrollViewHeader.postDelayed(new Runnable() {
	//
	// public void run(){
	// final int todayPosition = (c.get(Calendar.DAY_OF_MONTH) - 2) * horizontalScrollViewHeader.getChildAt(0).getRight() /
	// c.getActualMaximum(Calendar.DAY_OF_MONTH);
	// horizontalScrollViewHeader.smoothScrollTo(todayPosition, 0);
	// }
	// }, 200);
	// }else{
	// horizontalScrollViewHeader.smoothScrollTo(0, 0);
	// }
	// }

	// aaaa
	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_other_title), null);

		inflater = LayoutInflater.from(activity);
		calendarHeader = (DRCalendarHeader)getView().findViewById(R.id.lnr_calendar_header);
		Calendar c = Calendar.getInstance();
		txtDate = (TextView)getView().findViewById(R.id.fragment_txt_calendar_header_date);
		calendarHeader.setStepType(DRCalendarHeader.STEP_TYPE_WEEK);
		calendarHeader.buildLayout(1970, 1, c.get(Calendar.YEAR) + 1, 12, Calendar.getInstance().getTime(), new DRCalendarHeader.OnViewChangeListener() {

			@Override
			public void viewAsList(){
				switchView(MyReportFragment.VIEW_AS_LIST);
			}

			@Override
			public void viewAsCalendar(){
				switchView(MyReportFragment.VIEW_AS_CALENDAR);
			}
		}, new DRCalendarHeader.OnTimeChangeListener() {

			@Override
			public void onTimeChange(Date newSelectedDate){
				selectedDate = newSelectedDate;
				txtDate.setText(calendarHeader.getSelectedYearMonthStr());
				requestDailyReportAllUser();
			}
		}, getString(R.string.header_calendar_this_week));
		txtDate.setText(calendarHeader.getSelectedYearMonthStr());
		lstReports = (ListView)getView().findViewById(R.id.fragment_other_report_list);
		lnrReports = (LinearLayout)getView().findViewById(R.id.fragment_other_report_calandar_view);

		mLnrUser = (LinearLayout)getView().findViewById(R.id.lnr_id_user);
		// mLnrReport = (LinearLayout) getView().findViewById(R.id
		// .lnr_id_report);
		mLnrReportHeader = (LinearLayout)getView().findViewById(R.id.fragment_others_report_header);
		mLnrReportContent = (LinearLayout)getView().findViewById(R.id.fragment_others_report_content);

		// horizontalScrollViewHeader = (HorizontalScrollView)getView().findViewById(R.id.horizontalScrollViewHeader);
		// horizontalScrollViewContent = (HorizontalScrollView)getView().findViewById(R.id.horizontalScrollViewContent);
		//
		// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
		// horizontalScrollViewHeader.setOnScrollChangeListener(new View.OnScrollChangeListener() {
		//
		// @Override
		// public void onScrollChange(View view, int i, int i1, int i2, int i3){
		// horizontalScrollViewContent.scrollTo(i, i1);
		// }
		// });
		//
		// horizontalScrollViewContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
		//
		// @Override
		// public void onScrollChange(View view, int i, int i1, int i2, int i3){
		// horizontalScrollViewHeader.scrollTo(i, i1);
		// }
		// });
		//
		// }else{
		// listenerForHeader = new ViewTreeObserver.OnScrollChangedListener() {
		//
		// @Override
		// public void onScrollChanged(){
		// int scrollX = horizontalScrollViewHeader.getScrollX();
		// horizontalScrollViewContent.getViewTreeObserver().removeOnScrollChangedListener(listenerForContent);
		// horizontalScrollViewContent.scrollTo(scrollX, 0);
		// horizontalScrollViewContent.getViewTreeObserver().addOnScrollChangedListener(listenerForContent);
		// }
		// };
		//
		// listenerForContent = new ViewTreeObserver.OnScrollChangedListener() {
		//
		// @Override
		// public void onScrollChanged(){
		// int scrollX = horizontalScrollViewContent.getScrollX();
		// horizontalScrollViewHeader.getViewTreeObserver().removeOnScrollChangedListener(listenerForHeader);
		// horizontalScrollViewHeader.scrollTo(scrollX, 0);
		// horizontalScrollViewHeader.getViewTreeObserver().addOnScrollChangedListener(listenerForHeader);
		// }
		// };
		//
		// horizontalScrollViewHeader.getViewTreeObserver().addOnScrollChangedListener(listenerForHeader);
		//
		// horizontalScrollViewContent.getViewTreeObserver().addOnScrollChangedListener(listenerForContent);
		// }

		wfSpinnerDept = (WfSpinner)getView().findViewById(R.id.fragment_other_report_spn_dept);
		// wfSpinnerUser = (WfSpinner)getView().findViewById(R.id.fragment_other_report_spn_user);
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);
	}

	private void switchView(int viewType){
		if(viewType == MyReportFragment.VIEW_AS_CALENDAR){
			lstReports.setVisibility(View.GONE);
			lnrReports.setVisibility(View.VISIBLE);
		}else{
			lnrReports.setVisibility(View.GONE);
			lstReports.setVisibility(View.VISIBLE);
		}
	}

	private void requestDailyReportAllUser(){

		if(selectedDate == null){
			Calendar c = Calendar.getInstance();
			selectedDate = c.getTime();
		}
		List<Date> dates = CsDateUtil.getAllDatesOfWeek(CCDateUtil.makeCalendar(selectedDate), FIRST_DAY);

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetDeptId", selectedDept == null || DRDeptModel.KEY_ALL.equals(selectedDept.key) ? null : selectedDept.key);
			// jsonObject.put("targetMonth", monthStr);
			jsonObject.put("searchStart", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, dates.get(0)));
			jsonObject.put("searchEnd", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, dates.get(dates.size() - 1)));

		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_REPORT_LIST_OTHERS, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			holidays = CCJsonUtil.convertToModelList(response.optString("holidays"), Holiday.class);
			drUserModels = CCJsonUtil.convertToModelList(response.optString("reportByUsers"), DRUserModel.class);
			List<WorkingSymbolModel> workingSymbolModels = CCJsonUtil.convertToModelList(response.optString("workingByUsers"), WorkingSymbolModel.class);
			workingSymbolMap = MyReportFragment.buildWorkingSymbolMap(workingSymbolModels);

			deptModels = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			buildReports();
			if(deptSpinnerInited == false){
				deptSpinnerInited = true;
				buildDeptSpinner();
			}
			buildCalendarViewHeader();
			updateLayout();
		}
	}

	private void buildDeptSpinner(){
		wfSpinnerDept.setupLayout(getString(R.string.spinner_name_dept), getDepartmentDisplayedValue(drDeptModels), getSelectedDeptPosition(), new WfSpinner.OnDRSpinnerItemSelectedListener() {

			@Override
			public void onItemSelected(int selectedPosition){
				if(changedFragment){
					// selectedPosition = wfSpinnerDept.getPositionOf(selectedDept.deptName);
					wfSpinnerDept.setSelectedPosition(selectedPosition);
					onDeptSelected(selectedPosition);
					changedFragment = false;
				}else{
					onDeptSelected(selectedPosition);
				}
			}
		}, false);
	}

	private int getSelectedDeptPosition(){
		if(drDeptModels == null || drDeptModels.size() == 0 || selectedDept == null){
			return 0;
		}
		for(int i = 0; i < drDeptModels.size(); i++){
			if(drDeptModels.get(i).key.equals(selectedDept.key)){
				return i;
			}
		}
		return 0;
	}

	private void buildReports(){
		drDeptModels = new ArrayList<>();
		for(DeptModel deptModel : deptModels){
			DRDeptModel drDeptModel = getDRDeptModel(deptModel);
			drDeptModels.add(drDeptModel);
		}
		// if(drDeptModels.size() > 1){
		// appendDepartmentDeptAll();
		// }
		selectedDept = getSelectedDepth(drDeptModels, selectedDept);
		appendUserUserAll();
	}

	private void appendDepartmentDeptAll(){
		DRDeptModel deptAll = new DRDeptModel(DRDeptModel.KEY_ALL, getString(R.string.str_all));
		List<DRUserModel> drUserModels = new ArrayList<>();
		for(DRDeptModel drDeptModel : drDeptModels){
			for(DRUserModel drUserModel : drDeptModel.users){
				addUserToListIfNotExist(drUserModels, drUserModel);
			}
		}
		deptAll.users = drUserModels;
		drDeptModels.add(0, deptAll);
	}

	private DRDeptModel getDRDeptModel(DeptModel deptModel){
		DRDeptModel drDeptModelResult = new DRDeptModel(deptModel.key, deptModel.deptName);
		List<DRUserModel> drUserModels = new ArrayList<>();
		for(UserModel userModel : deptModel.members){
			DRUserModel drUserModel = getDRUserModel(userModel);
			drUserModels.add(drUserModel);
		}
		drDeptModelResult.users = drUserModels;
		return drDeptModelResult;
	}

	private DRUserModel getDRUserModel(UserModel userModel){
		for(DRUserModel drUserModel : drUserModels){
			if(userModel.key.equals(drUserModel.key)){
				return drUserModel;
			}
		}
		DRUserModel drUserModel = new DRUserModel(userModel.key, userModel.userName);
		drUserModel.reports = new ArrayList<>();
		return drUserModel;
	}

	private void appendUserUserAll(){
		DRUserModel userAll = new DRUserModel(DRUserModel.KEY_ALL, getString(R.string.str_all));
		userAll.reports = new ArrayList<>();
		for(DRDeptModel dept : drDeptModels){
			// if (dept.users.size() > 1) {
			dept.users.add(0, userAll);
			// }
		}
	}

	private DRDeptModel getSelectedDepth(List<DRDeptModel> depts, DRDeptModel previous){
		if(previous == null || depts.size() <= 0){
			return null;
		}
		for(DRDeptModel dept : depts){
			if(dept.key.equals(previous.key)){
				return dept;
			}
		}
		return null;
	}

	private void addUserToListIfNotExist(List<DRUserModel> users, DRUserModel userModel){
		for(DRUserModel user : users){
			if(user.key.equals(userModel.key)){
				return;
			}
		}
		users.add(userModel);
	}

	private void onDeptSelected(int selectedPosition){
		DRDeptModel newSelectedDept = drDeptModels.get(selectedPosition);
		if(selectedDept == null){
			selectedDept = newSelectedDept;
			// updateUserSpinner();
			updateLayout();
		}else{
			selectedDept = newSelectedDept;
			requestDailyReportAllUser();
		}
	}

	// private void updateUserSpinner(){
	// selectedUser = getSelectedUser(selectedDept.users, selectedUser);
	// wfSpinnerUser.setupLayout(getString(R.string.spinner_name_user), getUserDisplayedValue(selectedDept.users), getSelectedUserPosition(), new
	// WfSpinner.OnDRSpinnerItemSelectedListener() {
	//
	// @Override
	// public void onItemSelected(int selectedPosition){
	// selectedUser = selectedDept.users.get(selectedPosition);
	// updateLayout();
	// }
	// }, true);
	// if(selectedDept.users == null || selectedDept.users.size() == 0){
	// selectedUser = null;
	// updateLayout();
	// }
	// }

	// private int getSelectedUserPosition(){
	// if(drUserModels == null || drUserModels.size() == 0 || selectedUser == null){
	// return 0;
	// }
	// for(int i = 0; i < drUserModels.size(); i++){
	// if(drUserModels.get(i).key.equals(selectedUser.key)){
	// return i;
	// }
	// }
	// return 0;
	// }

	private void updateLayout(){
		filteredReportsByDept();
		buildCalendarView(filteredReports);
		buildListView(filteredReports);
	}

	private void filteredReportsByDept(){
		filteredReports = new ArrayList<>();
		if(selectedDept != null){
			for(DRDeptModel deptModel : drDeptModels){
				for(DRUserModel userModel : deptModel.users){
					if(userModel.reports != null){
						for(ReportModel reportModel : userModel.reports){
							if(ReportModel.REPORT_STATUS_DONE.equals(reportModel.reportStatus)){
								if(DRDeptModel.KEY_ALL.equals(selectedDept.key) || (!DRDeptModel.KEY_ALL.equals(selectedDept.key) && reportModel.reportUser.dept.key.equals(selectedDept.key))){
									addReportToListIfNotExist(filteredReports, reportModel);
								}
							}
						}
					}
				}
			}
		}
		sortReports();
	}

	private void addReportToListIfNotExist(List<ReportModel> reportModels, ReportModel reportModel){
		for(ReportModel report : reportModels){
			if(report.key.equals(reportModel.key)) return;
		}
		reportModels.add(reportModel);
	}

	private void sortReports(){
		Collections.sort(filteredReports, new Comparator<ReportModel>() {

			public int compare(ReportModel r1, ReportModel r2){
				int dateCompareResult = CCDateUtil.compareDate(CCDateUtil.convertStringToDate(r1.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), CCDateUtil.convertStringToDate(r2.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS), false);
				if(dateCompareResult == -1){
					return -1;
				}else if(dateCompareResult == 1){
					return 1;
				}
				return r1.reportUser.userName.compareTo(r2.reportUser.userName);
			}
		});
	}

	// private DRUserModel getSelectedUser(List<DRUserModel> users, DRUserModel previous){
	// if(previous == null || users == null || users.size() <= 0){
	// return null;
	// }
	// for(DRUserModel userModel : users){
	// if(userModel.key.equals(previous.key)){
	// return userModel;
	// }
	// }
	// return null;
	// }

	private List<String> getDepartmentDisplayedValue(List<DRDeptModel> drDeptModels){
		List<String> values = new ArrayList<>();
		for(DRDeptModel drDeptModel : drDeptModels){
			values.add(drDeptModel.deptName);
		}
		return values;
	}

	private List<String> getUserDisplayedValue(List<DRUserModel> userModels){
		List<String> values = new ArrayList<>();
		if(userModels != null){
			for(DRUserModel userModel : userModels){
				values.add(userModel.userName);
			}
		}
		return values;
	}

	private void buildListView(final List<ReportModel> filteredReports){
		OtherReportListAdapter otherReportListAdapter = new OtherReportListAdapter(activity, R.layout.item_other_report, filteredReports, new OnAvatarClickListener() {

			@Override
			public void OnAvatarClick(String userName, String avatarPath){
				mDlgProfile.show(BuildConfig.HOST, userName, avatarPath);
			}
		});
		lstReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				ReportModel reportModel = filteredReports.get(i);
				onReportModelSelected(reportModel);
			}
		});
		lstReports.setAdapter(otherReportListAdapter);
	}

	private void buildCalendarView(List<ReportModel> filteredReports){
		initDynamicData();
		mLnrUser.removeAllViews();
		mLnrReportContent.removeAllViews();
		Calendar c = Calendar.getInstance();
		buildCalendarViewContent(c);
	}

	public void onReportModelSelected(ReportModel reportModel){
		if(CCStringUtil.isEmpty(reportModel.key)){// create new
			MyReportFragment.gotoReportEditFragment(activity, reportModel);
		}else{
			if(ReportModel.REPORT_STATUS_DRTT.equals(reportModel.reportStatus)){
				MyReportFragment.gotoReportEditFragment(activity, reportModel);
			}else if(ReportModel.REPORT_STATUS_DONE.equals(reportModel.reportStatus)){
				changedFragment = true;
				MyReportFragment.gotoReportDetailFragment(activity, reportModel);
			}
		}
	}

	public boolean clickAble(ReportModel reportModel){
		return ReportModel.REPORT_STATUS_DONE.equals(reportModel.reportStatus);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		lstReports = null;
		lnrReports = null;
		calendarHeader = null;
		filteredReports = null;

		lstUser = null;
		lstDate = null;
		mLnrUser = null;
		mLnrReportHeader = null;
		mLnrReportContent = null;

		wfSpinnerDept = null;
		// wfSpinnerUser = null;
		drDeptModels = null;
		deptModels = null;

		selectedDept = null;
		// selectedUser = null;
		holidays = null;
		drUserModels = null;
		txtDate = null;
	}

}
