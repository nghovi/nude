package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
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
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.kpi.model.Personal;
import trente.asia.dailyreport.services.kpi.model.Progress;
import trente.asia.dailyreport.view.DRGroupHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfDialog;

/**
 * Created by viet on 2/15/2016.
 */
public class UserActualFragment extends AbstractDRFragment{

	private static final int			LABEL_COUNT	= 25;
	private static Map<Float, String>	formattedValuesMap;
	private DatePickerDialog			datePickerDialog;
	private TextView					txtSelectedDate;
	private DRGroupHeader				drGroupHeader;
	private WfDialog					wfDialog;
	private List<GroupKpi>				groupKpiList;
	LineChart							lineChart;
	private LinearLayout				lnrInfo;
	private GroupKpi					selectedGroup;
	private TextView					txtPeriod;
	private TextView					txtGoal;
	private TextView					txtActualTotal;
	private TextView					txtAchievementRate;
	private EditText					edtActualToday;
	private Personal					personal;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_user_actual;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_kpi;
	}

	@Override
	public void initData(){
		loadGroupInfo();
	}

	private void loadGroupInfo(){
		String dateStr = txtSelectedDate.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetDate", dateStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_GROUPS, jsonObject, true);
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_kpi_title), null);

		// getView().findViewById(R.id.lnr_fragment_action_plan_main).setVisibility(View.GONE);

		Calendar calendar = Calendar.getInstance();
		txtSelectedDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		txtSelectedDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendar.getTime()));
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				if(!CCCollectionUtil.isEmpty(groupKpiList)){
					loadPersonalInfo();
				}
				txtSelectedDate.setText(startDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		getView().findViewById(R.id.lnr_fragment_kpi_date).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickDateIcon();
			}
		});
		getView().findViewById(R.id.btn_fragment_user_actual_save).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickSaveButton();
			}
		});

		drGroupHeader = (DRGroupHeader)getView().findViewById(R.id.lnr_group_header);
		drGroupHeader.setOnGroupHeaderActionListener(new DRGroupHeader.OnGroupHeaderActionsListener() {

			@Override
			public void onGroupChange(GroupKpi newGroup){
				selectedGroup = newGroup;
				loadPersonalInfo();
			}

			@Override
			public void onNowGroupClicked(GroupKpi selectedGroup){
				GroupActualFragment groupActualFragment = new GroupActualFragment();
				groupActualFragment.setGroupKpiKey(selectedGroup.key);
				((WelfareActivity)activity).addFragment(groupActualFragment);
			}
		});

		txtPeriod = ((TextView)getView().findViewById(R.id.txt_fragment_user_actual_period));
		txtGoal = ((TextView)getView().findViewById(R.id.txt_fragment_user_actual_goal));
		txtActualTotal = ((TextView)getView().findViewById(R.id.txt_fragment_user_actual_total));
		txtAchievementRate = ((TextView)getView().findViewById(R.id.txt_fragment_user_actual_achievement_rate));
		edtActualToday = ((EditText)getView().findViewById(R.id.edt_fragment_kpi_value));

		lnrInfo = (LinearLayout)getView().findViewById(R.id.lnr_fragment_user_actual_info);
		lineChart = (LineChart)getView().findViewById(R.id.chart);

		// groupKpiList = createDummy();
		// buildChart(lineChart, groupKpiList.get(0));

	}

	public static void buildChart(LineChart lineChart, Personal personal){
		if(!CCCollectionUtil.isEmpty(personal.progressList)){
			formattedValuesMap = new HashMap<>();
			List<Entry> entries = new ArrayList<Entry>();
			Progress maxCheckPoint = personal.progressList.get(personal.progressList.size() - 1);

			Date lastCheckPointDate = CCDateUtil.makeDateCustom(maxCheckPoint.checkPointDate, WelfareConst.WF_DATE_TIME_DATE);
			Date firstCheckPointDate = CCDateUtil.makeDateCustom(personal.progressList.get(0).checkPointDate, WelfareConst.WF_DATE_TIME_DATE);
			long maxDistanceDay = (lastCheckPointDate.getTime() - firstCheckPointDate.getTime()) / (24 * 60 * 60 * 1000);

			int pageNum = personal.progressList.size() / 7 + 1;
			float maxVisibleRange = maxDistanceDay / pageNum;
			float labelDistance = Math.round(maxVisibleRange / LABEL_COUNT);

			for(Progress checkPoint : personal.progressList){
				Date checkPointDate = CCDateUtil.makeDateCustom(checkPoint.checkPointDate, WelfareConst.WF_DATE_TIME_DATE);
				float xDay = (checkPointDate.getTime() - firstCheckPointDate.getTime()) / (24 * 60 * 60 * 1000);

				float labelAxisValue = (Math.round(xDay / labelDistance)) * labelDistance;
				formattedValuesMap.put(labelAxisValue, checkPoint.checkPointDate);
				Entry t = new Entry(xDay, Float.valueOf(checkPoint.achievement));
				entries.add(t);
			}
			LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
			dataSet.setColor(Color.BLUE);
			dataSet.setLineWidth(1f);
			dataSet.setValueTextColor(Color.BLUE); // styling, ...
			dataSet.setValueTextSize(9);
			dataSet.setLabel(personal.group.name + " checkpoint achievement");

			LineData lineData = new LineData();
			lineData.addDataSet(dataSet);

			lineChart.setData(lineData);
			lineChart.setVisibleYRangeMaximum(200f, YAxis.AxisDependency.LEFT);
			lineChart.setVisibleYRangeMaximum(200f, YAxis.AxisDependency.RIGHT);

			IAxisValueFormatter formatter = new IAxisValueFormatter() {

				@Override
				public String getFormattedValue(float value, AxisBase axis){
					// return String.valueOf(value);
					String dateStr = formattedValuesMap.get(value);
					return CCStringUtil.isEmpty(dateStr) ? "" : dateStr;
				}

			};

			XAxis xAxis = lineChart.getXAxis();
			xAxis.setGranularityEnabled(false);
			xAxis.setLabelCount(LABEL_COUNT, false);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
			xAxis.setTextSize(10f);
			xAxis.setTextColor(Color.BLACK);
			xAxis.setDrawAxisLine(true);
			xAxis.setDrawGridLines(false);
			// xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
			xAxis.setValueFormatter(formatter);
			// xAxis.setAvoidFirstLastClipping(true);
			xAxis.setLabelRotationAngle(-50f);
			xAxis.setTextSize(7);
			xAxis.setAxisMaximum(maxDistanceDay + labelDistance);

			// set a custom value formatter
			// xAxis.setValueFormatter(new MyCustomFormatter());
			// and more...

			YAxis yAxisLeft = lineChart.getAxisLeft();
			yAxisLeft.setDrawAxisLine(false);
			// yAxisLeft.setZeroLineColor(Color.RED);
			// yAxisLeft.setDrawZeroLine(true);
			// yAxisLeft.setZeroLineWidth(10);
			yAxisLeft.setValueFormatter(new IAxisValueFormatter() {

				@Override
				public String getFormattedValue(float value, AxisBase axis){
					return "";
					// return String.valueOf((int)value * Integer.valueOf(selectedGroup.goal) + "円");
				}
			});

			//// TODO: 6/29/17 cannot add 150% line so add max %line instead
			LimitLine line150Left = new LimitLine(Float.valueOf(maxCheckPoint.achievement), String.valueOf(Integer.parseInt(personal.group.goal) * Integer.parseInt(maxCheckPoint.achievement) / 100) + "円");
			line150Left.setLineWidth(0.5f);
			line150Left.setLineColor(Color.GRAY);
			// upper_limit.enableDashedLine(10f, 10f, 0f);
			line150Left.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
			line150Left.setTextSize(10f);

			LimitLine line150Right = new LimitLine(Float.valueOf(maxCheckPoint.achievement), "");
			line150Right.setLineWidth(0.5f);
			line150Right.setLineColor(Color.GRAY);
			// upper_limit.enableDashedLine(10f, 10f, 0f);
			line150Right.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
			line150Right.setTextSize(10f);

			LimitLine line100Left = new LimitLine(100f, personal.group.goal + "円");
			line100Left.setLineWidth(0.5f);
			line100Left.setLineColor(Color.RED);
			// upper_limit.enableDashedLine(10f, 10f, 0f);
			line100Left.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
			line100Left.setTextSize(10f);

			LimitLine line100Right = new LimitLine(100f, "100%");
			line100Right.setLineWidth(0.5f);
			line100Right.setLineColor(Color.RED);
			// upper_limit.enableDashedLine(10f, 10f, 0f);
			line100Right.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
			line100Right.setTextSize(10f);

			LimitLine line50Left = new LimitLine(50f, String.valueOf(Integer.parseInt(personal.group.goal) / 2) + "円");
			line50Left.setLineWidth(0.5f);
			line50Left.setLineColor(Color.GRAY);
			// upper_limit.enableDashedLine(10f, 10f, 0f);
			line50Left.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
			line50Left.setTextSize(10f);

			LimitLine line50Right = new LimitLine(50f, "50%");
			line50Right.setLineWidth(0.5f);
			line50Right.setLineColor(Color.GRAY);
			// upper_limit.enableDashedLine(10f, 10f, 0f);
			line50Right.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

			line50Right.setTextSize(10f);

			yAxisLeft.removeAllLimitLines();
			yAxisLeft.addLimitLine(line50Left);
			yAxisLeft.addLimitLine(line50Right);
			yAxisLeft.addLimitLine(line100Left);
			yAxisLeft.addLimitLine(line100Right);
			// yAxisLeft.addLimitLine(line150Left);
			// yAxisLeft.addLimitLine(line150Right);

			// yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
			// yAxisLeft.setDrawZeroLine(false);
			yAxisLeft.setDrawGridLines(false);

			YAxis yAxisRight = lineChart.getAxisRight();
			yAxisRight.setDrawAxisLine(false);
			yAxisRight.setDrawGridLines(false);
			// yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
			yAxisRight.setValueFormatter(new IAxisValueFormatter() {

				@Override
				public String getFormattedValue(float value, AxisBase axis){
					// return value + "%";
					return "";
				}
			});

			Description description = new Description();
			description.setText("");
			lineChart.setDescription(description);
			lineChart.setVisibleXRangeMaximum(maxVisibleRange);
			lineChart.setScaleMinima(0f, 0f);
			lineChart.fitScreen();
			lineChart.invalidate(); // refresh
		}

	}

	private void showGoalAchieveDialog(){
		String title = getString(R.string.achieve_dialog_title);
		String content = getString(R.string.achieve_dialog_content);
		showNoticeDialog(title, content, Color.RED);
	}

	private void showGoalWarningDialog(){
		String title = getString(R.string.achieve_warning_dialog_title);
		String content = getString(R.string.achieve_warning_dialog_content);
		showNoticeDialog(title, content, 0);
	}

	private void showNoticeDialog(String title, String content, int titleTextColor){
		wfDialog = WfDialog.makeDialogNotice(activity, title, content, titleTextColor);
		wfDialog.show();
	}

	private void onClickDateIcon(){
		datePickerDialog.show();
	}

	private void onClickSaveButton(){
		String dateStr = txtSelectedDate.getText().toString();
		String todayActual = edtActualToday.getText().toString().replaceAll("[^\\d.]", "");
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetDate", dateStr);
			jsonObject.put("todayActual", todayActual);
			jsonObject.put("targetGroupId", selectedGroup.key);

		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(DRConst.API_KPI_PERSONAL_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(getView() != null){
			loadPersonalInfo();
		}
	}

	private void loadPersonalInfo(){
		String dateStr = txtSelectedDate.getText().toString();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetGroupId", drGroupHeader.getSelectedGroup().key);
			jsonObject.put("targetDate", dateStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_PERSONAL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			if(DRConst.API_KPI_GROUPS.equals(url)){
				onLoadGroupsSuccess(response);
			}else if(DRConst.API_KPI_PERSONAL.equals(url)){
				onGetPersonalSuccess(response);
			}else{
				super.successLoad(response, url);
			}

		}
	}

	private void onLoadGroupsSuccess(JSONObject response){
		groupKpiList = CCJsonUtil.convertToModelList(response.optString("groups"), GroupKpi.class);
		if(CCCollectionUtil.isEmpty(groupKpiList)){
			getView().findViewById(R.id.lnr_fragment_action_plan_main).setVisibility(View.GONE);
			getView().findViewById(R.id.txt_fragment_action_plan_empty).setVisibility(View.VISIBLE);
		}else{
			// groupKpiList = createDummy();
			drGroupHeader.buildLayout(groupKpiList, 0, true);
			selectedGroup = drGroupHeader.getSelectedGroup();
			loadPersonalInfo();
		}
	}

	private void onGetPersonalSuccess(JSONObject response){
		personal = CCJsonUtil.convertToModel(response.optString("personal"), Personal.class);
		buildPersonalGoalInfo(personal);
		checkToShowNoticeDialogs();

		getView().findViewById(R.id.lnr_fragment_action_plan_main).setVisibility(View.VISIBLE);

		buildChart(lineChart, personal);
	}

	private void checkToShowNoticeDialogs(){
		Calendar c = Calendar.getInstance();
		for(Progress progress : personal.progressList){
			if(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, c.getTime()).equals(progress.checkPointDate)){
				if(Integer.parseInt(progress.achievement) <= Integer.parseInt(personal.achievement)){ // success
					showGoalAchieveDialog();
				}else{
					showGoalWarningDialog();
				}
			}
		}
	}

	private void buildPersonalGoalInfo(Personal personal){
		if(!CCStringUtil.isEmpty(personal.todayActual)){// already enter
			lnrInfo.setBackgroundColor(ContextCompat.getColor(activity, R.color.wf_app_color_base_50));
			updateHeader(getString(R.string.fragment_kpi_title_tassei));
		}else{
			updateHeader(getString(R.string.fragment_kpi_title_jisseki));
			lnrInfo.setBackgroundColor(ContextCompat.getColor(activity, R.color.chiase_white));
		}
		personal.todayActual = personal.todayActual == null ? "0" : personal.todayActual;
		edtActualToday.setText(personal.todayActual + " " + selectedGroup.unit);
		String periodString = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(personal.startDate, WelfareConst.WF_DATE_TIME)) + " ~ " + CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(personal.endDate, WelfareConst.WF_DATE_TIME));
		txtPeriod.setText(periodString);
		txtGoal.setText(CCFormatUtil.formatAmount(personal.goal) + " " + selectedGroup.unit);
		txtActualTotal.setText(CCFormatUtil.formatAmount(personal.achievement) + " " + selectedGroup.unit);
		txtAchievementRate.setText(CCFormatUtil.formatAmount(personal.achievementRate) + "%");
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
