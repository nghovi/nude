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
import trente.asia.addresscard.databinding.CategoryItemBinding;
import trente.asia.addresscard.services.business.model.CategoryModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 5/8/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private OnCategoryAdapterListener               callback;
    private List<CategoryModel>                     categories;
    private Context                                 context;

    public CategoryAdapter(OnCategoryAdapterListener listener) {
        this.callback = listener;
        this.categories = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryModel category = categories.get(position);
        CategoryItemBinding binding = (CategoryItemBinding) holder.getBinding();
        binding.setVariable(BR.category, category);
        binding.executePendingBindings();
        loadImage(binding.customer0, 0, category.customers);
        loadImage(binding.customer1, 1, category.customers);
        loadImage(binding.customer2, 2, category.customers);
        loadImage(binding.customer3, 3, category.customers);

        binding.getRoot().setOnClickListener((View v) -> {
                callback.onItemClick(category.key);
        });
    }

    private void loadImage(ImageView imageView, int position, List<CustomerModel> customers) {
        if (position < customers.size()) {
            WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, imageView,
                            customers.get(position).attachment.fileUrl, R.drawable.default_logo);
        } else {
            Picasso.with(context).load("url").into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnCategoryAdapterListener {
        void onItemClick(int categoryId);
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }
}
