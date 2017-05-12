package trente.asia.addresscard.services.card;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;
import trente.asia.addresscard.services.card.model.CustomerModel;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerDialogAdapter extends RecyclerView.Adapter<ViewHolder>{
    List<CustomerModel> list;

    public CustomerDialogAdapter(List<CustomerModel> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_dialog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel customer = list.get(position);
        holder.getBinding().setVariable(BR.customer, customer);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
