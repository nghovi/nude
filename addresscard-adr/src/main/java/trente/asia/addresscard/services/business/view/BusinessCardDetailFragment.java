package trente.asia.addresscard.services.business.view;

import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AddressCardDetailFragment;
import trente.asia.addresscard.databinding.FragmentCardDetailShowBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class BusinessCardDetailFragment extends AddressCardDetailFragment {
    private BusinessCardModel card;

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
    protected void loadLayout(JSONObject response) {
        card = CCJsonUtil.convertToModel(response.optString("card"), BusinessCardModel.class);
        Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(getImageView());
        binding.setVariable(BR.card, card);
        binding.executePendingBindings();
        super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_edit);
    }

    @Override
    protected void gotoCardEditFragment() {
        gotoFragment(BusinessCardEditFragment.newInstance(card));
    }
}
