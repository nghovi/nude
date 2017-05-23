package trente.asia.addresscard.services.business.view;

import android.support.v7.app.AlertDialog;
import android.view.View;

import org.json.JSONObject;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardEditFragment;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCardEditFragment extends AddressCardEditFragment {
    private     int                                     customerId;

    private void showSetCustomerDialog() {
        JSONObject jsonObject = new JSONObject();
        requestLoad(ACConst.AC_BUSINESS_CUSTOMER_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);
        if (ACConst.AC_BUSINESS_CUSTOMER_LIST.equals(url)) {
            List<CustomerModel> customers = CCJsonUtil.convertToModelList(response.optString("customers"), CustomerModel.class);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            viewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                    R.layout.dialog_fragment_choose_customer, null, false);
            builder.setView(viewBinding.getRoot());
            CustomerDialogAdapter adapter = new CustomerDialogAdapter(customers, this);
            viewBinding.listCustomers.setAdapter(adapter);
            viewBinding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
            viewBinding.btnNewCustomer.setOnClickListener(this);
            dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    protected void updateAddressCard() {
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
        try {
            jsonObject.put("key", card.key);
            jsonObject.put("customerId", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, File> fileMap = new HashMap<>();
        log(jsonObject.toString());
        requestUpload(ACConst.AC_BUSINESS_CARD_UPDATE, jsonObject, fileMap, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        super.successUpdate(response, url);
        log(response.toString());

        try {
            customerId = response.getInt("customerId");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.customerName.setText(viewBinding.edtNewCustomer.getText());
        dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rlt_set_customer:
                showSetCustomerDialog();
                break;
            case R.id.btn_new_customer:
                createNewCustomer();
                break;
            default:
                break;
        }
    }

    private void createNewCustomer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerName", viewBinding.edtNewCustomer.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestUpdate(ACConst.AC_BUSINESS_CUSTOMER_CREATE, jsonObject, true);
    }

}
