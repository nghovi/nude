package trente.asia.thankscard.services.posted.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.ChoosePictureDialogBinding;
import trente.asia.thankscard.databinding.DialogCannotUsePhotoBinding;
import trente.asia.thankscard.fragments.dialogs.TCDialog;

/**
 * Created by tien on 7/19/2017.
 */

public class CannotUsePhotoDialog extends TCDialog {

    @Override
    public int getDialogLayoutId() {
        return R.layout.dialog_cannot_use_photo;
    }

    @Override
    public void buildDialogLayout(View rootView) {
        DialogCannotUsePhotoBinding binding = DataBindingUtil.bind(rootView);
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
