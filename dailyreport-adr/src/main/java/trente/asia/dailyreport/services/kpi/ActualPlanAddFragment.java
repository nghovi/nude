package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import trente.asia.dailyreport.services.kpi.view.CalendarFragment;
import trente.asia.dailyreport.services.kpi.view.CalendarPagerAdapter;
import trente.asia.dailyreport.services.kpi.view.KpiCalendarView;
import trente.asia.dailyreport.services.report.ReportEditFragment;
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
	private TextView			txtDate;
	private List<ActualPlan>	actualPlanList;
	private LinearLayout		lnrMonthlyGoal;
	private LinearLayout		lnrKpiSection;
	private LayoutInflater		inflater;
	private DRGroupHeader		drGroupHeader;

	ViewPager					mPager;
	CalendarPagerAdapter		adapter;
	private KpiCalendarView		kpiCalendarView;

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

		lnrMonthlyGoal = (LinearLayout)getView().findViewById(R.id.lnr_id_monthly_goal);
		lnrKpiSection = (LinearLayout)getView().findViewById(R.id.my_report_kpi_section);

		mPager = (ViewPager)getView().findViewById(R.id.view_pager);
		mPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event){
				mPager.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		adapter = new CalendarPagerAdapter(getChildFragmentManager());
		adapter.setOnDayClickListener(this);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(Integer.MAX_VALUE / 2);
	}

	private void loadActionPlans(){
		UserModel userMe = prefAccUtil.getUserPref();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetGroupId", userMe.key);
			jsonObject.put("targetDate", userMe.dept.key);
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
			drGroupHeader.buildLayout(new ArrayList<GroupKpi>(), 0);
			buildCalendarView(actualPlanList);
			buildActualPlans();
			if(false){
				getView().findViewById(R.id.lnr_fragment_action_plan_main).setVisibility(View.GONE);
				getView().findViewById(R.id.txt_fragment_action_plan_empty).setVisibility(View.VISIBLE);
			}
		}
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

	private void buildCalendarView(List<ActualPlan> actionPlans){
		kpiCalendarView = ((CalendarFragment)adapter.getItem(mPager.getCurrentItem())).getKpiCalendarView();
		Date d = CCDateUtil.makeDateCustom(txtDate.getText().toString(), WelfareConst.WF_DATE_TIME_DATE);
		Calendar c = CCDateUtil.makeCalendar(d);
		kpiCalendarView.updateLayoutWithData(actionPlans);
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
	public void onDayClicked(Calendar selectedDate){
		Date t = selectedDate.getTime();
		Date y = t;
	}
}
