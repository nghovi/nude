package trente.asia.addresscard.services.business.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentBusinessCardMainBinding;
import trente.asia.addresscard.services.business.model.CardModel;
import trente.asia.addresscard.services.business.presenter.CardAdapter;

/**
 * Created by tien on 4/18/2017.
 */

public class BusinessCardMainFragment extends AbstractAddressCardFragment implements CardAdapter.OnItemListener {
    private FragmentBusinessCardMainBinding binding;
    private CardAdapter adapter;
    private Uri photoUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_card_main, container, false);
            binding.listCards.setLayoutManager(new GridLayoutManager(getContext(), 3));
            binding.btnDelete.setOnClickListener(this);
            binding.btnCapture.setOnClickListener(this);
            binding.rowCategory.setOnClickListener(this);
            binding.rowCustomer.setOnClickListener(this);
            List<CardModel> cards = new ArrayList<>();
            adapter = new CardAdapter(cards, this);
            binding.listCards.setAdapter(adapter);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        super.initHeader(null, getString(R.string.ac_main_card_title), null);
    }

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        requestLoad(ACConst.AC_BUSINESS_CARD_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        List<CardModel> cards = CCJsonUtil.convertToModelList(
                response.optString("cards"), CardModel.class);
        adapter = new CardAdapter(cards, this);
        binding.listCards.setAdapter(adapter);
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
            case R.id.row_category:
                gotoFragment(new CategoryListFragment());
                break;
            case R.id.row_customer:
                gotoFragment(new CustomerListFragment(), "customer_list");
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(CardModel card) {
        gotoFragment(CardDetailFragment.newInstance(card.key));
    }

    @Override
    public void onItemLongClickListener() {
        showBtnDelete();
    }

    @Override
    public void onUnselectAllItems() {
        showBtnCapture();
    }

    public void showBtnDelete() {
        binding.btnDelete.setVisibility(View.VISIBLE);
        binding.btnCapture.setVisibility(View.GONE);
    }

    public void showBtnCapture() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnCapture.setVisibility(View.VISIBLE);
    }

    private void takeCapture() {
        ContentValues values = new ContentValues();
        photoUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, ACConst.AC_REQUEST_CODE_TAKE_CAPTURE);
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
            gotoFragment(UploadAddressCardFragment.newInstance(cardBitmap, logoBitmap));
        }
    }

    public void onBtnDeleteClick() {
        String cardIds = "";
        for (CardModel card : adapter.getListSelected()) {
            cardIds += card.key + ",";
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cardIds", cardIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_CARD_DELETE, jsonObject, true);
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

    private void log(String msg) {
        Log.e("BusinessCardMain", msg);
    }
}