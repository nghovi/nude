package asia.trente.officeletter.services.wiki;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import asia.trente.officeletter.commons.utils.OLUtils;
import asia.trente.officeletter.databinding.FragmentWikiDetailBinding;
import asia.trente.officeletter.services.wiki.model.WikiModel;

/**
 * Created by tien on 8/24/2017.
 */

public class WikiDetailFragment extends AbstractOLFragment {
    private FragmentWikiDetailBinding binding;
    private int wikiId;

    public void setWikiId(int wikiId) {
        this.wikiId = wikiId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_wiki_detail, container, false);
            binding = DataBindingUtil.bind(mRootView);
        }
        return mRootView;
    }

    @Override
    protected int getFooterItemId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, "", null);
        loadWikiDetail();
    }

    private void loadWikiDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", wikiId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(OLConst.API_OL_WIKI_DETAIL, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (OLConst.API_OL_WIKI_DETAIL.equals(url)) {
            try {
                WikiModel wikiModel = LoganSquare.parse(response.optString("wiki"), WikiModel.class);
                if (wikiModel == null) {
                    OLUtils.showAlertDialog(getContext(), R.string.ol_message_file_not_found);
                } else {
                    initHeader(R.drawable.wf_back_white, wikiModel.wikiTitle, null);
                    binding.webview.getSettings().setJavaScriptEnabled(true);
                    binding.webview.loadDataWithBaseURL("", wikiModel.wikiContent, "text/html", "UTF-8", "");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.successLoad(response, url);
        }
    }
}
