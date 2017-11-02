package nguyenhoangviet.vpcorp.thankscard.services.posted.dialog;

import android.databinding.DataBindingUtil;
import android.view.View;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.databinding.ChoosePictureDialogBinding;
import nguyenhoangviet.vpcorp.thankscard.databinding.DialogCannotUseStickersBinding;
import nguyenhoangviet.vpcorp.thankscard.fragments.dialogs.TCDialog;

/**
 * Created by tien on 7/19/2017.
 */

public class CannotUseStickersDialog extends TCDialog {

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_cannot_use_stickers;
    }

    @Override
    public void buildDialogLayout(View rootView) {
        DialogCannotUseStickersBinding binding = DataBindingUtil.bind(rootView);
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
