package asia.trente.officeletter.commons.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tien on 8/21/2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;
    public ViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
    public ViewDataBinding getBinding(){
        return binding;
    }
}
