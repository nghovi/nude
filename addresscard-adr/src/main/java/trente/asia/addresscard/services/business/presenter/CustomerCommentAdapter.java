package trente.asia.addresscard.services.business.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CommentItemCustomerDetailBinding;
import trente.asia.addresscard.services.business.model.CommentModel;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerCommentAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<CommentModel>                      comments = new ArrayList<>();
    private OnCustomerCommentAdapterListener        callback;

    public CustomerCommentAdapter(OnCustomerCommentAdapterListener listener) {
        this.callback = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item_customer_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentItemCustomerDetailBinding binding =
                (CommentItemCustomerDetailBinding) holder.getBinding();
        final CommentModel comment = comments.get(position);
        binding.setVariable(BR.comment, comment);
        binding.executePendingBindings();
        binding.btnDeleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.deleteComment(comment.key);
                comments.remove(comment);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public interface OnCustomerCommentAdapterListener {
        void deleteComment(int commentId);
    }
}
