package trente.asia.addresscard.commons.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import trente.asia.addresscard.R;
import trente.asia.addresscard.services.card.CardFragment;
import trente.asia.addresscard.services.company.CompanyFragment;
import trente.asia.addresscard.services.history.HistoryFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import trente.asia.addresscard.setting.SettingFragment;

/**
 * Created by tien on 4/18/2017.
 */

public class ACMainFragment extends AbstractAddressCardFragment implements View.OnClickListener{
    CardFragment cardFragment;
    CompanyFragment companyFragment;
    HistoryFragment historyFragment;
    SettingFragment settingFragment;

//    @BindView(R.id.tab_card) TextView tabCard;
//    @BindView(R.id.tab_company) TextView tabCompany;
//    @BindView(R.id.tab_history) TextView tabHistory;
//    @BindView(R.id.tab_setting) TextView tabSetting;

    int oldActiveViewId, newActiveViewId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Nullable // ???@Tien, what is it
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(mRootView == null){
            mRootView = inflater.inflate(R.layout.main_fragment, container, false);
        }
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
//        ButterKnife.bind(this, mRootView);
//        tabCard.setOnClickListener(this);
//        tabCompany.setOnClickListener(this);
//        tabHistory.setOnClickListener(this);
//        tabSetting.setOnClickListener(this);
//        cardFragment = new CardFragment();
//        addFragment(cardFragment);
//        oldActiveViewId = R.id.tab_card;
    }

    @Override
    public void onClick(View view) {
//        newActiveViewId = view.getId();
//        if (newActiveViewId == oldActiveViewId) {
//            return;
//        }
//        switch (newActiveViewId) {
//            case R.id.tab_card:
//                if (cardFragment == null) {
//                    cardFragment = new CardFragment();
//                }
//                gotoFragment(cardFragment);
//                break;
//            case R.id.tab_company:
//                if (companyFragment == null) {
//                    companyFragment = new CompanyFragment();
//                }
//                gotoFragment(companyFragment);
//                break;
//            case R.id.tab_history:
//                if (historyFragment == null) {
//                    historyFragment = new HistoryFragment();
//                }
//                gotoFragment(historyFragment);
//                break;
//            case R.id.tab_setting:
//                if (settingFragment == null) {
//                    settingFragment = new SettingFragment();
//                }
//                gotoFragment(settingFragment);
//                break;
//        }
//        updateActiveTab();
//        oldActiveViewId = view.getId();
    }

    private void updateActiveTab() {
        int activeColor = Color.WHITE;
        int deactiveColor = ContextCompat.getColor(getContext(), R.color.deactive_tab_text_color);
//        switch (newActiveViewId) {
//            case R.id.tab_card:
//                tabCard.setTextColor(activeColor);
//                tabCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.active_tab_card, 0, 0);
//                break;
//            case R.id.tab_company:
//                tabCompany.setTextColor(activeColor);
//                tabCompany.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.active_tab_company, 0, 0);
//                break;
//            case R.id.tab_history:
//                tabHistory.setTextColor(activeColor);
//                tabHistory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.active_tab_history, 0, 0);
//                break;
//            case R.id.tab_setting:
//                tabSetting.setTextColor(activeColor);
//                tabSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.active_tab_setting, 0, 0);
//                break;
//        }

        switch (oldActiveViewId) {
//            case R.id.tab_card:
//                tabCard.setTextColor(deactiveColor);
//                tabCard.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.deactive_tab_card, 0, 0);
//                break;
//            case R.id.tab_company:
//                tabCompany.setTextColor(deactiveColor);
//                tabCompany.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.deactive_tab_company, 0, 0);
//                break;
//            case R.id.tab_history:
//                tabHistory.setTextColor(deactiveColor);
//                tabHistory.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.deactive_tab_history, 0, 0);
//                break;
//            case R.id.tab_setting:
//                tabSetting.setTextColor(deactiveColor);
//                tabSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.deactive_tab_setting, 0, 0);
//                break;
        }
    }

//    private void addFragment(Fragment fragment) {
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
//                .addToBackStack(fragment.getClass().toString()).commit();
//    }
}