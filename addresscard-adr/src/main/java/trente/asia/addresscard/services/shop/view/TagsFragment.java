package trente.asia.addresscard.services.shop.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentShopCardsBinding;
import trente.asia.addresscard.databinding.FragmentTagsBinding;
import trente.asia.addresscard.services.shop.model.TagModel;
import trente.asia.addresstag.services.shop.presenter.TagAdapter;

/**
 * Created by viet on 5/22/2017.
 */

public class TagsFragment extends AbstractAddressCardFragment implements TagAdapter.OnItemClickListener{

	private FragmentTagsBinding			binding;
	private TagAdapter					adapter;
	private Uri							photoUri;
	private FragmentShopCardsBinding	shopCardBinding;

	public void setTags(List<TagModel> tags){
		this.tags = tags;
	}

	private List<TagModel> tags = new ArrayList<>();

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
			binding.lstTag.setLayoutManager(new LinearLayoutManager(getContext()));
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, getString(R.string.tag_title), null);
	}

	@Override
	protected void initData(){
		super.initData();
		if(CCCollectionUtil.isEmpty(tags)){
			JSONObject jsonObject = new JSONObject();
			requestLoad(ACConst.API_SHOP_TAG_LIST, jsonObject, true);
		}else{
			buildLayout();
		}
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		try {
			tags = LoganSquare.parseList(response.optString("tags"), TagModel.class);
			buildLayout();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildLayout(){
		adapter = new TagAdapter(tags, this);
		binding.lstTag.setAdapter(adapter);
	}

	private String getTagsString(List<TagModel> tagModels){
		List<String> tagNames = new ArrayList<>();
		for(TagModel tagModel : tagModels){
			if(tagModel.selected){
				tagNames.add(tagModel.tagName);
			}
		}
		if(CCCollectionUtil.isEmpty(tagNames)){
			return getString(R.string.chiase_common_none);
		}
		return StringUtils.join(tagNames, ", ");
	}

	@Override
	protected void onClickBackBtn(){
		String tagsString = getTagsString(tags);
		this.shopCardBinding.setShopTags(tagsString);
		this.shopCardBinding.setTags(tags);
		shopCardBinding.executePendingBindings();
		super.onClickBackBtn();
	}

	@Override
	public void onClick(View v){

	}

	public void setShopCardBinding(FragmentShopCardsBinding shopCardBinding){
		this.shopCardBinding = shopCardBinding;
	}

	@Override
	public void onItemClick(TagModel tag){

	}
}