package trente.asia.addresscard.services.shop.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardDetailFragment;
import trente.asia.addresscard.databinding.FragmentShopCardDetailBinding;
import trente.asia.addresscard.services.shop.model.ShopCardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class ShopCardDetailFragment extends AddressCardDetailFragment{

	private FragmentShopCardDetailBinding	binding;

	public static ShopCardDetailFragment newInstance(int cardKey){
		ShopCardDetailFragment fragment = new ShopCardDetailFragment();
		fragment.key = cardKey;
		return fragment;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_shop;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_card_detail, container, false);
			binding.rltPhone.setOnClickListener(this);
			binding.rltEmail.setOnClickListener(this);
			binding.rltAddress.setOnClickListener(this);
			mRootView = binding.getRoot();
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		}
		return mRootView;
	}

	@Override
	protected String getApi(){
		return ACConst.API_SHOP_CARD_DETAIL;
	}

	@Override
	protected void loadLayout(JSONObject response){
		// try{
		card = CCJsonUtil.convertToModel(response.optString("card"), ShopCardModel.class);
		((ShopCardModel)card).setTagSelected(true);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl)
				.resize(metrics.widthPixels, 0).into(binding.cardImage);
		binding.setVariable(BR.card, card);
		binding.executePendingBindings();
		super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_edit);
		// }catch(IOException e){
		// e.printStackTrace();
		// }
	}

	@Override
	protected void gotoCardEditFragment(){
		gotoFragment(ShopCardEditFragment.newInstance(card.key));
	}
}
