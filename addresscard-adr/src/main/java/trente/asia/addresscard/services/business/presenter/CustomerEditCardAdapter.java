package trente.asia.addresscard.services.business.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CardItemCustomerEditBinding;

/**
 * Created by tien on 5/12/2017.
 */

public class CustomerEditCardAdapter extends RecyclerView.Adapter<ViewHolder>{

    CardItemCustomerEditBinding binding;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_item_customer_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        binding = (CardItemCustomerEditBinding) holder.getBinding();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
