package nguyenhoangviet.vpcorp.thankscard.services.posted.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tien on 7/12/2017.
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
