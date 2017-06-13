package trente.asia.shiftworking.services.transit;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.BuildConfig;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.transit.model.TransitModelHolder;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.ImageAttachmentModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

public class WorkTransitDetailFragment extends AbstractSwFragment{

	private String			activeTransitId;
	private TextView		txtLeave;
	private TextView		txtArrive;
	private TextView		txtFee;
	private TextView		txtNote;

	private TextView		txtTransitType;
	private TextView		txtWayType;
//	private TextView		txtCostType;

	private LinearLayout	lnrAttachment;

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
		return 0;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.sw_back_white, myself.userName, null);

		txtLeave = (TextView)getView().findViewById(R.id.txt_id_leave);
		txtArrive = (TextView)getView().findViewById(R.id.txt_id_arrive);
		txtFee = (TextView)getView().findViewById(R.id.txt_id_fee);
		txtNote = (TextView)getView().findViewById(R.id.txt_id_note);

		txtTransitType = (TextView)getView().findViewById(R.id.txt_id_transit_type);
		txtWayType = (TextView)getView().findViewById(R.id.txt_id_way_type);
//		txtCostType = (TextView)getView().findViewById(R.id.txt_id_cost_type);
		lnrAttachment = (LinearLayout)getView().findViewById(R.id.lnr_id_attachment);
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
		requestLoad(SwConst.API_TRANSIT_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(SwConst.API_TRANSIT_DETAIL.equals(url)){

			String summaryStatus = response.optString("summaryStatus");
			if(!SwConst.SW_TRANSIT_STATUS_DONE.equals(summaryStatus)){
				ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);
				imgRightIcon.setImageResource(R.drawable.sw_action_edit);
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
//				txtCostType.setText(holder.transit.costTypeName);
				txtNote.setText(holder.transit.note);

                LinearLayout lnrHolder = (LinearLayout)getView().findViewById(R.id.lnr_id_attachment_holder);
				if(!CCCollectionUtil.isEmpty(holder.transit.attachmentFile)){
                    lnrHolder.setVisibility(View.VISIBLE);
					setAttachment(holder.transit.attachmentFile);
				}else{
					lnrHolder.setVisibility(View.GONE);
				}
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void setAttachment(List<ImageAttachmentModel> lstAttachment){
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        lnrAttachment.removeAllViews();
		for(ImageAttachmentModel attachmentModel : lstAttachment){
			if(attachmentModel.thumbnail != null && !CCStringUtil.isEmpty(attachmentModel.thumbnail.fileUrl)){
				View view = mInflater.inflate(R.layout.item_attachment_show_list, null);
				ImageView imgPhoto = (ImageView)view.findViewById(R.id.img_id_photo);
				WfPicassoHelper.loadImage(activity, BuildConfig.HOST + attachmentModel.thumbnail.fileUrl, imgPhoto, null);
				lnrAttachment.addView(view);
			}
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
