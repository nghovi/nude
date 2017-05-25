package trente.asia.addresscard.services.business.view;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardEditFragment;
import trente.asia.addresscard.databinding.DialogFragmentChooseCustomerBinding;
import trente.asia.addresscard.databinding.FragmentCardDetailEditBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerDialogAdapter;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCardEditFragment extends AddressCardEditFragment implements CustomerDialogAdapter.OnCustomerDialogListener{

	private int									customerId;
	private BusinessCardModel					card;
	private FragmentCardDetailEditBinding		binding;
	private Dialog								dialog;
	private DialogFragmentChooseCustomerBinding	viewBinding;

	public static BusinessCardEditFragment newInstance(int cardId){
		BusinessCardEditFragment fragment = new BusinessCardEditFragment();
		fragment.key = cardId;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_detail_edit, container, false);
			mRootView = binding.getRoot();
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
			binding.rltSetCustomer.setOnClickListener(this);
		}
		return mRootView;
	}

	@Override
	protected String getApiLoadData(){
		return ACConst.AC_BUSINESS_CARD_DETAIL;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		super.successLoad(response, url);
		if(ACConst.AC_BUSINESS_CUSTOMER_LIST.equals(url)){
			List<CustomerModel> customers = CCJsonUtil.convertToModelList(response.optString("customers"), CustomerModel.class);
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			viewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
					R.layout.dialog_fragment_choose_customer, null, false);
			builder.setView(viewBinding.getRoot());
			CustomerDialogAdapter adapter = new CustomerDialogAdapter(customers, customerId, this);
			viewBinding.listCustomers.setAdapter(adapter);
			viewBinding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
			viewBinding.btnNewCustomer.setOnClickListener(this);
			dialog = builder.create();
			dialog.show();
		}else if(ACConst.AC_BUSINESS_CARD_DETAIL.equals(url)){
			card = CCJsonUtil.convertToModel(response.optString("card"), BusinessCardModel.class);
			binding.setVariable(BR.card, card);
			binding.executePendingBindings();
			Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(binding.cardImage);
			customerId = card.customerId;
			super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_done);
		}
	}

	@Override
	protected void updateAddressCard(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
		try{
			jsonObject.put("key", card.key);
			jsonObject.put("customerId", customerId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		Map<String, File> fileMap = new HashMap<>();
		requestUpload(ACConst.AC_BUSINESS_CARD_UPDATE, jsonObject, fileMap, true);
	}

	@Override
	protected void onSuccessUpLoad(JSONObject response, boolean isAlert, String url) {
		Log.e("CardEdit", "response: " + response.toString());
		super.onSuccessUpLoad(response, isAlert, url);

	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);
		if(ACConst.AC_BUSINESS_CUSTOMER_CREATE.equals(url)){
			try{
				customerId = response.getInt("customerId");
			}catch(JSONException e){
				e.printStackTrace();
			}

			binding.customerName.setText(viewBinding.edtNewCustomer.getText());
			dialog.dismiss();
		}
	}

	@Override
	public void onSelectCustomer(CustomerModel customer){
		this.customerId = customer.key;
		dialog.dismiss();
		binding.customerName.setText(customer.customerName);
	}

	@Override
	public void onClick(View view){
		super.onClick(view);
		switch(view.getId()){
		case R.id.rlt_set_customer:
			showSetCustomerDialog();
			break;
		case R.id.btn_new_customer:
			createNewCustomer();
			break;
		default:
			break;
		}
	}

	private void showSetCustomerDialog(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(ACConst.AC_BUSINESS_CUSTOMER_LIST, jsonObject, true);
	}

	private void createNewCustomer(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("customerName", viewBinding.edtNewCustomer.getText());
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(ACConst.AC_BUSINESS_CUSTOMER_CREATE, jsonObject, true);
	}

}
