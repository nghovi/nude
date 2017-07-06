package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpi.model.ActionPlan;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.kpi.view.CalendarFragment;
import trente.asia.dailyreport.services.kpi.view.CalendarPagerAdapter;
import trente.asia.dailyreport.services.kpi.view.KpiCalendarView;
import trente.asia.dailyreport.services.report.ReportEditFragment;
import trente.asia.dailyreport.services.report.model.ActionEntry;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.DRCalendarView;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.dailyreport.view.DRGroupHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;

/**
 * Created by viet on 2/15/2016.
 */
public class ActualPlanAddFragment extends AbstractDRFragment implements DRCalendarView.OnReportModelSelectedListener,KpiCalendarView.OnDayClickedListener{

	private static final String	ACTUAL_PLAN_EDT_ID	= "edt_actual_plan_";
	private TextView			txtDate;
	private List<ActionPlan>	actionPlanList;
	private LinearLayout		lnrActualPlanContainer;
	private LinearLayout		lnrActionPlanSection;
	private TextView			txtNoAction;
	private EditText			edtMemo;
	private LayoutInflater		inflater;
	private DRGroupHeader		drGroupHeader;

	ViewPager					mPager;
	CalendarPagerAdapter		adapter;
	private KpiCalendarView		kpiCalendarView;
	private GroupKpi			selectedGroup;
	private List<GroupKpi>		groupKpiList;
	private Date				selectedDate;

	ImageView					imgHeaderRightIcon;
	private Map<String, String>	statusMap;
	private boolean				showStatusOnly		= false;
	private List<EditText>		edtActionValues;

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
		loadGroupInfo();
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_actual_plan_title), null);
		// updateHeader(prefAccUtil.getUserPref().userName);
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		inflater = LayoutInflater.from(activity);
		imgHeaderRightIcon = (ImageView)activity.findViewById(trente.asia.welfare.adr.R.id.img_id_header_right_icon);
		selectedDate = Calendar.getInstance().getTime();
		txtDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
		drGroupHeader = (DRGroupHeader)getView().findViewById(R.id.lnr_group_header);
		drGroupHeader.setOnGroupHeaderActionListener(new DRGroupHeader.OnGroupHeaderActionsListener() {

			@Override
			public void onGroupChange(GroupKpi newGroup){
				selectedGroup = newGroup;
				loadActionPlans(selectedDate, false);
			}

			@Override
			public void onNowGroupClicked(GroupKpi selectedGroup){

			}
		});

		lnrActualPlanContainer = (LinearLayout)getView().findViewById(R.id.lnr_actual_plan_container);
		lnrActionPlanSection = (LinearLayout)getView().findViewById(R.id.lnr_action_plan_section);
		txtNoAction = (TextView)getView().findViewById(R.id.txt_fragment_action_plan_empty);
		edtMemo = (EditText)getView().findViewById(R.id.edt_supplement_memo);
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
		mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

			}

			@Override
			public void onPageSelected(int position){
				lnrActionPlanSection.setVisibility(View.GONE);
				txtNoAction.setVisibility(View.GONE);
				Calendar c = ((CalendarFragment)adapter.getItem(mPager.getCurrentItem())).getCalendar();
				loadActionPlans(c.getTime(), true);
			}

			@Override
			public void onPageScrollStateChanged(int state){

			}
		});
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			if(DRConst.API_KPI_GROUPS.equals(url)){
				onLoadGroupsSuccess(response);
			}else if(DRConst.API_KPI_ACTIONS.equals(url)){
				onGetActionsSuccess(response);
			}else{
				super.successLoad(response, url);
			}

		}
	}

	private void onGetActionsSuccess(JSONObject response){
		List<ApiObjectModel> statusList = CCJsonUtil.convertToModelList(response.optString("statusList"), ApiObjectModel.class);
		statusMap = buildStatusMap(statusList);
		updateCalendarView(actionPlanList);
		if(showStatusOnly == false){
			actionPlanList = CCJsonUtil.convertToModelList(response.optString("actionPlanList"), ActionPlan.class);
			buildActionPlans();
		}
	}

	private Map<String, String> buildStatusMap(List<ApiObjectModel> statusList){
		Map<String, String> result = new HashMap<>();
		for(ApiObjectModel status : statusList){
			result.put(status.key, status.value);
		}
		return result;
	}

	private void onLoadGroupsSuccess(JSONObject response){
		groupKpiList = CCJsonUtil.convertToModelList(response.optString("groups"), GroupKpi.class);
		if(CCCollectionUtil.isEmpty(groupKpiList)){
			getView().findViewById(R.id.lnr_fragment_action_plan_main).setVisibility(View.GONE);
			getView().findViewById(R.id.txt_fragment_action_plan_empty).setVisibility(View.VISIBLE);
		}else{
			// groupKpiList = createDummy();
			drGroupHeader.buildLayout(groupKpiList, 0, false);
			selectedGroup = drGroupHeader.getSelectedGroup();
			loadActionPlans(selectedDate, false);
		}
	}

	private void loadGroupInfo(){
		String dateStr = txtDate.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetDate", dateStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_GROUPS, jsonObject, true);
	}

	private void loadActionPlans(Date date, boolean showStatusOnly){
		this.showStatusOnly = showStatusOnly;
		String targetDate = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date);
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetGroupId", selectedGroup.key);
			jsonObject.put("targetDate", targetDate);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_ACTIONS, jsonObject, true);
	}

	private void buildActionPlans(){
		if(!CCCollectionUtil.isEmpty(actionPlanList)){
			imgHeaderRightIcon.setVisibility(View.VISIBLE);
			boolean editMode = ActionPlan.STATUS_YET.equals(statusMap.get(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate)));
			if(editMode){
				imgHeaderRightIcon.setImageResource(R.drawable.wf_check);
				imgHeaderRightIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						saveActionPlans();
					}
				});
			}else{
				imgHeaderRightIcon.setImageResource(R.drawable.wf_attachment);
				imgHeaderRightIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						showActionPlans(true);
					}
				});
			}

			showActionPlans(editMode);
		}else{
			imgHeaderRightIcon.setVisibility(View.INVISIBLE);
			lnrActionPlanSection.setVisibility(View.GONE);
			txtNoAction.setVisibility(View.VISIBLE);
		}
	}

	private void showActionPlans(Boolean editMode){
		txtNoAction.setVisibility(View.GONE);
		lnrActionPlanSection.setVisibility(View.VISIBLE);
		lnrActualPlanContainer.removeAllViews();
		edtActionValues.clear();

		for(ActionPlan actionPlan : actionPlanList){
			addActionPlanView(actionPlan, editMode);
		}
	}

	private void saveActionPlans(){
		JSONObject jsonObject = new JSONObject();
		String dateStr = txtDate.getText().toString();
		String actionJson = getActionJson();
		try{
			jsonObject.put("actionJson", actionJson);
			jsonObject.put("targetDate", dateStr);
			jsonObject.put("note", edtMemo.getText().toString());
			jsonObject.put("targetGroupId", selectedGroup.key);

		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(DRConst.API_KPI_ACTIONS_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		loadActionPlans(selectedDate, false);
	}

	private void addActionPlanView(ActionPlan actionPlan, boolean editMode){
		View cellView;
		if(editMode){
			cellView = inflater.inflate(R.layout.item_actual_plan, null);
			TextView txtActualName = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_name);
			txtActualName.setText(actionPlan.name);

			TextView txtTodayGoal = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_goal);
			txtTodayGoal.setText(actionPlan.goal);
			EditText edtReal = (EditText)cellView.findViewById(R.id.edt_item_actual_plan_real);
			edtReal.setTag(actionPlan.key);
			edtActionValues.add(edtReal);
		}else{
			cellView = inflater.inflate(R.layout.item_actual_plan_view, null);
			TextView txtActualName = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_name);
			txtActualName.setText(actionPlan.name);

			TextView txtTodayGoal = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_goal);
			txtTodayGoal.setText(actionPlan.goal);

			TextView txtPerformance = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_performance);
			txtPerformance.setText(actionPlan.goal);

			TextView txtPerformanceMonth = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_performance_month);
			txtPerformanceMonth.setText(actionPlan.goal);

			TextView txtRate = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_month_rate);
			txtRate.setText(actionPlan.goal);
		}
		lnrActualPlanContainer.addView(cellView);
	}

	public static ActionPlan createEmptyActualPlanModel(Calendar c){
		ActionPlan actionPlan = new ActionPlan();
		actionPlan.date = CCFormatUtil.formatDateCustom(DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, c.getTime());
		return actionPlan;
	}

	private void updateCalendarView(List<ActionPlan> actionPlans){
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

	public static ActionPlan getActualPlanByDay(Calendar c, List<ActionPlan> actionPlen){
		for(ActionPlan actionPlan : actionPlen){
			String day = DRUtil.getDateString(actionPlan.date, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD);
			if(day.equals(DRUtil.getDateString(c.getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD))){
				return actionPlan;
			}
		}
		return createEmptyActualPlanModel(c);
		// todo></todo>
	}

	@Override
	public void onDayClicked(Calendar c){
		selectedDate = c.getTime();
		txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
		loadActionPlans(c.getTime(), false);
	}

	public String getActionJson(){
		class Action{

			public String	key;
			public String	actual;
		}

		List<Action> actions = new ArrayList<>();

		for(EditText edtActionValue : edtActionValues){
			Action action = new Action();
			action.key = (String)edtActionValue.getTag();
			action.actual = edtActionValue.getText().toString().replaceAll("[^\\d.]", "");
			actions.add(action);
		}

		Gson gson = new Gson();

		return gson.toJson(actions);
	}
}
