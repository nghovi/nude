package trente.asia.shiftworking.services.offer.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;

/**
 * Created by tien on 9/14/2017.
 */

public class OvertimeListFragment extends AbstractSwFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_overtime_list, container, false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setmIsNewFragment(true);
    }
}
