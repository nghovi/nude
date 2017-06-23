package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

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
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class UserActualFragment extends AbstractDRFragment{

	private LayoutInflater		inflater;
	private DatePickerDialog	datePickerDialog;
	private TextView			txtSelectedDate;
	private DRGroupHeader		drGroupHeader;

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
				((WelfareActivity)activity).addFragment(groupActualFragment);
			}
		});
		List<GroupKpi> groupKpis = new ArrayList<>();
		groupKpis.add(new GroupKpi());
		groupKpis.add(new GroupKpi());
		drGroupHeader.buildLayout(groupKpis, 0);
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
			jsonObject.put("targetGroupId", userMe.key);
			jsonObject.put("targetDate", dateStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_PERSONAL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			List<GroupKpi> groupKpiList = CCJsonUtil.convertToModelList(response.optString("groups"), GroupKpi.class);
			drGroupHeader.buildLayout(groupKpiList, 0);
			Personal personal = CCJsonUtil.convertToModel(response.optString("personal"), Personal.class);
			buildPersonalGoalInfo(personal);
		}
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
