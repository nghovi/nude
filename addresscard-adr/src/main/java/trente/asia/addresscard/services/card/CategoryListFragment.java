package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCategoryListBinding;

/**
 * Created by tien on 5/9/2017.
 */

public class CategoryListFragment extends AbstractAddressCardFragment {
    FragmentCategoryListBinding binding;
    ArrayList<Category> categories;
    ArrayList<Company> customers;
    CategoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setListCategory();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false);
        adapter = new CategoryAdapter(categories);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_company;
    }

    @Override
    public void onClick(View view) {

    }

    private void setListCategory() {
        categories = new ArrayList<>();
        customers = new ArrayList<>();

        ArrayList<Company> listCompany = new ArrayList<>();
        categories.add(new Category("Airline", listCompany));
        listCompany.add(new Company("Thai Airline", R.drawable.test_thai_airline));
        listCompany.add(new Company("Vietnam Airline", R.drawable.test_vn_airline));
        listCompany.add(new Company("Vietjet Airline", R.drawable.test_vietjet_airline));
        listCompany.add(new Company("Cathay Airline", R.drawable.test_cathay_airline));
        listCompany.add(new Company("Japan Airline", R.drawable.test_japan_airline));
        listCompany.add(new Company("Jetstar Airline", R.drawable.test_jetstar_airline));
        listCompany.add(new Company("S7 Airline", R.drawable.test_s7_airline));
        for (Company company : listCompany) {
            customers.add(company);
        }

        listCompany = new ArrayList<>();
        categories.add(new Category("Service Company", listCompany));
        listCompany.add(new Company("Facebook", R.drawable.test_facebook));
        listCompany.add(new Company("Skype", R.drawable.test_skype));
        listCompany.add(new Company("Twitter", R.drawable.test_twitter));
        for (Company company : listCompany) {
            customers.add(company);
        }

        listCompany = new ArrayList<>();
        categories.add(new Category("Offshore Company", listCompany));
        listCompany.add(new Company("Microsoft", R.drawable.test_microsoft));
        listCompany.add(new Company("Apple", R.drawable.test_facebook));
        listCompany.add(new Company("Google", R.drawable.test_google));
        listCompany.add(new Company("Amazon", R.drawable.test_amazon));
        for (Company company : listCompany) {
            customers.add(company);
        }

        listCompany = new ArrayList<>();
        categories.add(new Category("Global Company", listCompany));
        listCompany.add(new Company("Drive", R.drawable.test_drive));
        for (Company company : listCompany) {
            customers.add(company);
        }
    }
}
