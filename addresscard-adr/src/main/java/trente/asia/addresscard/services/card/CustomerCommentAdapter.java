package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CommentItemCustomerDetailBinding;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerCommentAdapter extends RecyclerView.Adapter<ViewHolder> {
    CommentItemCustomerDetailBinding binding;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.comment_item_customer_detail, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
