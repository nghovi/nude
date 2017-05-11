package trente.asia.addresscard.services.card;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Windows 10 Gamer on 07/05/2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;
    public ViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
    public ViewDataBinding getBinding() {
        return binding;
    }
}
