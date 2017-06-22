package trente.asia.dailyreport.services.kpi;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.fragments.AbstractDRFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by viet on 2/15/2016.
 */
public class GroupKpiFragment extends AbstractDRFragment {

	private LayoutInflater inflater;
	private DatePickerDialog datePickerDialog;
	private TextView txtSelectedDate;

	@Override
	public int getFragmentLayoutId() {
		return R.layout.fragment_kpi_group;
	}

	@Override
	public int getFooterItemId() {
		return R.id.lnr_view_common_footer_myreport;
	}

	@Override
	public void initData() {
		// requestDailyReportSingle(calendarHeader.getSelectedDate());
	}

	@Override
	public void buildBodyLayout() {
		super.initHeader(null, getString(R.string.fragment_kpi_title), null);

		Calendar calendar = Calendar.getInstance();
		txtSelectedDate = (TextView) getView().findViewById(R.id.txt_fragment_kpi_date);
		txtSelectedDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, calendar.getTime()));
		datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				String startDateStr = year + "/" + CCFormatUtil.formatZero(month + 1) + "/" + CCFormatUtil.formatZero(dayOfMonth);
				txtSelectedDate.setText(startDateStr);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		getView().findViewById(R.id.lnr_fragment_kpi_date).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickDateIcon();
			}
		});

		// group button
		getView().findViewById(R.id.btn_calendar_header_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickPreviousGroup();
			}
		});
		getView().findViewById(R.id.btn_calendar_header_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickNextGroup();
			}
		});
		getView().findViewById(R.id.img_calendar_header_this_group).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickThisGroupButton();
			}
		});

		// save button
		getView().findViewById(R.id.btn_fragment_kpi_save).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickSaveButton();
			}
		});

	}

	private void onClickNextGroup() {
		//// TODO: 6/22/17
	}

	private void onClickPreviousGroup() {
		//// TODO: 6/22/17
	}

	private void onClickDateIcon() {
		datePickerDialog.show();
	}

	private void onClickSaveButton() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Test", "test");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestUpdate(DRConst.API_KPI_UPDATE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url) {
		if (getView() != null) {

		}
	}

	public void onClickThisGroupButton() {
		GroupKpiFragment groupKpiFragment = new GroupKpiFragment();
		((WelfareActivity) activity).addFragment(groupKpiFragment);
	}

	private void requestDailyReportSingle(Date date) {
		String monthStr = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, date);
		UserModel userMe = prefAccUtil.getUserPref();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("targetUserId", userMe.key);
			jsonObject.put("targetDeptId", userMe.dept.key);
			jsonObject.put("targetMonth", monthStr);
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		super.requestLoad(DRConst.API_KPI_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url) {
		if (getView() != null) {

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
