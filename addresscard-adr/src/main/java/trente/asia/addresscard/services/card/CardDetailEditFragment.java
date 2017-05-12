package trente.asia.addresscard.services.card;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.DialogFragmentChooseCustomerBinding;
import trente.asia.addresscard.databinding.FragmentCardDetailEditBinding;
import trente.asia.addresscard.services.card.model.CustomerModel;

/**
 * Created by tien on 5/11/2017.
 */

public class CardDetailEditFragment extends AbstractAddressCardFragment {
    FragmentCardDetailEditBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_detail_edit, container, false);
            mRootView = binding.getRoot();
            binding.rltSetCustomer.setOnClickListener(this);
        }
        return mRootView;
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
            default:
                break;
        }
    }

    private void showSetCustomerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogFragmentChooseCustomerBinding viewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_fragment_choose_customer, null, false);
        builder.setView(viewBinding.getRoot());
        List<CustomerModel> customers = new ArrayList<>();
        customers.add(new CustomerModel("Trente", "https://cdn2.iconfinder.com/data/icons/business-flatcircle/512/travel-512.png"));
        customers.add(new CustomerModel("Thai airline", "http://www.hotelvergelijk.com/images/flight-icon.png"));
        customers.add(new CustomerModel("Vietnam airline",
                "http://www.ic.edu/Customized/Uploads/ByDate/2016/August_2016/August_21st_2016/stock-vector-school-bus-icon-circle-icon-43110542851108.png"));
        CustomerDialogAdapter adapter = new CustomerDialogAdapter(customers);
        viewBinding.listCustomers.setAdapter(adapter);
        viewBinding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
        builder.create().show();
    }
}
