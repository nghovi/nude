package asia.trente.officeletter.services.document;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.barteksc.pdfviewer.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.commons.fragment.AbstractOLFragment;
import asia.trente.officeletter.commons.utils.OLUtils;
import asia.trente.officeletter.databinding.FragmentDocumentDetailBinding;
import asia.trente.officeletter.services.document.listener.OnDownloadListener;
import asia.trente.officeletter.services.document.model.DocumentModel;

/**
 * Created by tien on 8/23/2017.
 */

public class DocumentDetailFragment extends AbstractOLFragment implements View.OnClickListener, OnDownloadListener {
    private DocumentModel documentModel;
    private FragmentDocumentDetailBinding binding;
    private boolean showMessage = false;

    public void setDocumentModel(DocumentModel documentModel) {
        this.documentModel = documentModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_document_detail, container, false);
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
        initHeader(R.drawable.wf_back_white, documentModel.document.documentTitle, R.drawable.ic_download);
    }

    @Override
    protected void initData() {
        super.initData();
        loadDocumentDetail();
    }

    private void loadDocumentDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", documentModel.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(OLConst.API_OL_DOCUMENT_DETAIL, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (OLConst.API_OL_DOCUMENT_DETAIL.equals(url)) {
            try {
                documentModel = LoganSquare.parse(response.optString("document"), DocumentModel.class);
                if ("DNE".equals(documentModel.deliveryStatus)) {
                    OLUtils.downloadFilePrivate(activity, host + documentModel.document.attachment.fileUrl, this);
                    binding.pdfNote.setText(documentModel.document.documentMessage);
                    getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
                    getView().findViewById(R.id.img_id_header_information).setOnClickListener(this);
                } else {
                    OLUtils.showAlertDialog(getContext(), R.string.ol_message_file_not_found);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.successLoad(response, url);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_id_header_right_icon:
                OLUtils.downloadFile(activity, documentModel.document.documentTitle, host + documentModel.document.attachment.fileUrl);
                break;
            case R.id.img_id_header_information:
                if (showMessage) {
                    binding.pdfNote.setVisibility(View.GONE);
                    showMessage = false;
                } else {
                    showMessage = true;
                    binding.pdfNote.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onDownloadCompleted() {
        binding.pdfViewer.fromFile(new File(getContext().getFilesDir(), OLConst.PDF_TEMP_NAME)).load();
    }

    private void log(String msg) {
        Log.e("DocumentDetail", msg);
    }
}
