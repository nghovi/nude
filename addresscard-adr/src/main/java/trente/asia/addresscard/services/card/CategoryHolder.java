package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tien on 5/8/2017.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {
    ViewDataBinding binding;
    public CategoryHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
    public ViewDataBinding getBinding() {
        return binding;
    }
}
