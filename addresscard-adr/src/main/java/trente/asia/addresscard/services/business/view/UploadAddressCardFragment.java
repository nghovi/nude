package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentUploadAddressCardBinding;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/10/2017.
 */

public class UploadAddressCardFragment extends AbstractAddressCardFragment {
    FragmentUploadAddressCardBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater,
                    R.layout.fragment_upload_address_card, container, false);
            mRootView = binding.getRoot();
            mRootView.findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, "Upload address card", R.drawable.ac_action_done);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                uploadAddressCard();
                break;
            default:
                break;
        }
    }

    private void uploadAddressCard() {
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
        Map<String, File> fileMap = new HashMap<>();
        String cardImagePath = Environment.getExternalStorageDirectory() + "/card.jpg";
        String logoPath = Environment.getExternalStorageDirectory() + "/logo.jpg";
        File cardImage = new File(cardImagePath);
        File logo = new File(logoPath);
        fileMap.put("card", cardImage);
        fileMap.put("logo", logo);
        Log.e("Upload Address Card", jsonObject.toString());
        requestUpload(ACConst.AC_BUSINESS_CARD_NEW, jsonObject, fileMap, true);
    }

    @Override
    protected void successUpload(JSONObject response, String url) {
        super.successUpload(response, url);
        onClickBackBtn();
    }
}
