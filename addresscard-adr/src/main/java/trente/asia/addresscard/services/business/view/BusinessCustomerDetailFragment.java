package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import org.json.JSONException;
import org.json.JSONObject;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.commons.utils.Utils;
import trente.asia.addresscard.databinding.FragmentCustomerDetailBinding;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerDetailCardAdapter;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/12/2017.
 */

public class BusinessCustomerDetailFragment extends AbstractAddressCardFragment {
    private     FragmentCustomerDetailBinding               binding;
    private     CustomerModel                               customer;
    private     int                                         customerId;

    public static BusinessCustomerDetailFragment newInstance(int customerId) {
        BusinessCustomerDetailFragment fragment = new BusinessCustomerDetailFragment();
        fragment.customerId = customerId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_detail, container, false);
            mRootView = binding.getRoot();
            binding.listCards.setLayoutManager(new LinearLayoutManager(getContext()));

            binding.listCards.setNestedScrollingEnabled(false);
            binding.rowLastComments.setOnClickListener(this);
            binding.rowAddress.setOnClickListener(this);
            binding.rowBrowser.setOnClickListener(this);
            binding.rowTel.setOnClickListener(this);

            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(ACConst.AC_BUSINESS_CUSTOMER_DETAIL, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);
        customer = CCJsonUtil.convertToModel(response.optString("customer"), CustomerModel.class);
        binding.listCards.setAdapter(new CustomerDetailCardAdapter(customer.cards));
        binding.setVariable(BR.customer, customer);
        binding.executePendingBindings();
        WfPicassoHelper.loadImageWithDefaultIcon(getContext(), BuildConfig.HOST,
                binding.customerLogo, customer.attachment.fileUrl, R.drawable.default_logo);
        super.initHeader(R.drawable.ac_back_white, customer.customerName, R.drawable.ac_action_edit);
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
            case R.id.row_tel:
                startPhoneCall();
                break;
            case R.id.row_address:
                openMap();
                break;
            case R.id.row_browser:
                openBrowser();
                break;
            default:
                break;
        }
    }

    private void onBtnEditClick() {
        gotoFragment(BusinessCustomerEditFragment.newInstance(customer));
    }

    private void showCommentFragment() {
        gotoFragment(BusinessCustomerCommentFragment.newInstance(customer));
    }

    private void startPhoneCall() {
        Utils.startPhoneCall(getContext(), customer.customerTel);
    }

    private void openMap() {

    }

    private void openBrowser() {

    }
}
