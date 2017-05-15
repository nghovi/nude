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
import trente.asia.addresscard.databinding.FragmentCustomerDetailBinding;
import trente.asia.addresscard.services.business.presenter.CustomerDetailCardAdapter;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerDetailFragment extends AbstractAddressCardFragment {
    FragmentCustomerDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_detail, container, false);
            mRootView = binding.getRoot();
            binding.listCards.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listCards.setAdapter(new CustomerDetailCardAdapter());
            binding.listCards.setNestedScrollingEnabled(false);
            binding.rowLastComments.setOnClickListener(this);
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, "Trente Vietnam", R.drawable.ac_action_edit);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                onBtnEditClick();
                break;
            case R.id.row_last_comments:
                showCommentFragment();
                break;
            default:
                break;
        }
    }

    private void onBtnEditClick() {
        gotoFragment(new CustomerEditFragment());
    }

    private void showCommentFragment() {
        gotoFragment(new CustomerCommentFragment());
    }
}
