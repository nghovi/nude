package trente.asia.addresscard.services.card;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CustomerCategoryItemBinding;
import trente.asia.addresscard.services.card.model.CustomerModel;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerCategoryEditAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<CustomerModel> customers;
    CustomerCategoryItemBinding binding;

    public CustomerCategoryEditAdapter(List<CustomerModel> customers) {
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
        binding = (CustomerCategoryItemBinding) holder.getBinding();
        binding.setVariable(BR.customer, customer);
        binding.executePendingBindings();
        binding.imageCheckbox.setImageResource(R.drawable.ac_action_check);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}
