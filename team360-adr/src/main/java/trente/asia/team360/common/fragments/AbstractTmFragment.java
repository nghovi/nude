package trente.asia.team360.common.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import trente.asia.team360.BuildConfig;
import trente.asia.team360.services.login.TmLoginFragment;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.WelfareConst;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractTmFragment extends WelfareFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        host = BuildConfig.HOST;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildFooter();
    }

    @Override
    protected void initView() {
        super.initView();
    }


    protected void buildFooter() {

    }



    private void setSelectedFooterItem(int footerItemId) {
        ImageView imgFooterItem = (ImageView) ((LinearLayout) getView().findViewById(footerItemId)).getChildAt(0);
        imgFooterItem.setAlpha((float) 1);

        TextView txtFooterItem = (TextView) ((LinearLayout) getView().findViewById(footerItemId)).getChildAt(1);
        txtFooterItem.setTextColor(Color.WHITE);
    }


    @Override
    protected String getServiceCd() {
        return WelfareConst.SERVICE_CD_SW;
    }

    @Override
    protected void gotoSignIn() {
        super.gotoSignIn();
        gotoFragment(new TmLoginFragment());
    }
}
