package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCardDetailShowBinding;

/**
 * Created by tien on 5/11/2017.
 */

public class CardDetailShowFragment extends AbstractAddressCardFragment {
    FragmentCardDetailShowBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_detail_show, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {

    }
}
