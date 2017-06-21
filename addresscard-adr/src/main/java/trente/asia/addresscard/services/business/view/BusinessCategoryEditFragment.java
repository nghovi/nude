package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.commons.defines.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCategoryEditBinding;
import trente.asia.addresscard.services.business.model.CategoryModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CategoryEditCustomerAdapter;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCategoryEditFragment extends AbstractAddressCardFragment {
    private FragmentCategoryEditBinding             binding;
    private CategoryModel                           category;
    private CategoryEditCustomerAdapter             adapter;

    public static BusinessCategoryEditFragment newInstance(CategoryModel category) {
        BusinessCategoryEditFragment fragment = new BusinessCategoryEditFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_edit, container, false);
            mRootView = binding.getRoot();
            binding.setVariable(BR.category, category);
            binding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listCustomers.setNestedScrollingEnabled(false);
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        requestLoad(ACConst.AC_BUSINESS_CUSTOMER_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);
        if (ACConst.AC_BUSINESS_CUSTOMER_LIST.equals(url)) {
            List<CustomerModel> allCustomers = CCJsonUtil.convertToModelList(
                    response.optString("customers"), CustomerModel.class);
            adapter = new CategoryEditCustomerAdapter(category.customers, allCustomers);
            binding.listCustomers.setAdapter(adapter);
            log("Customer number: " + allCustomers.size());
        }
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, category.categoryName, R.drawable.ac_action_done);
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                finishEditCategory();
                break;
            default:
                break;
        }
    }

    private void finishEditCategory() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerIds", adapter.getCustomerIds());
            jsonObject.put("key", category.key);
            jsonObject.put("categoryName", category.categoryName);
            jsonObject.put("categoryNote", category.categoryNote);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_CATEGORY_UPDATE, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        super.successUpdate(response, url);
        if (ACConst.AC_BUSINESS_CATEGORY_UPDATE.equals(url)) {
            onClickBackBtn();
        }
    }

    private void log(String msg) {
        Log.e("BusinessCategoryEdit", msg);
    }
}
