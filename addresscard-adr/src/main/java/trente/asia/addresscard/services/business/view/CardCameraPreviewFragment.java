package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.commons.utils.Utils;
import trente.asia.addresscard.databinding.FragmentUploadAddressCardBinding;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/10/2017.
 */

public class CardCameraPreviewFragment extends AbstractAddressCardFragment {
    private FragmentUploadAddressCardBinding    binding;
    private Bitmap                              cardBitmap;
    private Bitmap                              logoBitmap;
    private String                              cardPath;
    private String                              logoPath;
    private String                              apiString;


    public static CardCameraPreviewFragment newInstance(Bitmap cardBitmap, Bitmap logoBitmap, String apiString) {
        CardCameraPreviewFragment fragment = new CardCameraPreviewFragment();
        fragment.cardBitmap = cardBitmap;
        fragment.logoBitmap = logoBitmap;
        fragment.apiString = apiString;
        return fragment;
    }

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
    protected void initData() {
        super.initData();
        new LoadImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class LoadImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String folderCamera = Utils.createFolder("Camera");
            cardPath = Utils.copyImageToStorage(folderCamera, System.currentTimeMillis() + ".png", cardBitmap, 5);
            logoPath = Utils.copyImageToStorage(folderCamera, "logo" + System.currentTimeMillis() + ".png", logoBitmap, 1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Picasso.with(getContext()).load(new File(cardPath)).into(binding.cardImage);
            Picasso.with(getContext()).load(new File(logoPath)).into(binding.customerLogo);
        }
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

    public void setApiString(String apiString) {
        this.apiString = apiString;
    }

    private void uploadAddressCard() {
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
        Map<String, File> fileMap = new HashMap<>();
        File cardImage = new File(cardPath);
        File logo = new File(logoPath);
        fileMap.put("card", cardImage);
        fileMap.put("logo", logo);
        Log.e("Upload Address Card", jsonObject.toString());
        requestUpload(this.apiString, jsonObject, fileMap, true);
    }

    @Override
    protected void successUpload(JSONObject response, String url) {
        super.successUpload(response, url);
        onClickBackBtn();
    }

    private void log(String msg) {
        Log.e("UploadAddressCard", msg);
    }
}
