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

import trente.asia.addresscard.setting.SettingFragment;

/**
 * Created by tien on 4/18/2017.
 */

public class ACMainFragment extends AbstractAddressCardFragment{

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

    }


    @Override
    public void onClick(View view) {

    }
}