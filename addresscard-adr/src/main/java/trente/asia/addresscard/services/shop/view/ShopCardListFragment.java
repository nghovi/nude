package trente.asia.addresscard.services.shop.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardListFragment;
import trente.asia.addresscard.databinding.FragmentShopCardsBinding;
import trente.asia.addresscard.services.business.model.AddressCardModel;
import trente.asia.addresscard.services.business.presenter.CardAdapter;

/**
 * Created by tien on 4/18/2017.
 */

public class ShopCardListFragment extends AddressCardListFragment {

	private FragmentShopCardsBinding	binding;
//	private CardAdapter					adapterr;
//	private Uri							photoUri;

//	@photoUriOverride
//	public void onCreate(@Nullable Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//	}

	@Override
	protected void initViewBinding(LayoutInflater inflater, ViewGroup container) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_cards, container, false);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_shop;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_cards, container, false);
			binding.listCards.setLayoutManager(new GridLayoutManager(getContext(), 3));
			binding.btnDelete.setOnClickListener(this);
			binding.btnCapture.setOnClickListener(this);
			binding.rltTags.setOnClickListener(this);
			binding.setShopTags(getString(R.string.chiase_common_all));
			// binding.rowCustomer.setOnClickListener(this);
			adapter = new CardAdapter(this);
			binding.listCards.setAdapter(adapter);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, getString(R.string.shop_cards_title), null);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		super.successLoad(response,url);
			binding.listCards.setAdapter(adapter);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.rlt_tags:
			gotoTagsFragment();
			break;
		default:
			super.onClick(view);
			break;
		}
	}

	public void showBtnDelete() {
		binding.btnDelete.setVisibility(View.VISIBLE);
		binding.btnCapture.setVisibility(View.GONE);
	}

	public void showBtnCapture() {
		binding.btnDelete.setVisibility(View.GONE);
		binding.btnCapture.setVisibility(View.VISIBLE);
	}

	@Override
	protected String getApiLoadString() {
		return ACConst.API_SHOP_CARD_LIST;
	}

	@Override
	protected String getApiDeleteString() {
		return null;
	}

	// // TODO: 5/22/2017 shouldn't create each Tags Fragment?
	private void gotoTagsFragment(){
		TagsFragment tagsFragment = new TagsFragment();
		tagsFragment.setShopCardBinding((FragmentShopCardsBinding)binding);
		gotoFragment(tagsFragment);
	}


	@Override
	public void onItemClick(AddressCardModel card) {

	}
}