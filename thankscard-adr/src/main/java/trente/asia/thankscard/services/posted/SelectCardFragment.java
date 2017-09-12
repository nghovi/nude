package trente.asia.thankscard.services.posted;

import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.databinding.FragmentSelectCardBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.common.model.Template;
import trente.asia.thankscard.services.posted.adapter.CardAdapter;
import trente.asia.thankscard.services.posted.dialog.CannotUseAnimationDialog;
import trente.asia.thankscard.services.posted.dialog.CannotUsePhotoDialog;
import trente.asia.thankscard.services.posted.dialog.ChangeToNormalCardDialog;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by tien on 7/13/2017.
 */

public class SelectCardFragment extends AbstractTCFragment implements CardAdapter.OnCardAdapterListener, View.OnClickListener{
    private FragmentSelectCardBinding binding;
    private OnSelectCardListener callback;
    private CardAdapter adapterCards = new CardAdapter();
    private CardAdapter adapterPhoto = new CardAdapter();
    private CardAdapter adapterAnimations = new CardAdapter();
    private int pointSilver;
    private int pointGold;
    private int pointTotal;
    private boolean isBirtday;
    private boolean showConfirmDiaglog = false;

    public void setShowConfirmDiaglog(boolean showConfirmDiaglog) {
        this.showConfirmDiaglog = showConfirmDiaglog;
        log("showConfirmDiaglog = " + showConfirmDiaglog);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesAccountUtil preference = new PreferencesAccountUtil(getContext());
        pointSilver = Integer.parseInt(preference.get(TcConst.PREF_POINT_SILVER));
        pointGold = Integer.parseInt(preference.get(TcConst.PREF_POINT_GOLD));
        pointTotal = Integer.parseInt(preference.get(TcConst.PREF_POINT_TOTAL));
        isBirtday = Boolean.parseBoolean(new PreferencesSystemUtil(getContext()).get(TcConst.IS_BIRTHDAY));
    }

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
        binding.listCards.setAdapter(adapterCards);
        adapterCards.setCallback(this);
        adapterPhoto.setCallback(this);
        adapterAnimations.setCallback(this);
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

    public void setCards(List<Template> cards, List<Template> photo, List<Template> animations) {
        adapterCards.setCards(cards);
        adapterPhoto.setCards(photo);
        adapterAnimations.setCards(animations);
    }

    @Override
    public void onSelectCard(Template card) {
        if (showConfirmDiaglog) {
            showConfirmDialog(card);
            return;
        }
        selectCard(card);
    }

    private void selectCard(Template card) {
        if (callback != null) {
            callback.onSelectCardDone(card);
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_normal:
                showListNormalCards();
                break;
            case R.id.tab_photo:
                if (!isBirtday && pointTotal < pointSilver) {
                    new CannotUsePhotoDialog().show(getFragmentManager(), null);
                    return;
                }
                showListPhotoCards();
                break;
            case R.id.tab_animation:
                if (!isBirtday && pointTotal < pointGold) {
                    new CannotUseAnimationDialog().show(getFragmentManager(), null);
                    return;
                }
                showListAnimationCards();
                break;
        }
    }

    private void showListAnimationCards() {
        binding.listCards.setAdapter(adapterAnimations);
        binding.tabBottom.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tc_animation_card_tab));
    }

    private void showListNormalCards() {
        binding.listCards.setAdapter(adapterCards);
        binding.tabBottom.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tc_normal_card_tab));
    }

    private void showListPhotoCards() {
        binding.listCards.setAdapter(adapterPhoto);
        binding.tabBottom.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tc_photo_card_tab));
    }

    private void showConfirmDialog(final Template card){
        final ChangeToNormalCardDialog dialog = new ChangeToNormalCardDialog();
        dialog.setListeners(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                selectCard(card);
                dialog.dismiss();
            }
        }, new View.OnClickListener() {

            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), null);
    }

    public interface OnSelectCardListener {
        void onSelectCardDone(Template card);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }

    private void log(String msg) {
        Log.e("SelectCard", msg);
    }
}
