package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCardDetailShowBinding;
import trente.asia.addresscard.services.business.model.CardModel;

/**
 * Created by tien on 5/11/2017.
 */

public class CardDetailFragment extends AbstractAddressCardFragment {
    private FragmentCardDetailShowBinding binding;
    private int key;
    private CardModel card;
    public static CardDetailFragment newInstance(int cardKey) {
        CardDetailFragment fragment = new CardDetailFragment();
        fragment.key = cardKey;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_detail_show, container, false);
            mRootView = binding.getRoot();
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);

        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();

    }

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(ACConst.AC_BUSINESS_CARD_DETAIL, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);

        card = CCJsonUtil.convertToModel(response.optString("card"), CardModel.class);
        Picasso.with(getContext()).load(BuildConfig.HOST + card.attachment.fileUrl).into(binding.cardImage);
        binding.setVariable(BR.card, card);
        binding.executePendingBindings();
        super.initHeader(R.drawable.ac_back_white, card.cardName, R.drawable.ac_action_edit);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                gotoFragment(CardEditFragment.newInstance(card));
                break;
            default:
                break;
        }
    }

    private void log(String msg) {
        Log.e("Card Detail Fragment", msg);
    }
}
