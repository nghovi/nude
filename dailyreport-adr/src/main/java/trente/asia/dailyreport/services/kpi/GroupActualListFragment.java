package trente.asia.dailyreport.services.kpi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.dailyreport.services.kpi.view.GroupActualListAdapter;
import trente.asia.dailyreport.view.DRGroupHeader;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class GroupActualListFragment extends AbstractDRFragment{

	private ListView				lstGroupActual;
	private GroupActualListAdapter	adapter;
	private DatePickerDialog		datePickerDialog;
	private TextView				txtSelectedDate;

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_group_actual_list;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_kpi;
	}

	@Override
	public void initData(){
		requestKpiGroups();
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_group_actual_list_title), null);
		lstGroupActual = (ListView)getView().findViewById(R.id.lst_group_actual);

		Calendar calendar = Calendar.getInstance();
		txtSelectedDate = (TextView)getView().findViewById(R.id.txt_fragment_kpi_date);
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

	}

	private void requestKpiGroups(){
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
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			List<GroupKpi> groupKpiList = CCJsonUtil.convertToModelList(response.optString("groups"), GroupKpi.class);
			adapter = new GroupActualListAdapter(getContext(), R.layout.item_group_actual, groupKpiList);
			lstGroupActual.setAdapter(adapter);
		}
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

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
