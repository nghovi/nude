package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentBusinessCardMainBinding;
import trente.asia.addresscard.services.card.model.CardModel;

/**
 * Created by tien on 4/18/2017.
 */

public class BusinessCardMainFragment extends AbstractAddressCardFragment implements CardAdapter.OnItemListener{
    FragmentBusinessCardMainBinding binding;
    CardAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_business_card_main, container, false);
        binding.listCards.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.btnDelete.setOnClickListener(this);
        binding.btnCapture.setOnClickListener(this);
        binding.rowCategory.setOnClickListener(this);
        binding.rowCustomer.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, getString(R.string.ac_main_card_title), R.drawable.ac_action_edit);
    }

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        requestLoad(ACConst.AC_BUSINESS_CARD_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        log(url);
        log(response.toString());
        List<CardModel> cards;
        try {
            cards = LoganSquare.parseList(response.optString("cards"), CardModel.class);
            log("CardName: " + cards.get(0).cardName);
            log("Image url: " + cards.get(0).attachment.fileUrl);
            adapter = new CardAdapter(cards, this);
            binding.listCards.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                onBtnDeleteClick();
                break;
            case R.id.btn_capture:
                gotoFragment(new UploadAddressCardFragment());
                break;
            case R.id.row_category:
                gotoFragment(new CategoryListFragment());
                break;
            case R.id.row_customer:

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

    public void showBtnDelete() {
        binding.btnDelete.setVisibility(View.VISIBLE);
        binding.btnCapture.setVisibility(View.GONE);
    }

    public void showBtnCapture() {
        binding.btnDelete.setVisibility(View.GONE);
        binding.btnCapture.setVisibility(View.VISIBLE);
    }

    public void onBtnDeleteClick() {
        adapter.deleteSelectedCards();
        showBtnCapture();
    }

    private void log(String msg) {
        Log.e("BusinessCardMain", msg);
    }
}