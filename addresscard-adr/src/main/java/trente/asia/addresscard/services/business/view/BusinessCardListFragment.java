package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardListFragment;
import trente.asia.addresscard.databinding.FragmentBusinessCardsBinding;
import trente.asia.addresscard.services.business.model.AddressCardModel;
import trente.asia.addresscard.services.business.presenter.CardAdapter;

/**
 * Created by tien on 4/18/2017.
 */

public class BusinessCardListFragment extends AddressCardListFragment {
    private FragmentBusinessCardsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            initViewBinding(inflater, container);
            binding.listCards.setLayoutManager(new GridLayoutManager(getContext(), 3));
            binding.btnDelete.setOnClickListener(this);
            binding.btnCapture.setOnClickListener(this);
            binding.rowCategory.setOnClickListener(this);
            binding.rowCustomer.setOnClickListener(this);
            adapter = new CardAdapter(this);
            binding.listCards.setAdapter(adapter);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    public void onItemClick(AddressCardModel card) {
        gotoFragment(BusinessCardDetailFragment.newInstance(card.key));
    }

    @Override
    protected void initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_cards, container, false);
    }

    @Override
    public void showBtnDelete() {
        binding.btnDelete.setVisibility(View.VISIBLE);
        binding.btnCapture.setVisibility(View.GONE);
    }

    @Override
    public void showBtnCapture() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnCapture.setVisibility(View.VISIBLE);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }


    @Override
    public void initView() {
        super.initView();
        super.initHeader(null, getString(R.string.ac_main_card_title), null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.row_category:
                showBtnCapture();
                gotoFragment(new BusinessCategoryListFragment());
                break;
            case R.id.row_customer:
                showBtnCapture();
                gotoFragment(new BusinessCustomerListFragment(), "customer_list");
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected String getUploadApi() {
        return ACConst.AC_BUSINESS_CARD_NEW;
    }

    @Override
    protected String getApiLoadString() {
        return ACConst.AC_BUSINESS_CARD_LIST;
    }

    @Override
    protected String getApiDeleteString() {
        return ACConst.AC_BUSINESS_CARD_DELETE;
    }
}