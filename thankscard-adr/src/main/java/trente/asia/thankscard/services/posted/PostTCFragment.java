package trente.asia.thankscard.services.posted;

import trente.asia.thankscard.fragments.AbstractTCFragment;

/**
 * Created by tien on 7/12/2017.
 */

public class PostTCFragment extends AbstractTCFragment {

    @Override
    public int getFragmentLayoutId() {
        return 0;
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

    }
}
