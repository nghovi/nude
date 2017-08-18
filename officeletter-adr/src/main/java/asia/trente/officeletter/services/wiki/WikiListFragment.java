package asia.trente.officeletter.services.wiki;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.fragment.AbstractListFragment;

/**
 * Created by tien on 8/18/2017.
 */

public class WikiListFragment extends AbstractListFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_wiki_list, container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(null, getString(R.string.ol_document_list), null);
    }

    @Override
    protected int getFooterItemId() {
        return R.id.lnr_view_common_footer_wiki;
    }
}
