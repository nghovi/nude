package trente.asia.addresscard.services.shop.view;

import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentShopDetailBinding;
import trente.asia.addresscard.services.business.model.CardModel;
import trente.asia.addresscard.services.business.view.CardEditFragment;

/**
 * Created by tien on 5/11/2017.
 */

public class ShopDetailFragment extends AbstractAddressCardFragment{

	private FragmentShopDetailBinding	binding;
	private int							key;
	private CardModel					card;

	public static ShopDetailFragment newInstance(int cardKey){
		ShopDetailFragment fragment = new ShopDetailFragment();
		fragment.key = cardKey;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_detail, container, false);
			mRootView = binding.getRoot();
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);

		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, "", null);
	}

	@Override
	protected void initData(){
		super.initData();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(ACConst.AC_BUSINESS_CARD_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		super.successLoad(response, url);

		card = CCJsonUtil.convertToModel(response.optString("card"), CardModel.class);
		Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(binding.cardImage);
		binding.setVariable(BR.card, card);
		binding.executePendingBindings();
		super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_edit);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_card;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.img_id_header_right_icon:
			gotoFragment(CardEditFragment.newInstance(card));
			break;
		default:
			break;
		}
	}

	private void log(String msg){
		Log.e("Card Detail Fragment", msg);
	}
}
