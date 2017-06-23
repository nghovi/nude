package trente.asia.addresscard.commons.fragments;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.activities.CameraActivity;
import trente.asia.addresscard.commons.defines.ACConst;
import trente.asia.addresscard.services.business.model.AddressCardModel;
import trente.asia.addresscard.services.business.presenter.CardAdapter;
import trente.asia.addresscard.services.business.view.CardCameraPreviewFragment;

/**
 * Created by tien on 4/18/2017.
 */

public abstract class AddressCardListFragment extends AbstractAddressCardFragment implements CardAdapter.OnItemListener {
    protected CardAdapter adapter;

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
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, ACConst.AC_REQUEST_CODE_TAKE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String cardPath = data.getStringExtra("cardPath");
            String logoPath = data.getStringExtra("logoPath");
            String text = data.getStringExtra("text");
            gotoFragment(CardCameraPreviewFragment.newInstance(cardPath, logoPath, text, getUploadApi()));
        } else {
            log("Camera cancel");
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