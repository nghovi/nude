package trente.asia.team360.services.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.team360.R;
import trente.asia.team360.common.fragments.AbstractTmFragment;

/**
 * Created by takano-yasuhiro on 2017/06/19.
 */

public class TmSettingViewFragment extends AbstractTmFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_setting_view, container, false);
        return mRootView;
    }


    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_common_footer_setting;
    }
}
