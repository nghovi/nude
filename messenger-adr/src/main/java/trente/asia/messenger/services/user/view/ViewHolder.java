package nguyenhoangviet.vpcorp.messenger.services.user.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by tien on 8/16/2017.
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
