package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CustomerCategoryItemBinding;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/11/2017.
 */

public class CategoryEditCustomerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<CustomerModel>                 allCustomers;
    private Context                             context;
    private List<Integer>                       selectedCustomerIds = new ArrayList<>();

    public CategoryEditCustomerAdapter(List<CustomerModel> customers, List<CustomerModel> allCustomers) {
        this.allCustomers = allCustomers;
        for (CustomerModel customer : customers) {
            selectedCustomerIds.add(customer.key);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                        .inflate(R.layout.customer_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel customer = allCustomers.get(position);
        CustomerCategoryItemBinding binding = (CustomerCategoryItemBinding) holder.getBinding();
        binding.setVariable(BR.customer, customer);
        binding.executePendingBindings();
        WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, binding.customerLogo,
                customer.attachment.fileUrl, R.drawable.default_logo);
        if (selectedCustomerIds.indexOf(customer.key) >= 0) {
            binding.imageCheckbox.setVisibility(View.VISIBLE);
        } else {
            binding.imageCheckbox.setVisibility(View.INVISIBLE);
        }

        binding.getRoot().setOnClickListener((View v) -> {
            int index = selectedCustomerIds.indexOf(customer.key);
            if (index >= 0) {
                selectedCustomerIds.remove(index);
                binding.imageCheckbox.setVisibility(View.INVISIBLE);
            } else {
                selectedCustomerIds.add(customer.key);
                binding.imageCheckbox.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCustomers.size();
    }

    public String getCustomerIds() {
        String customerIds = "";
        for (int customerId : selectedCustomerIds) {
            customerIds += customerId + ",";
        }
        return customerIds;
    }

    private void log(String msg) {
        Log.e("CustomerAdapter", msg);
    }
}
