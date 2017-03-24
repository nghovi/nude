package trente.asia.shiftworking.services.worktime;

import java.util.ArrayList;
import java.util.Date;
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
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.shiftworking.services.worktime.model.WorkingTimeDailyModel;
import trente.asia.shiftworking.services.worktime.view.WorkerAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorkerFragment extends AbstractSwFragment{

	private ListView		lsvWorker;
	private CommonMonthView	monthView;
	private WorkerAdapter	mAdapter;

    public void setMonthView(CommonMonthView monthView) {
        this.monthView = monthView;
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_worker, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void initView(){
		super.initView();

		lsvWorker = (ListView)getView().findViewById(R.id.lsv_id_worker);
        mAdapter = new WorkerAdapter(activity, new ArrayList<WorkingTimeDailyModel>());
        lsvWorker.setAdapter(mAdapter);

        monthView.imgBack.setOnClickListener(this);
        monthView.imgNext.setOnClickListener(this);
        monthView.btnThisMonth.setOnClickListener(this);
	}

    public void initMonthView(CommonMonthView monthView){
        this.monthView = monthView;
        monthView.imgBack.setOnClickListener(this);
        monthView.imgNext.setOnClickListener(this);
        monthView.btnThisMonth.setOnClickListener(this);

        loadWorkerList();
    }

	@Override
	protected void initData(){
        loadWorkerList();
	}

	private void loadWorkerList(){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_WEEKDAY, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("userId", prefAccUtil.getUserPref().key);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_API_WOKER_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_API_WOKER_LIST.equals(url)){
			List<WorkingTimeDailyModel> lstWorker = CCJsonUtil.convertToModelList(response.optString("workHistory"), WorkingTimeDailyModel.class);
            mAdapter.clearAll();
			if(!CCCollectionUtil.isEmpty(lstWorker)){
				mAdapter.addAll(lstWorker);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_id_back:
			monthView.workMonth = WelfareUtil.addDate(monthView.workMonth, -1);
			loadWorkerList();
			break;
		case R.id.btn_id_next:
			monthView.workMonth = WelfareUtil.addDate(monthView.workMonth, 1);
			loadWorkerList();
			break;
		case R.id.img_id_this_month:
			monthView.workMonth = new Date();
			loadWorkerList();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		monthView = null;
		lsvWorker = null;
	}
}
