package trente.asia.addresscard.commons.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.business.model.AddressCardModel;
import trente.asia.addresscard.services.business.presenter.CardAdapter;
import trente.asia.addresscard.services.business.view.CardCameraPreviewFragment;

/**
 * Created by tien on 4/18/2017.
 */

public abstract class AddressCardListFragment extends AbstractAddressCardFragment implements CardAdapter.OnItemListener {
    protected CardAdapter adapter;
    protected Uri photoUri;


    protected abstract void initViewBinding(LayoutInflater inflater, ViewGroup container);

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }


    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        requestLoad(getApiLoadString(), jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        List<AddressCardModel> cards = CCJsonUtil.convertToModelList(
                response.optString("cards"), AddressCardModel.class);
        adapter.setCards(cards);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                onBtnDeleteClick();
                break;
            case R.id.btn_capture:
                takeCapture();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemLongClickListener() {
        showBtnDelete();
    }

    @Override
    public void onUnselectAllItems() {
        showBtnCapture();
    }

    private void takeCapture() {
        ContentValues values = new ContentValues();
        photoUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, ACConst.AC_REQUEST_CODE_TAKE_CAPTURE);
//        Bitmap card = BitmapFactory.decodeResource(getResources(), R.drawable.card);
//        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
//        gotoFragment(CardCameraPreviewFragment.newInstance(card, logo, ""));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACConst.AC_REQUEST_CODE_TAKE_CAPTURE &&
                resultCode == Activity.RESULT_OK) {
            Bitmap cardBitmap = null;
            try {
                cardBitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            String uploadApi = getUploadApi();
            gotoFragment(CardCameraPreviewFragment.newInstance(cardBitmap, logoBitmap, uploadApi));
        }
    }

    protected abstract String getUploadApi();

    public void onBtnDeleteClick() {
        String cardIds = "";
        for (AddressCardModel card : adapter.getSelectedCards()) {
            cardIds += card.key + ",";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cardIds", cardIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(getApiDeleteString(), jsonObject, true);
        adapter.deleteSelectedCards();
        showBtnCapture();
    }

    @Override
    protected void onClickBackBtn() {
        showBtnCapture();
        if (adapter.unselectAllCards()) {
            return;
        }
        super.onClickBackBtn();
    }

    protected abstract String getApiLoadString();
    protected abstract String getApiDeleteString();
    protected abstract void showBtnCapture();
    protected abstract void showBtnDelete();

    private void log(String msg) {
        Log.e("BusinessCardMain", msg);
    }
}