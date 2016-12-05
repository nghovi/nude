package trente.asia.shiftworking.services.transit;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.transit.model.TransitModelHolder;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WfUrlConst;

public class WorkTransitDetailFragment extends AbstractSwFragment{

	private String		activeTransitId;
	private TextView	txtLeave;
	private TextView	txtArrive;
	private TextView	txtFee;
	private TextView	txtNote;

	private TextView	txtTransitType;
	private TextView	txtWayType;
	private TextView	txtCostType;

	private ImageView	imgPhoto;

	public void setActiveTransitId(String activeTransitId){
		this.activeTransitId = activeTransitId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_transit_detail, container, false);
		}
		return mRootView;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_work_time;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.wf_back_white, myself.userName, null);

		txtLeave = (TextView)getView().findViewById(R.id.txt_id_leave);
		txtArrive = (TextView)getView().findViewById(R.id.txt_id_arrive);
		txtFee = (TextView)getView().findViewById(R.id.txt_id_fee);
		txtNote = (TextView)getView().findViewById(R.id.txt_id_note);

		txtTransitType = (TextView)getView().findViewById(R.id.txt_id_transit_type);
		txtWayType = (TextView)getView().findViewById(R.id.txt_id_way_type);
		txtCostType = (TextView)getView().findViewById(R.id.txt_id_cost_type);

		imgPhoto = (ImageView)getView().findViewById(R.id.img_id_photo);
//		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);

		imgPhoto.setOnClickListener(this);
//		imgRightIcon.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		loadTransitForm();
	}

	private void loadTransitForm(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeTransitId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_TRANS_0002, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_TRANS_0002.equals(url)){

            String summaryStatus = response.optString("summaryStatus");
            if(!SwConst.SW_TRANSIT_STATUS_DONE.equals(summaryStatus)){
                ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
                imgRightIcon.setImageResource(R.drawable.cs_dummy_small);
                imgRightIcon.setVisibility(View.VISIBLE);
                imgRightIcon.setOnClickListener(this);
            }

			TransitModelHolder holder = CCJsonUtil.convertToModel(CCStringUtil.toString(response), TransitModelHolder.class);
			if(holder.transit != null){
				txtLeave.setText(holder.transit.transLeave);
				txtArrive.setText(holder.transit.transArrive);
				txtTransitType.setText(holder.transit.transTypeName);
				txtWayType.setText(holder.transit.wayTypeName);

				txtFee.setText(CCFormatUtil.formatAmount(holder.transit.fee));
				txtCostType.setText(holder.transit.costTypeName);
				txtNote.setText(holder.transit.note);
			}
		}else{
			super.successLoad(response, url);
		}
	}

    @Override
    public void onResume(){
        super.onResume();

        if(!((WelfareActivity)activity).dataMap.isEmpty()){
            String isDelete = CCStringUtil.toString(((WelfareActivity)activity).dataMap.get(SwConst.ACTION_TRANSIT_DELETE));
            String isUpdate = CCStringUtil.toString(((WelfareActivity)activity).dataMap.get(SwConst.ACTION_TRANSIT_UPDATE));
            if(CCConst.YES.equals(isDelete) || CCConst.YES.equals(isUpdate)){
                ((WelfareActivity)activity).dataMap.clear();
                ((WelfareActivity)activity).isInitData = true;
                if(CCConst.YES.equals(isDelete)){
                    getFragmentManager().popBackStack();
                }
            }
        }
    }

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			WorkTransitFormFragment fragment = new WorkTransitFormFragment();
			fragment.setActiveTransitId(activeTransitId);
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
