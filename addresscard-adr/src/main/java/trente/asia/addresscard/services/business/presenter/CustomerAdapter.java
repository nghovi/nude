package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.BuildConfig;
import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CustomerRowItemBinding;
import trente.asia.addresscard.services.business.model.BusinessCardModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by Windows 10 Gamer on 13/05/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private     CustomerRowItemBinding          binding;
    private     Context                         context;
    private     OnCustomerAdapterListener       callback;
    private     List<CustomerModel>             customers;

    public CustomerAdapter (OnCustomerAdapterListener listener) {
        callback = listener;
        customers = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.customer_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel customer = customers.get(position);
        binding = (CustomerRowItemBinding) holder.getBinding();
        binding.setVariable(BR.customer, customer);
        binding.executePendingBindings();

        WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, binding.leftIcon,
                customer.attachment.fileUrl, R.drawable.default_logo);
        loadCardImage(binding.card1, 0, customer.cards);
        loadCardImage(binding.card2, 1, customer.cards);

        View view = binding.getRoot();
        view.setOnClickListener((View v) -> {
                callback.onItemClick(customer.key);
        });
    }

    private void loadCardImage(ImageView imageView, int position, List<BusinessCardModel> cards) {
        if (position < cards.size()) {
            BusinessCardModel card = cards.get(position);
            Picasso.with(context).load(BuildConfig.HOST + card.attachment.fileUrl)
                    .fit().into(imageView);
        } else {
            Picasso.with(context).load("url").into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void updateList(List<CustomerModel> customers) {
        this.customers = customers;
        notifyDataSetChanged();
    }

    public interface OnCustomerAdapterListener {
        void onItemClick(int customerId);
    }
}
