package trente.asia.addresscard.commons.fragments;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.addresscard.R;

/**
 * Created by tien on 5/23/2017.
 */

public abstract class AddressCardEditFragment extends AbstractAddressCardFragment{

	protected int key;

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
	public int getFooterItemId(){
		return R.id.lnr_view_footer_card;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.img_id_header_right_icon:
			finishEditCard();
			break;
		default:
			break;
		}
	}

	private void finishEditCard(){
		updateAddressCard();
	}

	@Override
	protected void onSuccessUpLoad(JSONObject response, boolean isAlert, String url) {
		super.onSuccessUpLoad(response, isAlert, url);
		onClickBackBtn();
	}

	protected abstract void updateAddressCard();

	protected abstract String getApiLoadData();

}
