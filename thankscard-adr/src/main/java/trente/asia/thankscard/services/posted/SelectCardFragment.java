package trente.asia.thankscard.services.posted;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.FragmentSelectCardBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.thankscard.services.posted.adapter.CardAdapter;

/**
 * Created by tien on 7/13/2017.
 */

public class SelectCardFragment extends AbstractTCFragment implements CardAdapter.OnCardAdapterListener, View.OnClickListener{
    private FragmentSelectCardBinding binding;
    private List<Template> cards;
    private OnSelectCardListener callback;
    private CardAdapter adapter = new CardAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_card, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_select_card;
    }

    @Override
    public boolean hasBackBtn() {
        return false;
    }

    @Override
    public boolean hasSettingBtn() {
        return false;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    public int getTitle() {
        return 0;
    }

    @Override
    public void buildBodyLayout() {
        binding.listCards.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.listCards.setAdapter(adapter);
        adapter.setCallback(this);
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        binding.tabNormal.setOnClickListener(this);
        binding.tabPhoto.setOnClickListener(this);
        binding.tabAnimation.setOnClickListener(this);
    }

    public void setCallback(OnSelectCardListener callback) {
        this.callback = callback;
    }

    public void setCards(List<Template> cards) {
        this.cards = cards;
        adapter.setCards(cards);
    }

    @Override
    public void onSelectCard(Template card) {
        if (callback != null) {
            callback.onSelectCardDone(card);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_normal:
                binding.tabBottom.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tc_normal_card_tab));
                break;
            case R.id.tab_photo:
                binding.tabBottom.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tc_photo_card_tab));
                break;
            case R.id.tab_animation:
                binding.tabBottom.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tc_animation_card_tab));
                break;
        }
    }

    public interface OnSelectCardListener {
        void onSelectCardDone(Template card);
    }
}
