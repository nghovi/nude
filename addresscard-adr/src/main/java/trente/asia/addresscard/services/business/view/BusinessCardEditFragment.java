package trente.asia.addresscard.services.business.view;

import trente.asia.addresscard.commons.fragments.AddressCardEditFragment;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCardEditFragment extends AddressCardEditFragment {
    public static BusinessCardEditFragment newInstance(BusinessCardModel card) {
        BusinessCardEditFragment fragment = new BusinessCardEditFragment();
        fragment.card = card;
        return fragment;
    }


}
