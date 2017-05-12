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
import trente.asia.addresscard.databinding.FragmentCustomerCommentBinding;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerCommentFragment extends AbstractAddressCardFragment {
    FragmentCustomerCommentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater,
                    R.layout.fragment_customer_comment, container, false);
            mRootView = binding.getRoot();
            binding.listComments.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listComments.setAdapter(new CustomerCommentAdapter());
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {

    }
}
