package trente.asia.dailyreport.services.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.report.model.GoalEntry;
import trente.asia.dailyreport.services.report.model.Holiday;
import trente.asia.dailyreport.services.report.model.Kpi;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.DRCalendarView;
import trente.asia.dailyreport.services.report.view.MyReportListAdapter;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.dailyreport.view.DRCalendarHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * Created by viet on 2/15/2016.
 */
public class MyReportFragment extends AbstractDRFragment implements DRCalendarView.OnReportModelSelectedListener{

	public static int			VIEW_AS_CALENDAR	= 1;
	public static int			VIEW_AS_LIST		= 2;

	private ListView			lstReports;
	private DRCalendarView		drCalendarView;
	private DRCalendarHeader	calendarHeader;
	private ScrollView			mScrCalendarView;

	private List<ReportModel>	reports;
	private List<Holiday>		holidays;
	private TextView			txtDate;
	private List<GoalEntry>		goalSummaries;
	private LinearLayout		lnrMonthlyGoal;
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
		requestDailyReportSingle(calendarHeader.getSelectedYear(), calendarHeader.getSelectedYearMonthStr());
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_title_empty), null);

		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		inflater = LayoutInflater.from(activity);
		calendarHeader = (DRCalendarHeader)getView().findViewById(R.id.lnr_calendar_header);
		Calendar c = Calendar.getInstance();
		txtDate = (TextView)getView().findViewById(R.id.fragment_txt_calendar_header_date);
		calendarHeader.buildLayout(this, 1970, 1, c.get(Calendar.YEAR) + 1, 12, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, new DRCalendarHeader.OnViewChangeListener() {

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
			public void onTimeChange(int newYear, int newMonth){
				String monthYear = WelfareUtil.getYearMonthStr(newYear, newMonth);
				txtDate.setText(monthYear);
				requestDailyReportSingle(newYear, monthYear);
			}
		});
		txtDate.setText(calendarHeader.getSelectedYearMonthStr());
		lstReports = (ListView)getView().findViewById(R.id.lst_my_report_fragment);

		mScrCalendarView = (ScrollView)getView().findViewById(R.id.src_id_calendar_view);
		drCalendarView = (DRCalendarView)getView().findViewById(R.id.my_report_fragment_calendar_view);
		drCalendarView.setOnReportModelSelectedListener(this);

		lnrMonthlyGoal = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_goal);

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
			if(goalSummaries.size() > 0) lnrMonthlyGoal.setVisibility(View.VISIBLE);
		}else{
			mScrCalendarView.setVisibility(View.GONE);
			lnrMonthlyGoal.setVisibility(View.GONE);
			lstReports.setVisibility(View.VISIBLE);
		}
	}

	private void requestDailyReportSingle(int searchYear, String monthStr){
		UserModel userMe = prefAccUtil.getUserPref();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", userMe.key);
			jsonObject.put("targetDeptId", userMe.dept.key);
			jsonObject.put("targetMonth", monthStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(WfUrlConst.WF_REPORT_LIST_MYSELF, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			reports = CCJsonUtil.convertToModelList(response.optString("reports"), ReportModel.class);
			reports = appendReports(reports);
			holidays = CCJsonUtil.convertToModelList(response.optString("holidays"), Holiday.class);
			goalSummaries = CCJsonUtil.convertToModelList(response.optString("goalSummaries"), GoalEntry.class);
			appendHolidayInfo(reports, holidays);
			updateHeader(prefAccUtil.getUserPref().userName);
			buildCalendarView(reports);
			buildListView(reports);
			builGoalSummaries();
		}
	}

	private void builGoalSummaries(){
		if(goalSummaries.size() > 0){
			lnrMonthlyGoal.setVisibility(View.VISIBLE);
			buildGoalEntries();
		}else{
			lnrMonthlyGoal.setVisibility(View.INVISIBLE);
		}
	}

	private void buildGoalEntries(){
		LinearLayout lnrMonthlyContainer = (LinearLayout)getView().findViewById(R.id.view_kpi_lnr_monthly);
		lnrMonthlyContainer.removeAllViews();
		for(GoalEntry goalEntry : goalSummaries){
			addGoalEntry(goalEntry, lnrMonthlyContainer);
		}
	}

	private void addGoalEntry(GoalEntry goalEntry, LinearLayout lnrConainer){
		View itemView = inflater.inflate(R.layout.item_goal_entry_detail_myreport, null);
		TextView txtItem = (TextView)itemView.findViewById(R.id.item_kpi_name);
		TextView txtGoal = (TextView)itemView.findViewById(R.id.item_kpi_goal);
		TextView txtNow = (TextView)itemView.findViewById(R.id.item_kpi_month_txt_plan);
		TextView txtAchievement = (TextView)itemView.findViewById(R.id.item_kpi_achievement);

		txtItem.setText(goalEntry.goalName);
		if(Kpi.KPI_UNIT_TIME.equals(goalEntry.goalUnit)){
			txtGoal.setText(goalEntry.goalValue);
			txtNow.setText(goalEntry.actualSum);
		}else{
			txtGoal.setText(WelfareUtil.formatAmount(goalEntry.goalValue));
			txtNow.setText(WelfareUtil.formatAmount(goalEntry.actualSum));
		}
		txtAchievement.setText(GoalEntry.getAchievementString(goalEntry));
		lnrConainer.addView(itemView);
	}

	/*
	 * If there is no report on date 2016/04/05, add an empty report to it
	 * @return reports: a month full of report
	 */
	private List<ReportModel> appendReports(List<ReportModel> reports){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, calendarHeader.getSelectedYear());
		c.set(Calendar.MONTH, calendarHeader.getSelectedMonth() - 1);
		List<ReportModel> results = new ArrayList<>();
		for(int i = c.getActualMinimum(Calendar.DAY_OF_MONTH); i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
			c.set(Calendar.DAY_OF_MONTH, i);
			ReportModel reportModel = getReportByDay(c, reports);
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
