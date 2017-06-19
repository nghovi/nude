package trente.asia.team360.common.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import trente.asia.team360.BuildConfig;
import trente.asia.team360.R;
import trente.asia.team360.services.login.TmLoginFragment;
import trente.asia.team360.services.member.TmMemberViewFragment;
import trente.asia.team360.services.setting.TmSettingViewFragment;
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

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    protected void initView() {
        super.initView();
        buildFooter();
    }


    private int activeFooterItemId;

    protected void buildFooter() {

        final int footerItemId = getFooterItemId();
        if(footerItemId != 0){
            View.OnClickListener listener = new View.OnClickListener() {

                @Override
                public void onClick(View view){
                    switch(view.getId()){
                        case R.id.lnr_view_common_footer_member:
                            if(activeFooterItemId != R.id.lnr_view_common_footer_member){
                                activeFooterItemId = R.id.lnr_view_common_footer_member;
                                emptyBackStack();
                                gotoFragment(new TmMemberViewFragment());
                            }
                            break;
                        case R.id.lnr_view_common_footer_camera:
                            if(activeFooterItemId != R.id.lnr_view_common_footer_camera){
                                activeFooterItemId = R.id.lnr_view_common_footer_camera;
                                emptyBackStack();
                                gotoFragment(new TmMemberViewFragment());
                            }
                            break;
                        case R.id.lnr_view_common_footer_setting:
                            if(activeFooterItemId != R.id.lnr_view_common_footer_setting){
                                activeFooterItemId = R.id.lnr_view_common_footer_setting;
                                emptyBackStack();
                                gotoFragment(new TmSettingViewFragment());
                            }
                            break;
                        default:
                            break;
                    }
                }
            }; // end listener

            getView().findViewById(R.id.lnr_view_common_footer_member).setOnClickListener(listener);
            getView().findViewById(R.id.lnr_view_common_footer_camera).setOnClickListener(listener);
            getView().findViewById(R.id.lnr_view_common_footer_setting).setOnClickListener(listener);
            setSelectedFooterItem(footerItemId);

        }
    }

    public abstract int getFooterItemId();

    private void setSelectedFooterItem(int footerItemId) {
        ImageView imgFooterItem = (ImageView) ((RelativeLayout) getView().findViewById(footerItemId)).getChildAt(0);
        imgFooterItem.setAlpha((float) 1);

        TextView txtFooterItem = (TextView) ((RelativeLayout) getView().findViewById(footerItemId)).getChildAt(1);
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
