package trente.asia.shiftworking.services.worktime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.shiftworking.view.CommonMonthView;
import trente.asia.shiftworking.services.worktime.model.NoticeModel;
import trente.asia.shiftworking.services.worktime.view.NoticeAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

public class WorknoticeListFragment extends AbstractSwFragment{

	private ListView		lsvNotice;
	private CommonMonthView	monthView;
	private NoticeAdapter	mAdapter;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_notice_list, container, false);
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

		lsvNotice = (ListView)getView().findViewById(R.id.lsv_id_notice);
		lsvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				NoticeModel noticeModel = (NoticeModel)parent.getItemAtPosition(position);
				WorknoticeFormFragment fragment = new WorknoticeFormFragment();
				fragment.setNoticeModel(noticeModel);
				gotoFragment(fragment);
			}
		});

		mAdapter = new NoticeAdapter(activity, new ArrayList<NoticeModel>());
		lsvNotice.setAdapter(mAdapter);

		swipeRfresh = (SwipeRefreshLayout)getView().findViewById(R.id.swp_id_notice);
		swipeRfresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh(){
				loadNoticeList(false);
			}
		});
	}

	@Override
	public void initData(){
	}

    public void initMonthView(CommonMonthView monthView){
        this.monthView = monthView;
        monthView.imgBack.setOnClickListener(this);
        monthView.imgNext.setOnClickListener(this);
        monthView.btnThisMonth.setOnClickListener(this);

        loadNoticeList(true);
    }

	private void loadNoticeList(boolean isLoading){
		monthView.txtMonth.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_10, monthView.workMonth));
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("userId", prefAccUtil.getUserPref().key);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, monthView.workMonth));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_NOTICE_0001, jsonObject, isLoading);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_NOTICE_0001.equals(url)){
			List<NoticeModel> lstNotice = CCJsonUtil.convertToModelList(response.optString("notifications"), NoticeModel.class);
			mAdapter.clearAll();
			if(!CCCollectionUtil.isEmpty(lstNotice)){
				mAdapter.addAll(lstNotice);
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
            loadNoticeList(true);
			break;
		case R.id.btn_id_next:
			monthView.workMonth = WelfareUtil.addDate(monthView.workMonth, 1);
            loadNoticeList(true);
			break;
		case R.id.img_id_this_month:
			monthView.workMonth = new Date();
            loadNoticeList(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		monthView = null;
		lsvNotice = null;
	}
}
