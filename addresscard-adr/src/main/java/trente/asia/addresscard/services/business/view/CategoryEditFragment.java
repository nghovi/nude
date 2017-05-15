package trente.asia.addresscard.services.business.view;

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
import trente.asia.addresscard.databinding.FragmentCategoryEditBinding;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerCategoryEditAdapter;

/**
 * Created by tien on 5/11/2017.
 */

public class CategoryEditFragment extends AbstractAddressCardFragment {
    FragmentCategoryEditBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_edit, container, false);
            mRootView = binding.getRoot();
            List<CustomerModel> customers = new ArrayList<>();
            customers.add(new CustomerModel("Trente", "https://cdn2.iconfinder.com/data/icons/business-flatcircle/512/travel-512.png"));
            customers.add(new CustomerModel("Thai airline", "http://www.hotelvergelijk.com/images/flight-icon.png"));
            customers.add(new CustomerModel("Vietnam airline",
                    "http://www.ic.edu/Customized/Uploads/ByDate/2016/August_2016/August_21" +
                            "st_2016/stock-vector-school-bus-icon-circle-icon-43110542851108.png"));
            CustomerCategoryEditAdapter adapter = new CustomerCategoryEditAdapter(customers);
            binding.listCustomers.setAdapter(adapter);
            binding.listCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, "Airline", R.drawable.ac_action_done);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                finishEditCategory();
                break;
            default:
                break;
        }
    }

    private void finishEditCategory() {
        onClickBackBtn();
    }
}
