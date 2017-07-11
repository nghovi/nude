package trente.asia.thankscard.fragments.dialogs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.DialogPostConfirmBinding;

/**
 * Created by tien on 7/10/2017.
 */

public class PostConfirmDialog extends TCDialog {
    private DialogPostConfirmBinding binding;
    private String posterName;

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    int getDialogLayoutId() {
        return R.layout.dialog_post_confirm;
    }

    @Override
    void buildDialogLayout(View rootView) {
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
        binding.txtPosterName.setText(getString(R.string.tc_confirm_name, posterName));
    }

    public boolean isSecret() {
        return binding.checkboxSecret.isChecked();
    }
}
