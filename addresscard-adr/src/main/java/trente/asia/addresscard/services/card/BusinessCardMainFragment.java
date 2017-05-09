package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentBusinessCardMainBinding;

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
        List<Card> list = new ArrayList<>();
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        list.add(new Card("Name001", R.drawable.card, "By Bkmsx"));
        adapter = new CardAdapter(list, this);
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerview.setAdapter(adapter);
        binding.btnDelete.setOnClickListener(this);
        binding.btnCapture.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                onBtnDeleteClick();
                break;
            case R.id.btn_capture:
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