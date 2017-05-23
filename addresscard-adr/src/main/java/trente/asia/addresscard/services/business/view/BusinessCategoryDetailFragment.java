package trente.asia.addresscard.services.business.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCategoryDetailBinding;
import trente.asia.addresscard.services.business.model.CategoryModel;
import trente.asia.addresscard.services.business.presenter.CategoryCustomerAdapter;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCategoryDetailFragment extends AbstractAddressCardFragment{

	private FragmentCategoryDetailBinding	binding;
	private int								categoryId;
	private CategoryCustomerAdapter			adapter;
	private CategoryModel					category;

	public static BusinessCategoryDetailFragment newInstance(int categoryId){
		BusinessCategoryDetailFragment fragment = new BusinessCategoryDetailFragment();
		fragment.categoryId = categoryId;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_detail, container, false);
			// binding.
			mRootView = binding.getRoot();
			adapter = new CategoryCustomerAdapter();
			binding.listCustomers.setAdapter(adapter);
//            binding.
			binding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
			mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		super.initData();
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", categoryId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(ACConst.AC_BUSINESS_CATEGORY_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		super.successLoad(response, url);
		if(ACConst.AC_BUSINESS_CATEGORY_DETAIL.equals(url)){
			category = CCJsonUtil.convertToModel(response.optString("category"), CategoryModel.class);
			binding.setVariable(BR.category, category);
			binding.executePendingBindings();
			adapter.setCustomers(category.customers);
		}
	}

	@Override
	protected void initView(){
		super.initView();
		super.initHeader(R.drawable.ac_back_white, "Airline", R.drawable.ac_action_edit);
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_card;
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.img_id_header_right_icon:
			gotoFragment(BusinessCategoryEditFragment.newInstance(category));
			break;
		default:
			break;
		}
	}
}
