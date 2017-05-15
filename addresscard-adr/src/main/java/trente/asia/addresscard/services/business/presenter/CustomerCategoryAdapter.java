package trente.asia.addresscard.services.business.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import trente.asia.addresscard.R;
import trente.asia.addresscard.services.business.model.CustomerModel;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerCategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<CustomerModel> customers;

    public CustomerCategoryAdapter(List<CustomerModel> customers) {
        this.customers = customers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.customer_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel customer = customers.get(position);
        holder.getBinding().setVariable(BR.customer, customer);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}
