package nguyenhoangviet.vpcorp.shiftworking.services.shiftworking;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.model.ShiftWorkingModel;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.model.WorkHistoryModel;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view.CommonMonthView;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view.ShiftWorkingAdapter;
import nguyenhoangviet.vpcorp.shiftworking.services.worktime.model.WorkingTimeSummaryModel;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

public class ShiftWorkingFragment extends AbstractSwFragment{

	private ListView			lsvShiftWorking;
	private CommonMonthView		monthView;
	private ShiftWorkingModel	shiftWorkingModel;

	private ShiftWorkingAdapter	mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_shift_working, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_shift_working;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, myself.userName, null);

		lsvShiftWorking = (ListView)getView().findViewById(R.id.lsv_id_shift_working);
		monthView = (CommonMonthView)getView().findViewById(R.id.view_id_month);
		monthView.initialization();

		mAdapter = new ShiftWorkingAdapter(activity, new ArrayList<WorkHistoryModel>());
		lsvShiftWorking.setAdapter(mAdapter);

		monthView.imgBack.setOnClickListener(this);
		monthView.imgNext.setOnClickListener(this);
		monthView.btnThisMonth.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		loadWorkHistory();
	}

	private void loadWorkHistory(){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", prefAccUtil.getUserPref().key);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(SwConst.API_WORK_HISTORY, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_WORK_HISTORY.equals(url)){
			shiftWorkingModel = CCJsonUtil.convertToModel(CCStringUtil.toString(response), ShiftWorkingModel.class);
			mAdapter.clearAll();

			List<WorkHistoryModel> lstHistory = new ArrayList<>();
			lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_TITLE, getString(R.string.sw_shift_work_summary_title)));
			if(shiftWorkingModel.workSummary != null){
				loadSummary(lstHistory, shiftWorkingModel.workSummary);
			}

			lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_TITLE, getString(R.string.sw_shift_work_history)));
			if(!CCCollectionUtil.isEmpty(shiftWorkingModel.workHistory)){
				lstHistory.addAll(shiftWorkingModel.workHistory);
			}

			mAdapter.addAll(lstHistory);
		}else{
			super.successLoad(response, url);
		}
	}

	private void loadSummary(List<WorkHistoryModel> lstHistory, WorkingTimeSummaryModel summaryModel){
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_item_status), summaryModel.statusName));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_regular_day), summaryModel.regularDays));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_working_day), summaryModel.workingDays));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_paid_vacation), summaryModel.countPaidVacation));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_special_vacation), summaryModel.countSpcialVacation));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_compensatory_holiday), summaryModel.countCompVacation));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_item_over_time), summaryModel.timeOver));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_short_time), summaryModel.timeShort));
        lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_holiday_working), summaryModel.timeHoliday));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_be_late), summaryModel.countBeLate));
		lstHistory.add(new WorkHistoryModel(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY, getString(R.string.sw_shift_work_leave_early), summaryModel.countLeaveEarly));
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_id_back:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, -1);
			loadWorkHistory();
			break;
		case R.id.btn_id_next:
			monthView.workMonth = WelfareUtil.addMonth(monthView.workMonth, 1);
			loadWorkHistory();
			break;
		case R.id.img_id_this_month:
			monthView.workMonth = WelfareUtil.makeMonthWithFirstDate();
			loadWorkHistory();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		lsvShiftWorking = null;
	}
}
