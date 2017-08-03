package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.dialogs.DRDialog;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpi.model.ActionPlan;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.kpi.view.CalendarFragment;
import trente.asia.dailyreport.services.kpi.view.CalendarPagerAdapter;
import trente.asia.dailyreport.services.kpi.view.KpiCalendarView;
import trente.asia.dailyreport.services.report.ReportEditFragment;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.DRCalendarView;
import trente.asia.dailyreport.services.util.KpiUtil;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.dailyreport.view.DRGroupHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.ApiObjectModel;

/**
 * Created by viet on 2/15/2016.
 */
public class ActionPlansAddFragment extends AbstractDRFragment implements DRCalendarView.OnReportModelSelectedListener,KpiCalendarView.OnDayClickedListener{

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
	ImageView					imgHeaderLeftIcon;

	private Map<String, String>	statusMap;
	private boolean				showStatusOnly	= false;
	private List<EditText>		edtActionValues	= new ArrayList<>();
	private LinearLayout		lnrGroupHeader;
	private List<ActionPlan>	filteredActionPlans;
	private Locale				locale;
	private DRDialog			drDialog;

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
		loadActionPlans(selectedDate, false);
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_actual_plan_title), null);
		inflater = LayoutInflater.from(activity);
		imgHeaderLeftIcon = (ImageView)activity.findViewById(trente.asia.welfare.adr.R.id.img_id_header_left_icon);
		imgHeaderLeftIcon.setVisibility(View.INVISIBLE);
		imgHeaderRightIcon = (ImageView)activity.findViewById(trente.asia.welfare.adr.R.id.img_id_header_right_icon);
		selectedDate = Calendar.getInstance().getTime();
		txtDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
		drGroupHeader = (DRGroupHeader)getView().findViewById(R.id.lnr_group_header);
		drGroupHeader.setOnGroupHeaderActionListener(new DRGroupHeader.OnGroupHeaderActionsListener() {

			@Override
			public void onGroupChange(GroupKpi newGroup){
				selectedGroup = newGroup;
				statusMap = buildStatusMap();
				updateCalendarView(statusMap);
				filteredActionPlans = filterByGroup();
				String actionStatus = statusMap.get(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
				boolean editMode = CCStringUtil.isEmpty(actionStatus) || ActionPlan.STATUS_YET.equals(actionStatus);
				buildActionPlans(editMode);
			}

			@Override
			public void onNowGroupClicked(GroupKpi selectedGroup){

			}
		});

		lnrGroupHeader = (LinearLayout)getView().findViewById(R.id.lnr_header);
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
				imgHeaderLeftIcon.setVisibility(View.INVISIBLE);
				Calendar c = ((CalendarFragment)adapter.getItem(position)).getKpiCalendarView().getSelectedDate();
				selectedDate = c.getTime();
				txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
				lnrActionPlanSection.setVisibility(View.GONE);
				txtNoAction.setVisibility(View.GONE);
				boolean showStatusOnly = Calendar.getInstance().get(Calendar.MONTH) != c.get(Calendar.MONTH);
				loadActionPlans(selectedDate, showStatusOnly);
			}

			@Override
			public void onPageScrollStateChanged(int state){

			}
		});
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			locale = getResources().getConfiguration().getLocales().get(0);
		}else{
			locale = getResources().getConfiguration().locale;
		}
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			onGetActionsSuccess(response);
			getView().post(new Runnable() {

				@Override
				public void run(){
					activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				}
			});
		}
	}

	private void onGetActionsSuccess(JSONObject response){
		imgHeaderLeftIcon.setVisibility(View.INVISIBLE);
		groupKpiList = CCJsonUtil.convertToModelList(response.optString("groups"), GroupKpi.class);
		int selectedGroupPosition = getSelectedGroupPosition();
		drGroupHeader.buildLayout(groupKpiList, selectedGroupPosition, false);
		selectedGroup = drGroupHeader.getSelectedGroup();
		statusMap = buildStatusMap();
		updateCalendarView(statusMap);
		if(showStatusOnly){
			imgHeaderRightIcon.setVisibility(View.INVISIBLE);
			lnrActionPlanSection.setVisibility(View.GONE);
			lnrGroupHeader.setVisibility(View.GONE);
			txtNoAction.setTextColor(Color.BLACK);
			txtNoAction.setText(getString(R.string.choose_the_day));
			txtNoAction.setVisibility(View.VISIBLE);
			kpiCalendarView = ((CalendarFragment)adapter.getItem(mPager.getCurrentItem())).getKpiCalendarView();
			kpiCalendarView.unselectDay();
		}else{
			if(CCCollectionUtil.isEmpty(groupKpiList)){
				imgHeaderLeftIcon.setVisibility(View.INVISIBLE);
				imgHeaderRightIcon.setVisibility(View.INVISIBLE);
				lnrActionPlanSection.setVisibility(View.GONE);
				lnrGroupHeader.setVisibility(View.GONE);
				txtNoAction.setTextColor(Color.RED);
				txtNoAction.setText(getString(R.string.action_plan_empty));
				txtNoAction.setVisibility(View.VISIBLE);
			}else{
				actionPlanList = CCJsonUtil.convertToModelList(response.optString("actions"), ActionPlan.class);
				filteredActionPlans = filterByGroup();
				boolean editMode = CCStringUtil.isEmpty(statusMap.get(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate)));
				buildActionPlans(editMode);
			}
		}
	}

	public int getSelectedGroupPosition(){
		if(selectedGroup == null){
			return 0;
		}
		for(int i = 0; i < groupKpiList.size(); i++){
			if(groupKpiList.get(i).key.equals(selectedGroup.key)){
				return i;
			}
		}
		return 0;
	}

	private List<ActionPlan> filterByGroup(){
		List<ActionPlan> result = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(actionPlanList)){
			for(ActionPlan actionPlan : actionPlanList){
				if(actionPlan.groupId.equals(selectedGroup.key)){
					result.add(actionPlan);
				}
			}
		}
		return result;
	}

	private Map<String, String> buildStatusMap(){
		Map<String, String> result = new HashMap<>();

		if(selectedGroup != null && !CCCollectionUtil.isEmpty(selectedGroup.statusList)){
			for(ApiObjectModel status : selectedGroup.statusList){
				String dayStatus = result.get(status.key);
				if(CCStringUtil.isEmpty(dayStatus) || !dayStatus.equals(ActionPlan.STATUS_NG)){
					dayStatus = status.value;
				}
				result.put(status.key, dayStatus);
			}
		}
		return result;
	}

	private void loadActionPlans(Date date, boolean showStatusOnly){
		this.showStatusOnly = showStatusOnly;
		String targetDate = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date);
		JSONObject jsonObject = new JSONObject();
		try{
			// jsonObject.put("targetGroupId", selectedGroup.key);
			jsonObject.put("searchDateString", targetDate);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_ACTIONS, jsonObject, true);
	}

	private void buildActionPlans(Boolean editMode){
		if(!CCCollectionUtil.isEmpty(filteredActionPlans)){
			txtNoAction.setVisibility(View.GONE);
			lnrGroupHeader.setVisibility(View.VISIBLE);
			imgHeaderRightIcon.setVisibility(View.VISIBLE);
			edtMemo.setText(selectedGroup.dayNote);
			if(editMode){
				edtMemo.setEnabled(true);
				edtMemo.setBackgroundResource(R.drawable.dr_white_background_gray_border_padding);
				imgHeaderRightIcon.setImageResource(R.drawable.ic_save);
				imgHeaderRightIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						saveActionPlans();
					}
				});
			}else{
				imgHeaderLeftIcon.setVisibility(View.INVISIBLE);
				edtMemo.setEnabled(false);
				edtMemo.setBackgroundResource(R.drawable.dr_blue_background_gray_border_padding);
				imgHeaderRightIcon.setImageResource(R.drawable.ic_edit);
				imgHeaderRightIcon.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						imgHeaderLeftIcon.setVisibility(View.VISIBLE);
						buildActionPlans(true);
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

	@Override
	protected void onClickBackBtn(){
		buildActionPlans(false);
	}

	private void showActionPlans(Boolean editMode){
		txtNoAction.setVisibility(View.GONE);
		lnrActionPlanSection.setVisibility(View.VISIBLE);
		lnrActualPlanContainer.removeAllViews();
		edtActionValues.clear();

		for(ActionPlan actionPlan : filteredActionPlans){
			inflateActionPlanView(actionPlan, editMode);
		}
		lnrActualPlanContainer.post(new Runnable() {

			@Override
			public void run(){
				activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}
		});
	}

	private void saveActionPlans(){
		if(checkValidData()){
			JSONObject jsonObject = new JSONObject();
			String dateStr = txtDate.getText().toString();
			String actionJson = getActionJson();
			try{
				jsonObject.put("targetGroupId", selectedGroup.key);
				jsonObject.put("targetDate", dateStr);
				jsonObject.put("dayNote", edtMemo.getText().toString());
				jsonObject.put("actionJson", actionJson);

			}catch(JSONException e){
				e.printStackTrace();
			}
			requestUpdate(DRConst.API_KPI_ACTIONS_UPDATE, jsonObject, true);
		}
	}

	private boolean checkValidData(){
		for(EditText edtActionValue : edtActionValues){
			if(CCStringUtil.isEmpty(edtActionValue.getText().toString())){
				showValidationDialog(getString(R.string.action_plan_please_input));
				return false;
			}

			if(!KpiUtil.isCheckSize("", edtActionValue.getText().toString())){
				showValidationDialog(getString(R.string.action_num_over_max, KpiUtil.getMax("")));
				return false;
			}

		}

		return true;
	}

	private void showValidationDialog(String message){
		if(drDialog == null){
			drDialog = new DRDialog(activity);
			drDialog.setDialogConfirm(message, getString(R.string.chiase_common_ok), null, null, null);
		}
		drDialog.show();
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		loadActionPlans(selectedDate, false);
	}

	private void inflateActionPlanView(final ActionPlan actionPlan, boolean editMode){
		View cellView;
		if(editMode){
			cellView = inflater.inflate(R.layout.item_actual_plan, null);
			TextView txtActualName = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_name);
			txtActualName.setText(actionPlan.planName);

			TextView txtTodayGoal = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_goal);
			txtTodayGoal.setText(CCFormatUtil.formatAmount(actionPlan.planValue) + actionPlan.unit);
			final EditText edtReal = (EditText)cellView.findViewById(R.id.edt_item_actual_plan_real);
			// edtReal.setCursorVisible(false)
			// ;
			if("0".equals(actionPlan.actual) && CCStringUtil.isEmpty(statusMap.get(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate)))){
				actionPlan.actual = null;
			}
			edtReal.setText(actionPlan.actual);
			edtReal.setTag(actionPlan.key);
			edtReal.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					edtReal.setCursorVisible(true);
					edtReal.setSelection(edtReal.getText().length());
				}
			});
			edtActionValues.add(edtReal);
			TextView txtUnit = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_real_unit);
			txtUnit.setText(actionPlan.unit);
		}else{
			cellView = inflater.inflate(R.layout.item_actual_plan_view, null);
			TextView txtActualName = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_name);
			txtActualName.setText(actionPlan.planName);

			TextView txtTodayGoal = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_goal);
			txtTodayGoal.setText(CCFormatUtil.formatAmount(actionPlan.planValue) + actionPlan.unit);

			TextView txtPerformance = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_today_performance);
			txtPerformance.setText(CCFormatUtil.formatAmount(actionPlan.actual) + actionPlan.unit);

			String language = locale.getLanguage();
			String monthStr = CCFormatUtil.formatDateCustom("M", selectedDate);
			if("en".equals(language)){
				monthStr = CCFormatUtil.formatDateCustom("MMM", selectedDate);
			}

			TextView txtPerformanceMonthLabel = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_performance_month_label);
			txtPerformanceMonthLabel.setText(getString(R.string.performance_in_month, monthStr));

			TextView txtPerformanceMonth = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_performance_month);
			txtPerformanceMonth.setText(CCFormatUtil.formatAmount(actionPlan.achievementMonth) + actionPlan.unit);

			TextView txtRateLabel = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_month_rate_label);
			txtRateLabel.setText(getString(R.string.achievement_rate_in_month, monthStr));

			TextView txtRate = (TextView)cellView.findViewById(R.id.txt_item_actual_plan_month_rate);
			txtRate.setText(actionPlan.achievementRate + "%");
		}
		lnrActualPlanContainer.addView(cellView);
	}

	public static ActionPlan createEmptyActualPlanModel(Calendar c){
		ActionPlan actionPlan = new ActionPlan();
		actionPlan.date = CCFormatUtil.formatDateCustom(DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, c.getTime());
		actionPlan.actionStatus = ActionPlan.STATUS_YET;
		return actionPlan;
	}

	private void updateCalendarView(Map<String, String> statusMap){
		kpiCalendarView = ((CalendarFragment)adapter.getItem(mPager.getCurrentItem())).getKpiCalendarView();
		kpiCalendarView.updateLayoutWithData(statusMap);
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

	@Override
	public void onDayClicked(Calendar c){
		selectedDate = c.getTime();
		txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
		loadActionPlans(c.getTime(), false);
	}

	public static class Action{

		public String	key;
		public String	actual;
	}

	public String getActionJson(){

		List<Action> actions = new ArrayList<Action>();

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
