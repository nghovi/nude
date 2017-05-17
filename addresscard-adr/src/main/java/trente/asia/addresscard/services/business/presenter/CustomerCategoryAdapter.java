package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class CustomerCategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<CustomerModel> customers;
    private CustomerCategoryItemBinding binding;
    private Context context;

    public CustomerCategoryAdapter() {
        this.customers = new ArrayList<>();
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
        binding = (CustomerCategoryItemBinding) holder.getBinding();
        CustomerModel customer = customers.get(position);
        binding.setVariable(BR.customer, customer);
        binding.executePendingBindings();
        WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, binding.customerLogo,
                customer.attachment.fileUrl, R.drawable.default_logo);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setCustomers(List<CustomerModel> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }
}
