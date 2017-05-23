package trente.asia.addresscard.commons.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.DialogFragmentChooseCustomerBinding;
import trente.asia.addresscard.databinding.FragmentCardDetailEditBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerDialogAdapter;
import trente.asia.addresscard.services.business.view.BusinessCardEditFragment;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/23/2017.
 */

public abstract class AddressCardEditFragment extends AbstractAddressCardFragment
        implements CustomerDialogAdapter.OnCustomerDialogListener {

    private     FragmentCardDetailEditBinding           binding;
    protected   int                                     key;
    private     Dialog                                  dialog;
    private     DialogFragmentChooseCustomerBinding     viewBinding;

    public static AddressCardEditFragment newInstance(int cardId) {
        AddressCardEditFragment fragment = new AddressCardEditFragment();
        fragment.key = cardId;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_detail_edit, container, false);
            binding.setVariable(BR.card, card);
            binding.executePendingBindings();
            Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(binding.cardImage);
            mRootView = binding.getRoot();
            customerId = card.customerId;
            binding.rltSetCustomer.setOnClickListener(this);
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();
        super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_done);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_set_customer:
                showSetCustomerDialog();
                break;
            case R.id.img_id_header_right_icon:
                finishEditCard();
                break;
            case R.id.btn_new_customer:
                createNewCustomer();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelectCustomer(CustomerModel customer) {
        this.customerId = customer.key;
        dialog.dismiss();
        binding.customerName.setText(customer.customerName);
    }

    private void finishEditCard() {
        updateAddressCard();
        onClickBackBtn();
    }

    protected abstract void updateAddressCard();

    @Override
    protected void successUpload(JSONObject response, String url) {
        super.successUpload(response, url);
        log(response.toString());
    }

    private void log(String msg) {
        Log.e("Card Edit Fragment", msg);
    }
}
