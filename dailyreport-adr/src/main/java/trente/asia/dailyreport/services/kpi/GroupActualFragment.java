package trente.asia.dailyreport.services.kpi;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.dailyreport.services.kpi.model.GroupKpi;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class GroupActualFragment extends AbstractDRFragment{

	private LayoutInflater		inflater;
	private DatePickerDialog	datePickerDialog;
	private TextView			txtSelectedDate;
	private GroupKpi			groupKpi;

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
		requestKpiGroup();
	}

	@Override
	public void buildBodyLayout(){
		super.initHeader(R.drawable.wf_back_white, getString(R.string.fragment_group_actual_title), null);

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

		getView().findViewById(R.id.btn_fragment_group_actual_list).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onClickGroupListButton();
			}
		});

	}

	private void requestKpiGroup(){
		String dateStr = txtSelectedDate.getText().toString();
		UserModel userMe = prefAccUtil.getUserPref();
		JSONObject jsonObject = new JSONObject();
		String targetGroupId = "0";
		if(groupKpi != null){
			targetGroupId = groupKpi.key;
		}
		try{
			jsonObject.put("targetGroupId", targetGroupId);
			jsonObject.put("targetDate", dateStr);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_GROUP, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(getView() != null){
			groupKpi = CCJsonUtil.convertToModel(response.optString("groupKpi"), GroupKpi.class);
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

	public void setGroupKpi(GroupKpi groupKpi){
		this.groupKpi = groupKpi;
	}
}
