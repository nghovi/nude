package trente.asia.shiftworking.services.transit;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.AndroidUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.android.view.ChiaseTextView;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractPhotoFragment;
import trente.asia.shiftworking.services.transit.activity.PlaceHistoryActivity;
import trente.asia.shiftworking.services.transit.model.TransitModel;
import trente.asia.shiftworking.services.transit.model.TransitModelHolder;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

public class WorkTransitFormFragment extends AbstractPhotoFragment{

	private String				activeTransitId;
	private ChiaseTextView		txtTransitType;
	private ChiaseTextView		txtWayType;
	private ChiaseTextView		txtCostType;

	private ImageView			activePhoto;
	private LinearLayout		lnrAttachment;
	private LinearLayout		lnrAdd;
	private Button				btnDelete;

	private EditText			edtLeave;
	private EditText			edtArrive;
	private int					numberAttachment	= 0;

	private LinearLayout		lnrTransitType;
	private LinearLayout		lnrWayType;
	private LinearLayout		lnrCostType;

	private ChiaseListDialog	dlgTransitType;
	private ChiaseListDialog	dlgWayType;
	private ChiaseListDialog	dlgCostType;

	private TransitModelHolder	mHolder;
	private final int			MAX_ATTACHMENT		= 3;
	private final int			ENTRY_LEAVE			= 201;
	private final int			ENTRY_ARRIVE		= 202;

	public void setActiveTransitId(String activeTransitId){
		this.activeTransitId = activeTransitId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_work_transit_form, container, false);
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
		super.initHeader(R.drawable.wf_back_white, myself.userName, R.drawable.cs_dummy_small);

		txtTransitType = (ChiaseTextView)getView().findViewById(R.id.txt_id_transit_type);
		txtWayType = (ChiaseTextView)getView().findViewById(R.id.txt_id_way_type);
		txtCostType = (ChiaseTextView)getView().findViewById(R.id.txt_id_cost_type);

		lnrAttachment = (LinearLayout)getView().findViewById(R.id.lnr_id_attachment);
		lnrAdd = (LinearLayout)getView().findViewById(R.id.lnr_id_add);
		btnDelete = (Button)getView().findViewById(R.id.btn_id_delete);
		ImageView imgRightIcon = (ImageView)getView().findViewById(R.id.img_id_header_right_icon);

		lnrTransitType = (LinearLayout)getView().findViewById(R.id.lnr_id_transit_type);
		lnrWayType = (LinearLayout)getView().findViewById(R.id.lnr_id_way_type);
		lnrCostType = (LinearLayout)getView().findViewById(R.id.lnr_id_cost_type);

		lnrTransitType.setOnClickListener(this);
		lnrWayType.setOnClickListener(this);
		lnrCostType.setOnClickListener(this);
		imgRightIcon.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		lnrAdd.setOnClickListener(this);

		if(!CCStringUtil.isEmpty(activeTransitId)){
			btnDelete.setVisibility(View.VISIBLE);
		}
		initPlaceEditText();
	}

	private void initDialog(TransitModelHolder holder){
		dlgTransitType = new ChiaseListDialog(activity, getString(R.string.sw_work_transit_trans_type_item), WelfareFormatUtil.convertList2Map(holder.transTypes), txtTransitType, null);
		dlgWayType = new ChiaseListDialog(activity, getString(R.string.sw_work_transit_way_type_item), WelfareFormatUtil.convertList2Map(holder.wayTypes), txtWayType, null);
		dlgCostType = new ChiaseListDialog(activity, getString(R.string.sw_work_transit_cost_type_item), WelfareFormatUtil.convertList2Map(holder.costTypes), txtCostType, null);

		if(CCStringUtil.isEmpty(activeTransitId)){
			// set default value
			txtTransitType.setText(holder.transTypes.get(0).value);
			txtTransitType.setValue(holder.transTypes.get(0).key);
			txtWayType.setText(holder.wayTypes.get(0).value);
			txtWayType.setValue(holder.wayTypes.get(0).key);
			txtCostType.setText(holder.costTypes.get(0).value);
			txtCostType.setValue(holder.costTypes.get(0).key);
		}
	}

	private void initPlaceEditText(){
		edtLeave = (EditText)getView().findViewById(R.id.edt_id_leave);
		edtArrive = (EditText)getView().findViewById(R.id.edt_id_arrive);

		edtLeave.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus){
				if(hasFocus){
					if(!CCCollectionUtil.isEmpty(mHolder.historyNames)){
						Intent intent = new Intent(activity, PlaceHistoryActivity.class);
						Bundle bundle = new Bundle();

						bundle.putSerializable(SwConst.KEY_HISTORY_LIST, (Serializable)mHolder.historyNames);
						bundle.putString(SwConst.KEY_HISTORY_NAME, CCStringUtil.toString(edtLeave.getText()));
						intent.putExtras(bundle);
						startActivityForResult(intent, ENTRY_LEAVE);
					}
				}
			}
		});

		edtArrive.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus){
				if(hasFocus){
					if(!CCCollectionUtil.isEmpty(mHolder.historyNames)){
						Intent intent = new Intent(activity, PlaceHistoryActivity.class);
						Bundle bundle = new Bundle();

						bundle.putSerializable(SwConst.KEY_HISTORY_LIST, (Serializable)mHolder.historyNames);
						bundle.putString(SwConst.KEY_HISTORY_NAME, CCStringUtil.toString(edtArrive.getText()));
						intent.putExtras(bundle);
						startActivityForResult(intent, ENTRY_ARRIVE);
					}
				}
			}
		});
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
			mHolder = CCJsonUtil.convertToModel(CCStringUtil.toString(response), TransitModelHolder.class);
			initDialog(mHolder);
			if(!CCStringUtil.isEmpty(activeTransitId)){
				loadTransit(mHolder.transit);
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void loadTransit(TransitModel transitModel){
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
		try{
			Gson gson = new Gson();
			CAObjectSerializeUtil.deserializeObject(lnrContent, new JSONObject(gson.toJson(transitModel)));
			txtTransitType.setText(transitModel.transTypeName);
			txtWayType.setText(transitModel.wayTypeName);
			txtCostType.setText(transitModel.costTypeName);
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	private void updateTransit(){
		Map<String, File> photoMap = new HashMap<>();
		if(!CCStringUtil.isEmpty(mOriginalPath)){
			photoMap.put("photo", new File(mOriginalPath));
		}
		LinearLayout lnrContent = (LinearLayout)getView().findViewById(R.id.lnr_id_content);
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(lnrContent, null);
		try{
			jsonObject.put("key", activeTransitId);
			jsonObject.put("userId", myself.key);
			jsonObject.put("transDate", CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_7, new Date()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpload(WfUrlConst.WF_TRANS_0003, jsonObject, photoMap, true);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		if(WfUrlConst.WF_TRANS_0003.equals(url)){
			((WelfareActivity)activity).isInitData = true;
			((WelfareActivity)activity).dataMap.put(SwConst.ACTION_TRANSIT_UPDATE, CCConst.YES);
			getFragmentManager().popBackStack();
		}else{
			super.successLoad(response, url);
		}
	}

	private void deleteTransit(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeTransitId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_TRANS_0004, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_TRANS_0004.equals(url)){
			getFragmentManager().popBackStack();
			((WelfareActivity)activity).dataMap.put(SwConst.ACTION_TRANSIT_DELETE, CCConst.YES);
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);
		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case WelfareConst.RequestCode.PHOTO_CHOOSE:
			if(returnedIntent != null){
				String detailMessage = returnedIntent.getExtras().getString("detail");
				if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
					alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
					alertDialog.show();
				}else{
					mOriginalPath = returnedIntent.getExtras().getString(WelfareConst.IMAGE_PATH_KEY);
					Uri uri = AndroidUtil.getUriFromFileInternal(activity, new File(mOriginalPath));
					activePhoto.setImageURI(uri);
				}
			}

			break;

		case ENTRY_LEAVE:
			Bundle bundle = returnedIntent.getExtras();
			String leave = (String)bundle.getSerializable(SwConst.KEY_HISTORY_NAME);
			edtLeave.setText(leave);
			edtLeave.clearFocus();
			break;
		case ENTRY_ARRIVE:
			Bundle bundle1 = returnedIntent.getExtras();
			String arrive = (String)bundle1.getSerializable(SwConst.KEY_HISTORY_NAME);
			edtArrive.setText(arrive);
			edtArrive.clearFocus();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_id_transit_type:
			dlgTransitType.show();
			break;
		case R.id.lnr_id_way_type:
			dlgWayType.show();
			break;
		case R.id.lnr_id_cost_type:
			dlgCostType.show();
			break;
		case R.id.img_id_photo:
			// menuManager.openMenu(imgPhoto1);
			mViewForMenuBehind.setVisibility(View.VISIBLE);
			break;
		case R.id.img_id_header_right_icon:
			updateTransit();
			break;
		case R.id.btn_id_delete:
			deleteTransit();
			break;
		case R.id.lnr_id_add:
			addAttachment();
			break;
		default:
			break;
		}
	}

	private void addAttachment(){
		numberAttachment++;
		judgeAdd();
		LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		final View view = mInflater.inflate(R.layout.item_attachment_list, null);
		ImageView imgDelete = (ImageView)view.findViewById(R.id.img_id_photo_delete);
		imgDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				lnrAttachment.removeView(view);
				numberAttachment--;
				judgeAdd();
			}
		});

		final ImageView imgPhoto = (ImageView)view.findViewById(R.id.img_id_photo);
		imgPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				activePhoto = imgPhoto;
				menuManager.openMenu(imgPhoto);
				mViewForMenuBehind.setVisibility(View.VISIBLE);
			}
		});

		lnrAttachment.addView(view);
	}

	private void judgeAdd(){
		if(numberAttachment >= MAX_ATTACHMENT){
			lnrAdd.setEnabled(false);
		}else{
			lnrAdd.setEnabled(true);
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
