package trente.asia.dailyreport.services.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.report.model.Holiday;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.model.WorkingSymbolModel;
import trente.asia.dailyreport.services.report.view.DRCalendarView;
import trente.asia.dailyreport.services.report.view.MyReportListAdapter;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.dailyreport.view.DRCalendarHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class MyReportFragment extends AbstractDRFragment implements DRCalendarView.OnReportModelSelectedListener{

	private static final String	ACTUAL_PLAN_EDT_ID	= "edt_actual_plan_";
	public static int			VIEW_AS_CALENDAR	= 1;
	public static int			VIEW_AS_LIST		= 2;

	private ListView			lstReports;
	private DRCalendarView		drCalendarView;
	private DRCalendarHeader	calendarHeader;
	private ScrollView			mScrCalendarView;

	private List<ReportModel>	reports;
	private List<Holiday>		holidays;
	private TextView			txtDate;
	private LayoutInflater		inflater;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_my_report;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_myreport;
	}

	@Override
	public void initData(){
		requestDailyReportSingle(calendarHeader.getSelectedDate());
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_title_empty), null);

		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		inflater = LayoutInflater.from(activity);
		calendarHeader = (DRCalendarHeader)getView().findViewById(R.id.lnr_calendar_header);
		Calendar c = Calendar.getInstance();
		txtDate = (TextView)getView().findViewById(R.id.fragment_txt_calendar_header_date);
		calendarHeader.setStepType(DRCalendarHeader.STEP_TYPE_MONTH);
		calendarHeader.buildLayout(1970, 1, c.get(Calendar.YEAR) + 1, 12, Calendar.getInstance().getTime(), new DRCalendarHeader.OnViewChangeListener() {

			@Override
			public void viewAsList(){
				switchView(VIEW_AS_LIST);
			}

			@Override
			public void viewAsCalendar(){
				switchView(VIEW_AS_CALENDAR);
			}
		}, new DRCalendarHeader.OnTimeChangeListener() {

			@Override
			public void onTimeChange(Date newSelectedDate){
				String selectedYearMonthStr = calendarHeader.getSelectedYearMonthStr();
				txtDate.setText(selectedYearMonthStr);
				requestDailyReportSingle(newSelectedDate);
			}
		}, getString(R.string.header_calendar_this_month));
		String selectedYearMonthStr = calendarHeader.getSelectedYearMonthStr();
		txtDate.setText(selectedYearMonthStr);
		lstReports = (ListView)getView().findViewById(R.id.lst_my_report_fragment);

		mScrCalendarView = (ScrollView)getView().findViewById(R.id.src_id_calendar_view);
		drCalendarView = (DRCalendarView)getView().findViewById(R.id.my_report_fragment_calendar_view);
		drCalendarView.setOnReportModelSelectedListener(this);
	}

	public static void gotoReportDetailFragment(Activity activity, ReportModel reportModel){
		ReportDetailFragment reportDetailFragment = new ReportDetailFragment();
		reportDetailFragment.setReportModel(reportModel);
		((WelfareActivity)activity).addFragment(reportDetailFragment);
	}

	private void switchView(int viewType){
		if(viewType == VIEW_AS_CALENDAR){
			lstReports.setVisibility(View.GONE);
			mScrCalendarView.setVisibility(View.VISIBLE);
		}else{
			mScrCalendarView.setVisibility(View.GONE);
			lstReports.setVisibility(View.VISIBLE);
		}
	}

	private void requestDailyReportSingle(Date date){
		String monthStr = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, date);
		UserModel userMe = prefAccUtil.getUserPref();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", userMe.key);
			jsonObject.put("targetDeptId", userMe.dept.key);
			jsonObject.put("targetMonth", monthStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_REPORT_LIST_MYSELF, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			reports = CCJsonUtil.convertToModelList(response.optString("reports"), ReportModel.class);
			reports = addReportsForEmptyDays(reports);

			WorkingSymbolModel workingSymbolModel = CCJsonUtil.convertToModel(response.optString("working"), WorkingSymbolModel.class);
			List<WorkingSymbolModel> workingSymbolModels = new ArrayList<>();
			workingSymbolModels.add(workingSymbolModel);
			appendWorkingSymbol(reports, workingSymbolModels);

			holidays = CCJsonUtil.convertToModelList(response.optString("holidays"), Holiday.class);
			appendHolidayInfo(reports, holidays);
			updateHeader(prefAccUtil.getUserPref().userName);
			buildCalendarView(reports);
			buildListView(reports);
		}
	}

	public static void appendWorkingSymbol(List<ReportModel> reports, List<WorkingSymbolModel> workingSymbolModels){
		Map<String, Map<String, String>> workingSymbolMap = buildWorkingSymbolMap(workingSymbolModels);
		for(ReportModel reportModel : reports){
			String reportUserId = reportModel.reportUser != null ? reportModel.reportUser.key : reportModel.loginUserId;
			Map<String, String> userWorkingSymbolMap = workingSymbolMap.get(reportUserId);
			if(userWorkingSymbolMap != null){
				String reportDateKey = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_CODE, CCDateUtil.makeDateCustom(reportModel.reportDate, WelfareConst.WF_DATE_TIME));
				reportModel.workingSymbol = userWorkingSymbolMap.get(reportDateKey);
			}
		}
	}

	public static Map<String, Map<String, String>> buildWorkingSymbolMap(List<WorkingSymbolModel> workingSymbolModels){
		Map<String, Map<String, String>> result = new HashMap<>();
		if(!CCCollectionUtil.isEmpty(workingSymbolModels)){
			for(WorkingSymbolModel workingSymbolModel : workingSymbolModels){
				if(workingSymbolModel != null && !CCCollectionUtil.isEmpty(workingSymbolModel.symbols)){
					Map<String, String> dateSymbolMap = result.get(workingSymbolModel.userId);
					if(dateSymbolMap == null){
						dateSymbolMap = new HashMap<>();
					}
					for(ApiObjectModel apiObjectModel : workingSymbolModel.symbols){
						dateSymbolMap.put(apiObjectModel.key, apiObjectModel.value);
					}
					result.put(workingSymbolModel.userId, dateSymbolMap);
				}
			}
		}
		return result;
	}

	/*
	 * If there is no report on date 2016/04/05, add an empty report to it
	 * @return reports: a month full of report
	 */
	private List<ReportModel> addReportsForEmptyDays(List<ReportModel> reports){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, calendarHeader.getSelectedYear());
		c.set(Calendar.MONTH, calendarHeader.getSelectedMonth());
		List<ReportModel> results = new ArrayList<>();
		for(int i = c.getActualMinimum(Calendar.DAY_OF_MONTH); i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
			c.set(Calendar.DAY_OF_MONTH, i);
			ReportModel reportModel = getReportByDay(c, reports);
			reportModel.reportUser = myself;
			results.add(reportModel);
		}
		return results;
	}

	public static ReportModel createEmptyReportModel(Calendar c, UserModel reportUser){
		ReportModel reportModel = new ReportModel();
		reportModel.reportDate = CCFormatUtil.formatDateCustom(DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, c.getTime());
		reportModel.reportUser = reportUser;
		reportModel.checks = new ArrayList<>();
		reportModel.comments = new ArrayList<>();
		reportModel.likes = new ArrayList<>();
		return reportModel;
	}

	/*
	 * @return ReportModel if no report found at date X, return an empty report
	 */
	public static ReportModel getReportByDay(Calendar c, List<ReportModel> reportModels){
		for(ReportModel reportModel : reportModels){
			String day = DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD);
			if(day.equals(DRUtil.getDateString(c.getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD))){
				return reportModel;
			}
		}
		return createEmptyReportModel(c, null);
	}

	/*
	 * @return ReportModel if no report found at date X, return an empty report
	 */
	public static ReportModel getReportByDayAndUser(Calendar c, List<ReportModel> filteredReports, UserModel user){
		for(ReportModel reportModel : filteredReports){
			if(reportModel.reportUser.key.equals(user.key)){
				String day = DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD);
				if(day.equals(DRUtil.getDateString(c.getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD))){
					return reportModel;
				}
			}
		}
		return createEmptyReportModel(c, user);
	}

	public static void appendHolidayInfo(List<ReportModel> reportModels, List<Holiday> holidays){
		if(reportModels != null && holidays != null){
			for(ReportModel reportModel : reportModels){
				appendHolidayInfo(reportModel, holidays);
			}
		}
	}

	public static void appendHolidayInfo(ReportModel reportModel, List<Holiday> holidays){
		String dateStr = DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD);

		for(Holiday holiday : holidays){
			if(holiday.key.equals(dateStr)){
				reportModel.holiday = holiday;
				break;
			}
		}
	}

	private void buildListView(final List<ReportModel> reports){
		MyReportListAdapter myReportListAdapter = new MyReportListAdapter(activity, R.layout.item_my_report, reports);
		lstReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
				ReportModel reportModel = reports.get(i);
				onReportModelSelected(reportModel);
			}
		});
		lstReports.setAdapter(myReportListAdapter);
	}

	private void buildCalendarView(List<ReportModel> reports){
		drCalendarView.updateLayout(activity, calendarHeader.getSelectedYear(), calendarHeader.getSelectedMonth(), reports);
	}

	@Override
	public void onReportModelSelected(ReportModel reportModel){
		if(CCStringUtil.isEmpty(reportModel.key)){// create new
			gotoReportEditFragment(activity, reportModel);
		}else{
			if(ReportModel.REPORT_STATUS_DRTT.equals(reportModel.reportStatus)){
				gotoReportEditFragment(activity, reportModel);
			}else if(ReportModel.REPORT_STATUS_DONE.equals(reportModel.reportStatus)){
				gotoReportDetailFragment(activity, reportModel);
			}
		}
	}

	public static void gotoReportEditFragment(Activity activity, ReportModel reportModel){
		ReportEditFragment reportEditFragment = new ReportEditFragment();
		reportEditFragment.setReportModel(reportModel);
		((WelfareActivity)activity).addFragment(reportEditFragment);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		lstReports = null;
		drCalendarView = null;
		calendarHeader = null;
		txtDate = null;
	}
}
