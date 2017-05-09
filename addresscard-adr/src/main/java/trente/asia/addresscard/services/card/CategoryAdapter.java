package trente.asia.addresscard.services.card;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import trente.asia.addresscard.BR;
import trente.asia.addresscard.R;

/**
 * Created by tien on 5/8/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {
    ArrayList<Category> categories;
    CategoryAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category category = categories.get(position);
        holder.getBinding().setVariable(BR.category, category);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
