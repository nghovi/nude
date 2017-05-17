package trente.asia.addresscard.services.business.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CustomerDialogItemBinding;
import trente.asia.addresscard.services.business.model.CustomerModel;

/**
 * Created by tien on 5/11/2017.
 */

public class CustomerDialogAdapter extends RecyclerView.Adapter<ViewHolder>{
    private List<CustomerModel>                 list;
    private CustomerDialogItemBinding           binding;
    private OnCustomerDialogListener            callback;

    public CustomerDialogAdapter(List<CustomerModel> list, OnCustomerDialogListener listener) {
        this.list = list;
        this.callback = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_dialog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel customer = list.get(position);
        binding = (CustomerDialogItemBinding) holder.getBinding();
        binding.setVariable(BR.customer, customer);
        binding.executePendingBindings();
        binding.getRoot().setOnClickListener((View v) -> {
            callback.onSelectCustomer(customer);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnCustomerDialogListener {
        public void onSelectCustomer(CustomerModel customer);
    }
}
