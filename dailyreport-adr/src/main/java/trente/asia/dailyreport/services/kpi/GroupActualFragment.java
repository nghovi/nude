package trente.asia.dailyreport.services.kpi;

import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

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

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_group_actual;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_kpi;
	}

	@Override
	public void initData(){
		if(groupKpiKey == null){
			groupKpiKey = (String)((WelfareActivity)activity).dataMap.get(GroupActualListFragment.GROUP_KPI_KEY);
			((WelfareActivity)activity).dataMap.clear();
		}

		loadGroupDetail(groupKpiKey);
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_group_actual_title), null);

		Calendar calendar = Calendar.getInstance();
		txtSelectedDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		txtGroupName = (TextView)getView().findViewById(R.id.txt_fragment_group_name);
		txtSelectedDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendar.getTime()));
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtSelectedDate.setText(startDateStr);
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

		lineChart = (LineChart)getView().findViewById(R.id.chart);
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

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			groupKpi = CCJsonUtil.convertToModel(response.optString("group"), GroupKpi.class);
			txtGroupName.setText(groupKpi.name);

			String periodString = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(groupKpi.startDate, WelfareConst.WF_DATE_TIME)) + " ~ " + CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(groupKpi.endDate, WelfareConst.WF_DATE_TIME));
			txtPeriod.setText(periodString);
			txtGoal.setText(CCFormatUtil.formatAmount(groupKpi.goal) + " " + groupKpi.unit);
			txtActualTotal.setText(CCFormatUtil.formatAmount(groupKpi.achievement) + " " + groupKpi.unit);
			txtAchievementRate.setText(CCFormatUtil.formatAmount(groupKpi.achievementRate) + "%");
			txtToGoal.setText(CCFormatUtil.formatAmount(Integer.parseInt(groupKpi.goal) - Integer.parseInt(groupKpi.achievement)) + " " + groupKpi.unit);

			// UserActualFragment.buildChart(lineChart, groupKpi);
		}
	}

	private void onClickDateIcon(){
		datePickerDialog.show();
	}

	private void onClickGroupListButton(){
		GroupActualListFragment fragment = new GroupActualListFragment();
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
}
