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
import trente.asia.addresscard.databinding.FragmentCategoryListBinding;
import trente.asia.addresscard.services.business.presenter.CategoryAdapter;

/**
 * Created by tien on 5/9/2017.
 */

public class CategoryListFragment extends AbstractAddressCardFragment
    implements CategoryAdapter.OnCategoryAdapterListener {
    FragmentCategoryListBinding binding;
    CategoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false);
            adapter = new CategoryAdapter(this);
            binding.listCategories.setAdapter(adapter);
            binding.listCategories.setLayoutManager(new LinearLayoutManager(getContext()));
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, "Category list", null);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick() {
        gotoFragment(new CategoryDetailFragment());
    }

}
