package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
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

		lineChart = (LineChart)getView().findViewById(R.id.chart);
		List<Entry> entries = new ArrayList<Entry>();
		Calendar c = Calendar.getInstance();
		for(int i = 0; i < 8; i++){
			Entry t = new Entry(i, (int)(Math.random() * i));
			entries.add(t);
		}
		LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
		dataSet.setColor(Color.RED);
		dataSet.setValueTextColor(Color.BLUE); // styling, ...

		LineData lineData = new LineData();
		lineData.addDataSet(dataSet);
		lineChart.setData(lineData);

		// the labels that should be drawn on the XAxis
		final String[] quarters = new String[]{"gotosfsfskdf1", "gotosfsfskdf2", "gotosfsfskdf3", "gotosfsfskdf4", "gotosfsfskdf5", "gotosfsfskdf6", "gotosfsfskdf7", "gotosfsfskdf8"};

		IAxisValueFormatter formatter = new IAxisValueFormatter() {

			@Override
			public String getFormattedValue(float value, AxisBase axis){
				return quarters[(int)value];
			}

		};

		XAxis xAxis = lineChart.getXAxis();
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setTextSize(10f);
		xAxis.setTextColor(Color.CYAN);
		xAxis.setDrawAxisLine(true);
		xAxis.setDrawGridLines(false);

		xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
		xAxis.setValueFormatter(formatter);

		// set a custom value formatter
		// xAxis.setValueFormatter(new MyCustomFormatter());
		// and more...

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
		}
	}

	private List<GroupKpi> createDummy(){
		List<GroupKpi> groupKpis = new ArrayList<>();
		GroupKpi g1 = new GroupKpi();
		g1.name = "g1";
		g1.key = "1";
		GroupKpi g2 = new GroupKpi();
		g2.name = "g2";
		g2.key = "2";
		GroupKpi g3 = new GroupKpi();
		g3.name = "g3";
		g3.key = "3";
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
