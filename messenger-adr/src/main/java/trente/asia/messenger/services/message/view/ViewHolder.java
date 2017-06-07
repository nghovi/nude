package trente.asia.messenger.services.message.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tien on 6/6/2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
    private ViewDataBinding binding;
    public ViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
