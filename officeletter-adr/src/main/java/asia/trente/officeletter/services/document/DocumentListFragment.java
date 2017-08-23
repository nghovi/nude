package asia.trente.officeletter.services.document;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import asia.trente.officeletter.databinding.FragmentDocumentListBinding;
import asia.trente.officeletter.services.document.adapter.DocumentAdapter;
import asia.trente.officeletter.services.document.model.DocumentModel;

/**
 * Created by tien on 8/18/2017.
 */

public class DocumentListFragment extends AbstractListFragment implements DocumentAdapter.OnDocumentAdapterListener,
        View.OnClickListener, DocumentFilterFragment.OnDocumentFilterListener{
    private FragmentDocumentListBinding binding;
    private DocumentAdapter adapter = new DocumentAdapter();
    private String searchStart;
    private String searchEnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchStart = getString(R.string.chiase_common_none);
        searchEnd = getString(R.string.chiase_common_none);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_document_list, container, false);
            binding = DataBindingUtil.bind(mRootView);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(null, myself.userName, R.drawable.ic_filter);
        binding.rlvDocument.setAdapter(adapter);
        binding.rlvDocument.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.txtFilter.setText(getString(R.string.ol_filter), );
        getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
    }

    @Override
    protected int getFooterItemId() {
        return R.id.lnr_view_common_footer_document;
    }

    @Override
    protected void initData() {
        super.initData();
        loadDocumentList();
    }

    private void loadDocumentList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("searchStart", searchStart);
            jsonObject.put("searchEnd", searchEnd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(OLConst.API_OL_DOCUMENT_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (OLConst.API_OL_DOCUMENT_LIST.equals(url)) {
            try {
                List<DocumentModel> documents = LoganSquare.parseList(response.optString("documents"), DocumentModel.class);
                adapter.setDocuments(documents);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.successLoad(response, url);
        }
    }

    @Override
    public void onDocumentClick(DocumentModel documentModel) {
        log(documentModel.document.documentTitle);
    }

    private void log(String msg) {
        Log.e("DocumentList", msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_id_header_right_icon:
                DocumentFilterFragment fragment = new DocumentFilterFragment();
                fragment.setCallback(this);
                fragment.setFilterCondition(searchStart, searchEnd);
                gotoFragment(fragment);
                break;
        }
    }

    @Override
    public void onDocumentFilterDone(String startMonth, String endMonth) {
        searchStart = startMonth;
        searchEnd = endMonth;
        String filter = "";
        if (searchStart == null) {
            filter += getString(R.string.chiase_common_none);
        } else {
            filter += searchStart;
        }
        filter += " - ";
        if (searchEnd == null) {
            filter += getString(R.string.chiase_common_none);
        } else {
            filter += searchEnd;
        }
        binding.txtFilter.setText();
    }
}
