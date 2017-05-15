package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CustomerRowItemBinding;

/**
 * Created by Windows 10 Gamer on 13/05/2017.
 */

public class CustomerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private CustomerRowItemBinding binding;
    private Context context;
    private OnCustomerAdapterListener callback;

    public CustomerAdapter (OnCustomerAdapterListener listener) {
        callback = listener;
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
        binding = (CustomerRowItemBinding) holder.getBinding();
        View view = binding.getRoot();
        view.setOnClickListener((View v) -> {
                callback.onItemClick();
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public interface OnCustomerAdapterListener {
        void onItemClick();
    }
}
