package nguyenhoangviet.vpcorp.thankscard.services.posted.dialog;

import android.databinding.DataBindingUtil;
import android.view.View;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.databinding.DialogCannotUseAnimationsBinding;
import nguyenhoangviet.vpcorp.thankscard.databinding.DialogCannotUsePhotoBinding;
import nguyenhoangviet.vpcorp.thankscard.fragments.dialogs.TCDialog;

/**
 * Created by tien on 7/19/2017.
 */

public class CannotUseAnimationDialog extends TCDialog {

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_cannot_use_animations;
    }

    @Override
    public void buildDialogLayout(View rootView) {
        DialogCannotUseAnimationsBinding binding = DataBindingUtil.bind(rootView);
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
