package trente.asia.shiftworking.services.transit;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.transit.model.TransitModel;
import trente.asia.shiftworking.services.transit.view.TransitAdapter;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;

public class WorkTransitListFragment extends AbstractSwFragment{

	private ListView		lsvTransit;
	private TransitAdapter	mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_transit_list, container, false);
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
		super.initHeader(R.drawable.sw_back_white, myself.userName, null);

		lsvTransit = (ListView)getView().findViewById(R.id.lsv_id_transit);

		lsvTransit.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				TransitModel transitModel = (TransitModel)parent.getItemAtPosition(position);
				WorkTransitDetailFragment fragment = new WorkTransitDetailFragment();
				fragment.setActiveTransitId(transitModel.key);
				gotoFragment(fragment);
			}
		});
	}

	@Override
	protected void initData(){
		loadTransitList();
	}

	private void loadTransitList(){
		Date date = new Date();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetUserId", myself.key);
			jsonObject.put("searchDateString", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, date));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_TRANS_0001, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_TRANS_0001.equals(url)){
			List<TransitModel> lstTransit = CCJsonUtil.convertToModelList(response.optString("transits"), TransitModel.class);
			if(!CCCollectionUtil.isEmpty(lstTransit)){
				mAdapter = new TransitAdapter(activity, lstTransit);
				lsvTransit.setAdapter(mAdapter);
			}else{
                if(mAdapter != null){
                    mAdapter.clearAll();
                }
            }
			String summaryStatus = response.optString("summaryStatus");
			if(!SwConst.SW_TRANSIT_STATUS_DONE.equals(summaryStatus)){
				ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
				imgRightIcon.setImageResource(R.drawable.bb_action_add);
				imgRightIcon.setVisibility(View.VISIBLE);
				imgRightIcon.setOnClickListener(this);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			WorkTransitFormFragment fragment = new WorkTransitFormFragment();
			gotoFragment(fragment);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
