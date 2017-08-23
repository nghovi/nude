package asia.trente.officeletter.services.document.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.holder.ViewHolder;
import asia.trente.officeletter.databinding.ItemDocumentBinding;
import asia.trente.officeletter.services.document.model.DocumentModel;

/**
 * Created by tien on 8/23/2017.
 */

public class DocumentAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<DocumentModel> documents;
    private OnDocumentAdapterListener callback;

    public void setCallback(OnDocumentAdapterListener callback) {
        this.callback = callback;
    }

    public void setDocuments(List<DocumentModel> documents) {
        this.documents = documents;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemDocumentBinding binding = (ItemDocumentBinding) holder.getBinding();
        final DocumentModel documentModel = documents.get(position);
        binding.docName.setText(documentModel.document.documentTitle);
        String deliveryDate = documentModel.deliveryDate.substring(0, 10);
        binding.docDate.setText(deliveryDate.replace("/", "."));
        if (documentModel.passwordHint == null) {
            binding.imgLock.setVisibility(View.INVISIBLE);
        }
        binding.rltDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onDocumentClick(documentModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents == null ? 0 : documents.size();
    }

    public interface OnDocumentAdapterListener {
        void onDocumentClick(DocumentModel documentModel);
    }
}
