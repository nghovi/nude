package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringEditUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.kpi.model.Personal;
import trente.asia.dailyreport.view.DRGroupHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class UserActualFragment extends AbstractDRFragment{

	private LayoutInflater		inflater;
	private DatePickerDialog	datePickerDialog;
	private TextView			txtSelectedDate;
	private DRGroupHeader		drGroupHeader;
	private WfDialog			wfDialog;
	private List<GroupKpi>		groupKpiList;
	LineChart					lineChart;
	private LinearLayout		lnrInfo;
	private GroupKpi			selectedGroup;
	private Map<Float, String>	formattedValuesMap	= new HashMap<>();

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
		requestPersonalGoal();
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(null, getString(R.string.fragment_kpi_title), null);

		Calendar calendar = Calendar.getInstance();
		txtSelectedDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
		txtSelectedDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendar.getTime()));
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				requestPersonalGoal();
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
			}

			@Override
			public void onNowGroupClicked(GroupKpi selectedGroup){
				GroupActualFragment groupActualFragment = new GroupActualFragment();
				groupActualFragment.setGroupKpi(selectedGroup);
				((WelfareActivity)activity).addFragment(groupActualFragment);
			}
		});
		groupKpiList = createDummy();
		drGroupHeader.buildLayout(groupKpiList, 0);
		selectedGroup = drGroupHeader.getSelectedGroup();

		lnrInfo = (LinearLayout)getView().findViewById(R.id.lnr_fragment_user_actual_info);

		buildChart();
	}

	private void buildChart(){
		lineChart = (LineChart)getView().findViewById(R.id.chart);
		List<Entry> entries = new ArrayList<Entry>();
		Calendar c = Calendar.getInstance();

		Date lastCheckPointDate = CCDateUtil.makeDateCustom(selectedGroup.checkPointList.get(selectedGroup.checkPointList.size() - 1).date, WelfareConst.WF_DATE_TIME_DATE);
		Date firstCheckPointDate = CCDateUtil.makeDateCustom(selectedGroup.checkPointList.get(0).date, WelfareConst.WF_DATE_TIME_DATE);
		int averageDistance = (int)((lastCheckPointDate.getTime() - firstCheckPointDate.getTime()) / selectedGroup.checkPointList.size());

		for(GroupKpi.CheckPoint checkPoint : selectedGroup.checkPointList){
			Date checkPointDate = CCDateUtil.makeDateCustom(checkPoint.date, WelfareConst.WF_DATE_TIME_DATE);
			float x = (checkPointDate.getTime() - firstCheckPointDate.getTime()) / averageDistance;
			formattedValuesMap.put(x, checkPoint.date);
			Entry t = new Entry(x, Float.valueOf(checkPoint.achievement));
			entries.add(t);
		}
		LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
		dataSet.setColor(Color.RED);
		dataSet.setValueTextColor(Color.BLUE); // styling, ...
		dataSet.setValueTextSize(11);
		dataSet.setLabel("Group checkpoint achievement");

		LineData lineData = new LineData();
		lineData.addDataSet(dataSet);
		lineChart.setData(lineData);

		IAxisValueFormatter formatter = new IAxisValueFormatter() {

			@Override
			public String getFormattedValue(float value, AxisBase axis){
				String dateStr = formattedValuesMap.get(value);
				return CCStringUtil.isEmpty(dateStr) ? "" : dateStr;
			}

		};

		XAxis xAxis = lineChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setTextSize(10f);
		xAxis.setTextColor(Color.BLACK);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(false);

		YAxis yAxisLeft = lineChart.getAxisLeft();
		// yAxisLeft.setZeroLineColor(Color.RED);
		// yAxisLeft.setDrawZeroLine(true);
		// yAxisLeft.setZeroLineWidth(10);
		yAxisLeft.setValueFormatter(new IAxisValueFormatter() {

			@Override
			public String getFormattedValue(float value, AxisBase axis){
				return String.valueOf((int)value * Integer.valueOf(selectedGroup.goal) + "円");
			}
		});

		LimitLine upper_limit = new LimitLine(100f, "");
		upper_limit.setLineWidth(0.5f);
		upper_limit.setLineColor(Color.RED);
		// upper_limit.enableDashedLine(10f, 10f, 0f);
		upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
		upper_limit.setTextSize(10f);

		yAxisLeft.removeAllLimitLines();
		yAxisLeft.addLimitLine(upper_limit);
		// yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
		// yAxisLeft.setDrawZeroLine(false);
		// yAxisLeft.setDrawGridLines(false);

		YAxis yAxisRight = lineChart.getAxisRight();
		// yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
		yAxisRight.setValueFormatter(new IAxisValueFormatter() {

			@Override
			public String getFormattedValue(float value, AxisBase axis){
				return value + "%";
			}
		});

		// xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
		xAxis.setValueFormatter(formatter);
		xAxis.setLabelRotationAngle(-45f);

		// set a custom value formatter
		// xAxis.setValueFormatter(new MyCustomFormatter());
		// and more...
		Description description = new Description();
		description.setText("");
		lineChart.setDescription(description);
		lineChart.setVisibleXRangeMaximum(4);
		lineChart.invalidate(); // refresh
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
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("Test", "test");
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(DRConst.API_KPI_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(getView() != null){

		}
	}

	private void requestPersonalGoal(){
		String dateStr = txtSelectedDate.getText().toString();
		UserModel userMe = prefAccUtil.getUserPref();
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
			// groupKpiList = CCJsonUtil.convertToModelList(response.optString("groups"), GroupKpi.class);
			Personal personal = CCJsonUtil.convertToModel(response.optString("personal"), Personal.class);
			buildPersonalGoalInfo(personal);
			showGoalAchieveDialog();
			showGoalWarningDialog();

			if(true){
				lnrInfo.setBackgroundColor(ContextCompat.getColor(activity, R.color.wf_app_color_base_50));
			}else{
				lnrInfo.setBackgroundColor(ContextCompat.getColor(activity, R.color.chiase_white));
			}
		}
	}

	private List<GroupKpi> createDummy(){
		List<GroupKpi.CheckPoint> checkPointList = new ArrayList<>();
		GroupKpi.CheckPoint cp1 = new GroupKpi.CheckPoint();
		cp1.date = "2017/05/29";
		cp1.achievement = "25";

		GroupKpi.CheckPoint cp2 = new GroupKpi.CheckPoint();
		cp2.date = "2017/06/29";
		cp2.achievement = "50";

		GroupKpi.CheckPoint cp3 = new GroupKpi.CheckPoint();
		cp3.date = "2017/07/29";
		cp3.achievement = "120";

		checkPointList.add(0, cp1);
		checkPointList.add(1, cp2);
		checkPointList.add(2, cp3);

		List<GroupKpi> groupKpis = new ArrayList<>();
		GroupKpi g1 = new GroupKpi();
		g1.name = "g1";
		g1.key = "1";
		g1.goal = "150000";
		g1.checkPointList = checkPointList;
		GroupKpi g2 = new GroupKpi();
		g2.name = "g2";
		g2.key = "2";
		g2.goal = "100000";
		g2.checkPointList = checkPointList;
		GroupKpi g3 = new GroupKpi();
		g3.name = "g3";
		g3.key = "3";
		g3.goal = "550000";
		g3.checkPointList = checkPointList;
		groupKpis.add(g1);
		groupKpis.add(g2);
		groupKpis.add(g3);
		return groupKpis;
	}

	private void buildPersonalGoalInfo(Personal personal){
		String periodString = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(personal.startDate, WelfareConst.WF_DATE_TIME)) + " ~ " + CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDateCustom(personal.endDate, WelfareConst.WF_DATE_TIME));
		((TextView)getView().findViewById(R.id.txt_fragment_user_actual_period)).setText(periodString);
		((TextView)getView().findViewById(R.id.txt_fragment_user_actual_goal)).setText(CCFormatUtil.formatAmount(personal.goal));
		((TextView)getView().findViewById(R.id.txt_fragment_user_actual_total)).setText(CCFormatUtil.formatAmount(personal.todayActual));
		((TextView)getView().findViewById(R.id.txt_fragment_user_actual_achievement_rate)).setText(CCFormatUtil.formatAmount(personal.achievementRate));
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
