package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardListFragment;
import trente.asia.addresscard.databinding.FragmentBusinessCardMainBinding;

/**
 * Created by tien on 4/18/2017.
 */

public class BusinessCardListFragment extends AddressCardListFragment {
    private FragmentBusinessCardMainBinding binding;
//    private CardAdapter adapter;
//    private Uri photoUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showBtnDelete() {
        binding.btnDelete.setVisibility(View.VISIBLE);
        binding.btnCapture.setVisibility(View.GONE);
    }

    public void showBtnCapture() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnCapture.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            initViewBinding(inflater, container);
            binding.listCards.setLayoutManager(new GridLayoutManager(getContext(), 3));
            binding.btnDelete.setOnClickListener(this);
            binding.btnCapture.setOnClickListener(this);
            binding.rowCategory.setOnClickListener(this);
            binding.rowCustomer.setOnClickListener(this);
//            List<CardModel> cards = new ArrayList<>();
//            adapter = new CardAdapter(cards, this);
//            binding.listCards.setAdapter(adapter);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    protected void initViewBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_card_main, container, false);
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
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        requestLoad(ACConst.AC_BUSINESS_CARD_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url){
        super.successLoad(response,url);
        binding.listCards.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.row_category:
                gotoFragment(new CategoryListFragment());
                break;
            case R.id.row_customer:
                gotoFragment(new CustomerListFragment(), "customer_list");
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    protected String getApiString() {
        return ACConst.AC_BUSINESS_CARD_NEW;
    }
}