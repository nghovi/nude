package trente.asia.thankscard.services.posted;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.FragmentSelectDeptBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;

/**
 * Created by tien on 7/12/2017.
 */

public class SelectDeptFragment extends AbstractTCFragment{
    private FragmentSelectDeptBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_dept, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    public int getFragmentLayoutId() {
        return 0;
    }

    @Override
    public boolean hasBackBtn() {
        return false;
    }

    @Override
    public boolean hasSettingBtn() {
        return false;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    public void buildBodyLayout() {

    }
}
