package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCustomerListBinding;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerAdapter;

/**
 * Created by Windows 10 Gamer on 13/05/2017.
 */

public class BusinessCustomerListFragment extends AbstractAddressCardFragment
    implements CustomerAdapter.OnCustomerAdapterListener {

    private     FragmentCustomerListBinding             binding;
    private     CustomerAdapter                         adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_list,
                    container, false);
            mRootView = binding.getRoot();
            adapter = new CustomerAdapter(this);
            binding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listCustomers.setAdapter(adapter);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, "Customer list", null);
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
        List<CustomerModel> customers = CCJsonUtil.convertToModelList(
                response.optString("customers"), CustomerModel.class);
        adapter.updateList(customers);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(int customerId) {
        gotoFragment(BusinessCustomerDetailFragment.newInstance(customerId));
    }
}
