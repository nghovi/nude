package asia.trente.officeletter.services.wiki;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.commons.fragment.AbstractListFragment;
import asia.trente.officeletter.databinding.FragmentWikiListBinding;
import asia.trente.officeletter.services.wiki.adapter.WikiAdapter;
import asia.trente.officeletter.services.wiki.listener.OnWikiAdapterListener;
import asia.trente.officeletter.services.wiki.model.WikiModel;

/**
 * Created by tien on 8/18/2017.
 */

public class WikiListFragment extends AbstractListFragment implements OnWikiAdapterListener{

	private FragmentWikiListBinding	binding;
	private WikiAdapter				adapter	= new WikiAdapter();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_wiki_list, container, false);
			binding = DataBindingUtil.bind(mRootView);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(null, myself.userName, null);
        binding.rlvWiki.setAdapter(adapter);
        binding.rlvWiki.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setCallback(this);
	}

    @Override
    protected void initData() {
        super.initData();
        loadWikiList();
    }

    private void loadWikiList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("execType", "PUBLIC");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(OLConst.API_OL_WIKI_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (OLConst.API_OL_WIKI_LIST.equals(url)) {
            try {
                List<WikiModel> wikis = LoganSquare.parseList(response.optString("wikis"), WikiModel.class);
                adapter.setWikis(wikis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.successLoad(response, url);
        }
    }

    @Override
	protected int getFooterItemId(){
		return R.id.lnr_view_common_footer_wiki;
	}

	@Override
	public void onWikiClick(int wikiId){
        WikiDetailFragment fragment = new WikiDetailFragment();
        fragment.setWikiId(wikiId);
        gotoFragment(fragment);
	}
}
