package trente.asia.addresscard.services.shop.view;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentTagsBinding;
import trente.asia.addresscard.services.shop.model.TagModel;
import trente.asia.addresstag.services.shop.presenter.TagAdapter;

/**
 * Created by viet on 5/22/2017.
 */

public class TagsFragment extends AbstractAddressCardFragment{

	private FragmentTagsBinding	binding;
	private TagAdapter			adapter;
	private Uri					photoUri;

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
			mRootView = binding.getRoot();
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, getString(R.string.shop_cards_title), null);
	}

	@Override
	protected void initData(){
		super.initData();
		JSONObject jsonObject = new JSONObject();
		requestLoad(ACConst.API_SHOP_TAG_LIST, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		List<TagModel> tags = null;
		try {
			tags = LoganSquare.parseList(response.optString("tags"), TagModel
					.class);
			adapter = new TagAdapter(tags);
			binding.lstTag.setAdapter(adapter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onClickBackBtn(){

		super.onClickBackBtn();
	}


	@Override
	public void onClick(View v){

	}
}