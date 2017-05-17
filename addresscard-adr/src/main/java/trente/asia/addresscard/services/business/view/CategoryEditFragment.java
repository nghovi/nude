package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import org.json.JSONObject;

import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCategoryEditBinding;
import trente.asia.addresscard.services.business.model.CategoryModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerCategoryEditAdapter;

/**
 * Created by tien on 5/11/2017.
 */

public class CategoryEditFragment extends AbstractAddressCardFragment {
    private FragmentCategoryEditBinding binding;
    private CategoryModel category;

    public static CategoryEditFragment newInstance(CategoryModel category) {
        CategoryEditFragment fragment = new CategoryEditFragment();
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
            CustomerCategoryEditAdapter adapter =
                    new CustomerCategoryEditAdapter(category.customers, allCustomers);
            binding.listCustomers.setAdapter(adapter);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, category.categoryName, R.drawable.ac_action_done);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
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
        onClickBackBtn();
    }
}
