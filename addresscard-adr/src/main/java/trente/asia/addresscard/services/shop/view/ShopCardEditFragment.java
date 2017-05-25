package trente.asia.addresscard.services.shop.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardEditFragment;
import trente.asia.addresscard.databinding.FragmentShopCardEditBinding;
import trente.asia.addresscard.services.shop.model.ShopCardModel;
import trente.asia.addresscard.services.shop.model.TagModel;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/11/2017.
 */

public class ShopCardEditFragment extends AddressCardEditFragment{

	private ShopCardModel				card;
	private FragmentShopCardEditBinding	binding;

	public static ShopCardEditFragment newInstance(int cardId){
		ShopCardEditFragment fragment = new ShopCardEditFragment();
		fragment.key = cardId;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_card_edit, container, false);
			binding.rltCardTag.setOnClickListener(this);
			mRootView = binding.getRoot();
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, "", R.drawable.ac_action_done);
	}

	@Override
	protected String getApiLoadData(){
		return ACConst.API_SHOP_CARD_DETAIL;
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(ACConst.API_SHOP_CARD_DETAIL.equals(url)){
			// try{
			card = CCJsonUtil.convertToModel(response.optString("card"), ShopCardModel.class);
			card.setTagSelected(true);
			binding.setTags(card.tags);
			binding.setVariable(BR.card, card);
			binding.executePendingBindings();
			Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(binding.cardImage);
			updateHeader(card.cardName);
			// }catch(IOException e){
			// e.printStackTrace();
			// }
		}
	}

	@Override
	protected void updateAddressCard(){
		JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
		try{
			jsonObject.put("key", card.key);
			String tagKeys = TagModel.getSelectedTagKeys(binding.getTags());
			jsonObject.put("strTagIds", tagKeys);
		}catch(JSONException e){
			e.printStackTrace();
		}
		Map<String, File> fileMap = new HashMap<>();
		requestUpload(ACConst.API_SHOP_CARD_UPDATE, jsonObject, fileMap, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		super.successUpdate(response, url);
	}

	@Override
	public void onClick(View view){
		super.onClick(view);
		switch(view.getId()){
		case R.id.rlt_card_tag:
			gotoTagsFragment();
			break;
		default:
			break;
		}
	}

	private void gotoTagsFragment(){
		TagsFragment tagsFragment = new TagsFragment();
		tagsFragment.setEditBinding(binding);
		gotoFragment(tagsFragment);
	}

}
