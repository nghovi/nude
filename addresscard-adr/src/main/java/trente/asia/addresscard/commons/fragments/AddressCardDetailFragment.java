package trente.asia.addresscard.commons.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import trente.asia.addresscard.R;
import trente.asia.addresscard.services.business.view.BusinessCardDetailFragment;

/**
 * Created by tien on 5/23/2017.
 */

public abstract class AddressCardDetailFragment extends AbstractAddressCardFragment {
    protected   int                                 key;
    protected   ViewDataBinding                     binding;

    public static BusinessCardDetailFragment newInstance(int cardKey) {
        BusinessCardDetailFragment fragment = new BusinessCardDetailFragment();
        fragment.key = cardKey;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            mRootView = binding.getRoot();
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    protected abstract int getLayoutId();

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String api = getApi();
        requestLoad(api, jsonObject, true);
    }

    protected abstract String getApi();

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);
        loadLayout(response);
    }

    protected abstract void loadLayout(JSONObject response);

    protected abstract ImageView getImageView();

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                gotoCardEditFragment();
                break;
            default:
                break;
        }
    }

    protected abstract void gotoCardEditFragment();

    private void log(String msg) {
        Log.e("Card Detail Fragment", msg);
    }
}
