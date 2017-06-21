package trente.asia.addresscard.services.shop.view;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.commons.defines.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentShopCardEditBinding;
import trente.asia.addresscard.databinding.FragmentShopCardsBinding;
import trente.asia.addresscard.databinding.FragmentTagsBinding;
import trente.asia.addresscard.services.shop.model.TagModel;
import trente.asia.addresstag.services.shop.presenter.TagAdapter;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by viet on 5/22/2017.
 */

public class TagsFragment extends AbstractAddressCardFragment implements TagAdapter.OnItemClickListener{

	public static final String			PREF_SAVED_TAG_IDS	= "PREF_SAVED_TAG_IDS";
	private FragmentTagsBinding			binding;
	private TagAdapter					adapter;
	private Uri							photoUri;
	private FragmentShopCardsBinding	shopCardBinding;
	private FragmentShopCardEditBinding	editBinding;
	private List<TagModel>				tagModels = new ArrayList<>();									// master all
	private List<TagModel>				tags				= new ArrayList<>();
	private Map<String, Boolean>		originTagStatus		= new HashMap<>();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tags, container, false);
			// List<TagModel> tagModels = new ArrayList<>();
			// adapter = new TagAdapter(tagModels);
			// binding.lstTag.setAdapter(adapter);
			binding.btnCheckAll.setOnClickListener(this);
			binding.btnUncheckAll.setOnClickListener(this);
			binding.lstTag.setLayoutManager(new LinearLayoutManager(getContext()));
			adapter = new TagAdapter(this);
			binding.lstTag.setAdapter(adapter);
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, getString(R.string.tag_title), R.drawable.ac_action_done);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
	}

	@Override
	protected void initData(){
		super.initData();
		JSONObject jsonObject = new JSONObject();
		requestLoad(ACConst.API_SHOP_TAG_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		tagModels = CCJsonUtil.convertToModelList(response.optString("tags"), TagModel.class);
		buildLayout();
	}

	private void buildLayout(){
		originTagStatus = new HashMap<>();
		for(TagModel tagModel : tagModels){
			for(TagModel tag : tags){
				if(tag.key.equals(tagModel.key)){
					tagModel.selected = tag.selected;
					break;
				}
			}
			originTagStatus.put(tagModel.key, tagModel.selected);
		}

		adapter.setTags(tagModels);
	}

	//// TODO: 5/25/17 when
	protected void onClickBackBtn(){
		// Restore origin tag selected status
		for(TagModel tagModel : tagModels){
			tagModel.selected = originTagStatus.get(tagModel.key);
		}

		backToPreviousFragment();
	}

	private void backToPreviousFragment(){
		if(getFragmentManager().getBackStackEntryCount() <= 1){
			((WelfareActivity)activity).setDoubleBackPressedToFinish();
		}else{
			// No init data (call api at shop card list screen
			getFragmentManager().popBackStack();
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			onChooseTagDone();
			break;
		case R.id.btn_check_all:
			checkAllTags();
			break;
		case R.id.btn_uncheck_all:
			uncheckAllTags();
			break;
		default:
			break;
		}
	}

	private void uncheckAllTags(){
		setCheckAllTags(false);
	}

	private void checkAllTags(){
		setCheckAllTags(true);
	}

	private void onChooseTagDone(){
		if(shopCardBinding != null){
			String savedTagIds = TagModel.getSelectedTagKeys(tagModels);
			PreferencesSystemUtil prefSysUtil = new PreferencesSystemUtil(activity);
			prefSysUtil.set(PREF_SAVED_TAG_IDS, savedTagIds);
			shopCardBinding.setTags(tagModels);
			shopCardBinding.executePendingBindings();
		}
		if(editBinding != null){
			editBinding.setTags(tagModels);
			editBinding.executePendingBindings();
		}
		backToPreviousFragment();
	}

	public void setShopCardBinding(FragmentShopCardsBinding shopCardBinding){
		this.shopCardBinding = shopCardBinding;
		this.tags = shopCardBinding.getTags();
	}

	@Override
	public void onItemClick(TagModel tag){

	}

	public void setEditBinding(FragmentShopCardEditBinding editBinding){
		this.editBinding = editBinding;
		this.tags = editBinding.getTags();
	}

	public void setCheckAllTags(boolean check){
		for(TagModel tag : tagModels){
			tag.selected = check;
		}
		adapter.notifyDataSetChanged();
	}
}