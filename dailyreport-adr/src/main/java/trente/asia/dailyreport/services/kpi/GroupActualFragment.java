package nguyenhoangviet.vpcorp.dailyreport.services.kpi;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.activity.ChiaseActivity;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.fragments.AbstractDRFragment;
import nguyenhoangviet.vpcorp.dailyreport.services.kpi.model.GroupKpi;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class GroupActualFragment extends AbstractDRFragment{

	private LayoutInflater		inflater;
	private DatePickerDialog	datePickerDialog;
	private TextView			txtSelectedDate;
	TextView					txtPeriod;
	TextView					txtGoal;
	TextView					txtActualTotal;
	TextView					txtAchievementRate;
	TextView					txtToGoal;
	private TextView			txtGroupName;
	private GroupKpi			groupKpi;
	LineChart					lineChart;
	private String				groupKpiKey;
	private Date				selectedDate;
	private LinearLayout		lnrChartContainer;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_group_actual;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void initData(){
		String newGroupKpiKey = (String)((WelfareActivity)activity).dataMap.get(GroupActualListFragment.GROUP_KPI_KEY);
		if(!CCStringUtil.isEmpty(newGroupKpiKey)){
			((WelfareActivity)activity).dataMap.clear();
			groupKpiKey = newGroupKpiKey;
		}

		loadGroupDetail(groupKpiKey);
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_group_actual_title), null);

		Calendar calendar = Calendar.getInstance();
		txtSelectedDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		txtGroupName = (TextView)getView().findViewById(R.id.txt_fragment_group_name);
		if(selectedDate == null){
			selectedDate = calendar.getTime();
		}else{
			calendar.setTime(selectedDate);
		}
		txtSelectedDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, selectedDate));
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtSelectedDate.setText(startDateStr);
				loadGroupDetail(groupKpi.key);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		getView().findViewById(R.id.lnr_fragment_kpi_date).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickDateIcon();
			}
		});

		txtPeriod = ((TextView)getView().findViewById(R.id.group_actual_info_period));
		txtGoal = ((TextView)getView().findViewById(R.id.group_actual_info_goal));
		txtActualTotal = ((TextView)getView().findViewById(R.id.group_actual_info_actual_total));
		txtAchievementRate = ((TextView)getView().findViewById(R.id.group_actual_info_achievement_rate));
		txtToGoal = ((TextView)getView().findViewById(R.id.group_actual_info_to_goal));

		getView().findViewById(R.id.btn_fragment_group_actual_list).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickGroupListButton();
			}
		});

		lnrChartContainer = (LinearLayout)getView().findViewById(R.id.lnr_chart_container);

		// lineChart = (LineChart)getView().findViewById(R.id.chart);
	}

	private void loadGroupDetail(String groupKpiKey){
		String dateStr = txtSelectedDate.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("searchNo", groupKpiKey);
			jsonObject.put("searchDateString", dateStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_GROUP, jsonObject, true);
	}

	protected void onClickBackBtn(){
		if(getFragmentManager().getBackStackEntryCount() <= 1){
			emptyBackStack();
			UserActualFragment userActualFragment = new UserActualFragment();
			userActualFragment.setGroupId(groupKpi.key);
			userActualFragment.setTargetDate(txtSelectedDate.getText().toString());
			gotoFragment(userActualFragment);
		}else{
			((ChiaseActivity)activity).isInitData = true;
			getFragmentManager().popBackStack();
		}
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			groupKpi = CCJsonUtil.convertToModel(response.optString("group"), GroupKpi.class);
			txtGroupName.setText(groupKpi.name);
			String periodString = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(groupKpi.startDate, WelfareConst.WF_DATE_TIME)) + " ~ " + CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(groupKpi.endDate, WelfareConst.WF_DATE_TIME));
			txtPeriod.setText(periodString);
			txtGoal.setText(CCFormatUtil.formatAmount(groupKpi.goal) + " " + groupKpi.goalUnit);
			txtActualTotal.setText(CCFormatUtil.formatAmount(groupKpi.actual) + " " + groupKpi.goalUnit);
			txtAchievementRate.setText(CCFormatUtil.formatAmount(groupKpi.achievementRate) + "%");
			txtToGoal.setText(CCFormatUtil.formatAmount(Integer.parseInt(groupKpi.toGoal)) + " " + groupKpi.goalUnit);

			Date groupStartDate = CCDateUtil.makeDateCustom(groupKpi.startDate, WelfareConst.WF_DATE_TIME);
			Date groupEndDate = CCDateUtil.makeDateCustom(groupKpi.endDate, WelfareConst.WF_DATE_TIME);
			datePickerDialog.getDatePicker().setMinDate(groupStartDate.getTime());
			datePickerDialog.getDatePicker().setMaxDate(groupEndDate.getTime());
			//// TODO: 7/14/17 setMinDate not work right away
			// datePickerDialog.getDatePicker().forceLayout();
			//// TODO: 7/17/17 create field for chart_unit
			lnrChartContainer.removeAllViews();
			LinearLayout chartView = (LinearLayout)LayoutInflater.from(activity).inflate(R.layout.kpi_chart, null);
			lineChart = (LineChart)chartView.findViewById(R.id.chart);
			((TextView)chartView.findViewById(R.id.txt_kpi_chart_unit)).setText(groupKpi.goalUnit);
			String status = null;
			if(Integer.parseInt(groupKpi.actual) >= Integer.valueOf(groupKpi.goal)){
				status = getString(R.string.achieve_dialog_title);
			}else{
				status = getString(R.string.fragmen_group_actual_needed_amount2, CCFormatUtil.formatAmount(groupKpi.toGoal)) + groupKpi.goalUnit;
			}
			((TextView)chartView.findViewById(R.id.txt_kpi_chart_result)).setText(status);
			UserActualFragment.buildChart(activity, lineChart, groupKpi.checkPoints, groupKpi, null);
			lnrChartContainer.addView(chartView);
		}
	}

	private void onClickDateIcon(){
		datePickerDialog.show();
	}

	private void onClickGroupListButton(){
		GroupActualListFragment fragment = new GroupActualListFragment();
		fragment.setSelectedDate(selectedDate);
		gotoFragment(fragment);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(getView() != null){

		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public void setGroupKpiKey(String groupKpiKey){
		this.groupKpiKey = groupKpiKey;
	}

	public void setSelectedDate(Date selectedDate){
		this.selectedDate = selectedDate;
	}
}
