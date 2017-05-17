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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCustomerEditBinding;
import trente.asia.addresscard.services.business.model.CardModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerEditCardAdapter;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.util.CAObjectSerializeUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerEditFragment extends AbstractAddressCardFragment
    implements CustomerEditCardAdapter.OnCardAdapterListener {

    private         FragmentCustomerEditBinding         binding;
    private         CustomerModel                       customer;
    private         CustomerEditCardAdapter             adapter;

    public static CustomerEditFragment newInstance(CustomerModel customer) {
        CustomerEditFragment fragment = new CustomerEditFragment();
        fragment.customer = customer;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_customer_edit, container, false);
            binding.setVariable(BR.customer, customer);
            binding.executePendingBindings();
            WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + customer.attachment.fileUrl,
                    binding.customerLogo, null);
            mRootView = binding.getRoot();
            binding.listCards.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CustomerEditCardAdapter(customer.cards, this);
            binding.listCards.setAdapter(adapter);
            binding.listCards.setNestedScrollingEnabled(false);
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
            binding.btnDelete.setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, customer.customerName, R.drawable.ac_action_done);
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                finishEditCustomer();
                break;
            case R.id.btn_delete:
                deleteCustomer();
                break;
            default:
                break;
        }
    }

    private void finishEditCustomer() {
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
        try {
            jsonObject.put("key", customer.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, File> fileMap = new HashMap<>();
        requestUpload(ACConst.AC_BUSINESS_CUSTOMER_UPDATE, jsonObject, fileMap, true);
    }

    @Override
    protected void successUpload(JSONObject response, String url) {
        super.successUpload(response, url);
        onClickBackBtn();
    }

    private void deleteCustomer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", customer.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_CUSTOMER_DELETE, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        super.successUpdate(response, url);
        if (ACConst.AC_BUSINESS_CUSTOMER_DELETE.equals(url)) {
            ((ChiaseActivity) activity).isInitData = true;
            getFragmentManager().popBackStack("customer_list", 0);
        }
    }

    @Override
    public void ungroupCard(CardModel card) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", card.key);
            jsonObject.put("cardName", card.cardName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_CARD_UNGROUP, jsonObject, true);
    }

    @Override
    public void deleteCard(CardModel card) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cardIds", card.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_CARD_DELETE, jsonObject, true);
    }
}
