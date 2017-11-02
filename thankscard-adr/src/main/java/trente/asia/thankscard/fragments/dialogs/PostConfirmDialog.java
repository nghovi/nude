package nguyenhoangviet.vpcorp.thankscard.fragments.dialogs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.databinding.DialogPostConfirmBinding;

/**
 * Created by tien on 7/10/2017.
 */

public class PostConfirmDialog extends TCDialog {
    private DialogPostConfirmBinding binding;
    private String receiverName;

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_post_confirm;
    }

    @Override
    public void buildDialogLayout(View rootView) {
        binding = DataBindingUtil.bind(rootView);
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPositiveListener.onClick(view);
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNegativeListener.onClick(view);
            }
        });
        binding.txtPosterName.setText(getString(R.string.tc_confirm_msg, receiverName));
    }

    public boolean isSecret() {
        return binding.checkboxSecret.isChecked();
    }
}
