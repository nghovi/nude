package trente.asia.addresscard.commons.fragments;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.addresscard.R;
import trente.asia.addresscard.services.business.model.AddressCardModel;

/**
 * Created by tien on 5/23/2017.
 */

public abstract class AddressCardEditFragment extends AbstractAddressCardFragment{

	protected int				key;
	protected AddressCardModel	card;

	@Override
	protected void initData(){
		super.initData();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(getApiLoadData(), jsonObject, true);
	}

	@Override

	public void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, "", R.drawable.ac_action_done);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.img_id_header_right_icon:
			finishEditCard();
			break;
		case R.id.btn_delete:
			deleteCard();
		default:
			break;
		}
	}

	private void deleteCard(){
		//// TODO: 5/30/17
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("cardIds", card.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(getApiDeleteString(), jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		gotoCardListFragment();
	}

	protected abstract void gotoCardListFragment();

	protected abstract String getApiDeleteString();

	private void finishEditCard(){
		updateAddressCard();
	}

	@Override
	protected void onSuccessUpLoad(JSONObject response, boolean isAlert, String url){
		super.onSuccessUpLoad(response, isAlert, url);
	}

	@Override
	protected void successUpload(JSONObject response, String url){
		super.successUpload(response, url);
		onClickBackBtn();
	}

	protected abstract void updateAddressCard();

	protected abstract String getApiLoadData();

}
