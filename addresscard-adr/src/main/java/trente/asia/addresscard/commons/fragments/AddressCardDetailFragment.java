package trente.asia.addresscard.commons.fragments;

import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.utils.Utils;
import trente.asia.addresscard.services.business.model.AddressCardModel;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

/**
 * Created by tien on 5/23/2017.
 */

public abstract class AddressCardDetailFragment extends AbstractAddressCardFragment{

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
		String api = getApi();
		requestLoad(api, jsonObject, true);
	}

	protected abstract String getApi();

	@Override
	protected void successLoad(JSONObject response, String url){
		super.successLoad(response, url);
		loadLayout(response);
	}

	protected abstract void loadLayout(JSONObject response);

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_card;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.img_id_header_right_icon:
			gotoCardEditFragment();
			break;
		case R.id.rlt_phone:
			Utils.startPhoneCall(getContext(), card.cardTel);
			break;
		case R.id.rlt_email:
			Utils.sendEmail(getContext(), card.cardEmail);
			break;
		case R.id.rlt_address:
			Utils.openMap(getContext(), card.cardAddress);
			break;
		default:
			break;
		}
	}

	protected abstract void gotoCardEditFragment();

	private void log(String msg){
		Log.e("Card Detail Fragment", msg);
	}
}
