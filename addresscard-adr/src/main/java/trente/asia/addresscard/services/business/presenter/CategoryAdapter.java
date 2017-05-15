package trente.asia.addresscard.services.business.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.addresscard.R;
import trente.asia.addresscard.databinding.CategoryItemBinding;

/**
 * Created by tien on 5/8/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private OnCategoryAdapterListener callback;
    private CategoryItemBinding binding;
    private Context context;

    public CategoryAdapter(OnCategoryAdapterListener listener) {
        this.callback = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(
                R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        binding = (CategoryItemBinding) holder.getBinding();
        binding.getRoot().setOnClickListener((View v) -> {
                callback.onItemClick();
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public interface OnCategoryAdapterListener {
        void onItemClick();
    }
}
