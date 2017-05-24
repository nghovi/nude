package trente.asia.addresscard.services.shop.view;

import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardDetailFragment;
import trente.asia.addresscard.databinding.FragmentCardDetailShowBinding;
import trente.asia.addresscard.databinding.FragmentShopCardDetailBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;
import trente.asia.addresscard.services.business.view.BusinessCardDetailFragment;
import trente.asia.addresscard.services.business.view.BusinessCardEditFragment;
import trente.asia.addresscard.services.shop.model.ShopCardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class ShopCardDetailFragment extends AddressCardDetailFragment{

	private ShopCardModel card;

	public static ShopCardDetailFragment newInstance(int cardKey){
		ShopCardDetailFragment fragment = new ShopCardDetailFragment();
		fragment.key = cardKey;
		return fragment;
	}

	@Override
	protected int getLayoutId(){
		return R.layout.fragment_shop_card_detail;
	}

	@Override
	protected String getApi(){
		return ACConst.API_SHOP_CARD_DETAIL;
	}

	@Override
	protected ImageView getImageView(){
		return ((FragmentShopCardDetailBinding)binding).cardImage;
	}

	@Override
	protected void loadLayout(JSONObject response){
		try{
			card = LoganSquare.parse(response.optString("card"), ShopCardModel.class);
			Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(getImageView());
			binding.setVariable(BR.card, card);
			binding.executePendingBindings();
			super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_edit);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void gotoCardEditFragment(){
		gotoFragment(ShopCardEditFragment.newInstance(card.key));
	}
}
