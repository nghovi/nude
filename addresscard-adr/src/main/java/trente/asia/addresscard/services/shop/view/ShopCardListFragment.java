package trente.asia.addresscard.services.shop.view;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardListFragment;
import trente.asia.addresscard.databinding.FragmentShopCardsBinding;
import trente.asia.addresscard.services.business.model.AddressCardModel;
import trente.asia.addresscard.services.business.presenter.CardAdapter;
import trente.asia.addresscard.services.shop.model.ShopCardModel;
import trente.asia.addresscard.services.shop.model.TagModel;
import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by tien on 4/18/2017.
 */

public class ShopCardListFragment extends AddressCardListFragment{

	private FragmentShopCardsBinding	binding;
	private List<ShopCardModel>			cards			= new ArrayList<>();
	private List<AddressCardModel>		filteredCards	= new ArrayList<>();
	private List<String>				savedTagIds		= new ArrayList<>();
	// private CardAdapter adapterr;
	// private Uri photoUri;

	// @photoUriOverride
	// public void onCreate(@Nullable Bundle savedInstanceState){
	// super.onCreate(savedInstanceState);
	// }

	@Override
	protected void initViewBinding(LayoutInflater inflater, ViewGroup container){
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
			adapter = new CardAdapter(this);
			binding.listCards.setAdapter(adapter);
			// binding.setShopTags(getString(R.string.chiase_common_all));
			binding.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

				@Override
				public void onPropertyChanged(Observable observable, int i){
					retrieveSavedTags();
					filterCards();
					// adapter.setCards(filteredCards);
					adapter.notifyDataSetChanged();
				}
			});

			retrieveSavedTags();

			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	private void retrieveSavedTags(){
		// Get saved tag
		PreferencesSystemUtil prefSysUtil = new PreferencesSystemUtil(activity);
		String savedTags = prefSysUtil.get(TagsFragment.PREF_SAVED_TAG_IDS);
		if(!CCStringUtil.isEmpty(savedTags)){
			savedTagIds = new ArrayList<String>(Arrays.asList(savedTags.split(",")));
		}else{
			savedTagIds = new ArrayList<>();
		}
	}

	@Override
	public void onItemClick(AddressCardModel card){
		gotoFragment(ShopCardDetailFragment.newInstance(card.key));
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(null, getString(R.string.shop_cards_title), null);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		cards = CCJsonUtil.convertToModelList(response.optString("cards"), ShopCardModel.class);

		List<ApiObjectModel> mapTags = CCJsonUtil.convertToModelList(response.optString("mapTags"),
				ApiObjectModel.class);
		List<TagModel> tags = getTags(mapTags);
		binding.setTags(tags);
		filterCards();
		adapter.setCards(filteredCards);
	}

	private void filterCards(){
		filteredCards.clear();
		for(ShopCardModel shopCardModel : cards){
			if(CCCollectionUtil.isEmpty(savedTagIds) && CCCollectionUtil.isEmpty(shopCardModel.tags)){
				filteredCards.add(shopCardModel);
			}else{
				for(TagModel tagModel : shopCardModel.tags){
					if(savedTagIds.contains(tagModel.key)){
						filteredCards.add(shopCardModel);
						break;
					}
				}
			}
		}
	}

	private List<TagModel> getTags(List<ApiObjectModel> mapTags){
		List<TagModel> tagModels = new ArrayList<>();
		for(ApiObjectModel apiObjectModel : mapTags){
			TagModel tagModel = new TagModel();
			tagModel.key = apiObjectModel.key;
			tagModel.tagName = apiObjectModel.value;
			if(savedTagIds.contains(tagModel.key)){
				tagModel.selected = true;
			}
			tagModels.add(tagModel);
		}
		return tagModels;
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

	@Override
	protected String getUploadApi(){
		return ACConst.API_SHOP_CARD_UPDATE;
	}

	public void showBtnDelete(){
		binding.btnDelete.setVisibility(View.VISIBLE);
		binding.btnCapture.setVisibility(View.GONE);
	}

	public void showBtnCapture(){
		binding.btnDelete.setVisibility(View.GONE);
		binding.btnCapture.setVisibility(View.VISIBLE);
	}

	@Override
	protected String getApiLoadString(){
		return ACConst.API_SHOP_CARD_LIST;
	}

	@Override
	protected String getApiDeleteString(){
		return ACConst.API_SHOP_CARD_DELETE;
	}

	// // TODO: 5/22/2017 shouldn't create each Tags Fragment?
	private void gotoTagsFragment(){
		adapter.unselectAllCards();
		showBtnCapture();
		TagsFragment tagsFragment = new TagsFragment();
		tagsFragment.setShopCardBinding((FragmentShopCardsBinding)binding);
		gotoFragment(tagsFragment);
	}
}