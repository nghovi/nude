package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCustomerListBinding;
import trente.asia.addresscard.services.business.presenter.CustomerAdapter;

/**
 * Created by Windows 10 Gamer on 13/05/2017.
 */

public class CustomerListFragment extends AbstractAddressCardFragment
    implements CustomerAdapter.OnCustomerAdapterListener {
    FragmentCustomerListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_list,
                    container, false);
            mRootView = binding.getRoot();
            binding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listCustomers.setAdapter(new CustomerAdapter(this));
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, "Customer list", null);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick() {
        gotoFragment(new CustomerDetailFragment());
    }
}
