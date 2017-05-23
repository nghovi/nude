package trente.asia.addresscard.services.business.view;

import android.widget.ImageView;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardDetailFragment;
import trente.asia.addresscard.databinding.FragmentCardDetailShowBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCardDetailFragment extends AddressCardDetailFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_card_detail_show;
    }

    @Override
    protected String getApi() {
        return ACConst.AC_BUSINESS_CARD_DETAIL;
    }

    @Override
    protected ImageView getImageView() {
        return ((FragmentCardDetailShowBinding) binding).cardImage;
    }

    @Override
    protected void gotoCardEditFragment() {
        gotoFragment(BusinessCardEditFragment.newInstance((BusinessCardModel) card));
    }
}
