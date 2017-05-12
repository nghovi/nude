package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCustomerEditBinding;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerEditFragment extends AbstractAddressCardFragment {
    FragmentCustomerEditBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_edit, container, false);
            mRootView = binding.getRoot();
            binding.listCards.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listCards.setAdapter(new CustomerEditCardAdapter());
            binding.listCards.setNestedScrollingEnabled(false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void onClick(View view) {

    }
}
