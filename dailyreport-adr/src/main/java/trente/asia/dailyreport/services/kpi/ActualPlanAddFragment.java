package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import trente.asia.dailyreport.services.kpi.model.ActualPlan;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.kpi.view.KpiCalendarView;
import trente.asia.dailyreport.services.report.ReportEditFragment;
import trente.asia.dailyreport.services.report.model.Holiday;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.DRCalendarView;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.dailyreport.view.DRGroupHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class ActualPlanAddFragment extends AbstractDRFragment implements DRCalendarView.OnReportModelSelectedListener,KpiCalendarView.OnDayClickedListener{

	private static final String	ACTUAL_PLAN_EDT_ID	= "edt_actual_plan_";
	public static int			VIEW_AS_CALENDAR	= 1;
	public static int			VIEW_AS_LIST		= 2;

	private ListView			lstReports;
	private KpiCalendarView		kpiCalendarView;
	private ScrollView			mScrCalendarView;

	private List<ReportModel>	reports;
	private List<Holiday>		holidays;
	private TextView			txtDate;
	private List<ActualPlan>	actualPlanList;
	private LinearLayout		lnrMonthlyGoal;
	private LinearLayout		lnrKpiSection;
	private LayoutInflater		inflater;
	private List<ActualPlan>	actionPlans			= new ArrayList<>();
	private DRGroupHeader		drGroupHeader;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_action_plan_add;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_ap;
	}

	@Override
	public void initData(){
		loadActionPlans();
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_actual_plan_title), null);

		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		inflater = LayoutInflater.from(activity);
		Calendar c = Calendar.getInstance();
		txtDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		drGroupHeader = (DRGroupHeader)getView().findViewById(R.id.lnr_group_header);

		lstReports = (ListView)getView().findViewById(R.id.lst_my_report_fragment);

		mScrCalendarView = (ScrollView)getView().findViewById(R.id.src_id_calendar_view);
		kpiCalendarView = (KpiCalendarView)getView().findViewById(R.id.my_report_fragment_calendar_view);
		kpiCalendarView.setOnDayClickedListener(this);

		lnrMonthlyGoal = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_goal);
		lnrKpiSection = (LinearLayout)getView().findViewById(R.id.my_report_kpi_section);
	}

	private void loadActionPlans(){
		UserModel userMe = prefAccUtil.getUserPref();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetGroupId", userMe.key);
			jsonObject.put("targetDate", userMe.dept.key);
			// jsonObject.put("targetMonth", monthStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_PERSONAL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){

			actualPlanList = CCJsonUtil.convertToModelList(response.optString("actualPlanList"), ActualPlan.class);
			updateHeader(prefAccUtil.getUserPref().userName);
			actualPlanList = new ArrayList<>();
			actualPlanList = addForEmptyDays(actualPlanList);
			buildCalendarView(actualPlanList);

			drGroupHeader.buildLayout(new ArrayList<GroupKpi>(), 0);

			buildActualPlans();
			if(false){
				getView().findViewById(R.id.lnr_fragment_action_plan_main).setVisibility(View.GONE);
				getView().findViewById(R.id.txt_fragment_action_plan_empty).setVisibility(View.VISIBLE);
			}
		}
	}

	/*
	 * If there is no report on date 2016/04/05, add an empty report to it
	 * @return reports: a month full of report
	 */
	private List<ActualPlan> addForEmptyDays(List<ActualPlan> actualPlanList){
		Date d = CCDateUtil.makeDateCustom(txtDate.getText().toString(), WelfareConst.WF_DATE_TIME_DATE);
		Calendar c = CCDateUtil.makeCalendar(d);

		List<ActualPlan> results = new ArrayList<>();
		for(int i = c.getActualMinimum(Calendar.DAY_OF_MONTH); i <= c.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
			c.set(Calendar.DAY_OF_MONTH, i);
			ActualPlan actualPlan = getActualPlanByDay(c, actualPlanList);
			results.add(actualPlan);
		}
		return results;
	}

	private void buildActualPlans(){
		if(!CCCollectionUtil.isEmpty(actualPlanList)){
			lnrMonthlyGoal.removeAllViews();
			lnrKpiSection.setVisibility(View.VISIBLE);
			for(ActualPlan actualPlan : actualPlanList){
				addActualPlan(actualPlan);
			}
		}else{
			lnrKpiSection.setVisibility(View.INVISIBLE);
		}
	}

	private void addActualPlan(ActualPlan actualPlan){
		if(true){
			View cellView = inflater.inflate(R.layout.item_actual_plan, null);

			TextView txtTodayGoal = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_goal);
			txtTodayGoal.setText(actualPlan.name);
			EditText edtReal = (EditText)cellView.findViewById(R.id.edt_item_actual_plan_real);
			edtReal.setTag(ACTUAL_PLAN_EDT_ID + actualPlan.key);
		}else{
			View cellView = inflater.inflate(R.layout.item_actual_plan_view, null);
			TextView txtTodayGoal = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_goal);
			txtTodayGoal.setText(actualPlan.goal);

			TextView txtPerformance = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_performance);
			txtPerformance.setText(actualPlan.goal);

			TextView txtPerformanceMonth = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_performance_month);
			txtPerformanceMonth.setText(actualPlan.goal);

			TextView txtRate = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_month_rate);
			txtRate.setText(actualPlan.goal);
		}
	}

	public static ActualPlan createEmptyActualPlanModel(Calendar c){
		ActualPlan actualPlan = new ActualPlan();
		actualPlan.date = CCFormatUtil.formatDateCustom(DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, c.getTime());
		return actualPlan;
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

	private void buildCalendarView(List<ActualPlan> actionPlans){
		Date d = CCDateUtil.makeDateCustom(txtDate.getText().toString(), WelfareConst.WF_DATE_TIME_DATE);
		Calendar c = CCDateUtil.makeCalendar(d);
		kpiCalendarView.updateLayout(activity, c.get(Calendar.YEAR), c.get(Calendar.MONTH), actionPlans);
	}

	@Override
	public void onReportModelSelected(ReportModel reportModel){
		if(CCStringUtil.isEmpty(reportModel.key)){// create new
			gotoReportEditFragment(activity, reportModel);
		}else{
			if(ReportModel.REPORT_STATUS_DRTT.equals(reportModel.reportStatus)){
				gotoReportEditFragment(activity, reportModel);
			}else if(ReportModel.REPORT_STATUS_DONE.equals(reportModel.reportStatus)){
				// gotoReportDetailFragment(activity, reportModel);
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
		kpiCalendarView = null;
		txtDate = null;
	}

	public static ActualPlan getActualPlanByDay(Calendar c, List<ActualPlan> actualPlans){
		for(ActualPlan actualPlan : actualPlans){
			String day = DRUtil.getDateString(actualPlan.date, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD);
			if(day.equals(DRUtil.getDateString(c.getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD))){
				return actualPlan;
			}
		}
		return createEmptyActualPlanModel(c);
		// todo></todo>
	}

	@Override
	public void onDayClicked(ActualPlan actualPlan){

	}
}
