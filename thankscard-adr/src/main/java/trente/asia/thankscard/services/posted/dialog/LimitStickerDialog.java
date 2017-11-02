package nguyenhoangviet.vpcorp.thankscard.services.posted.dialog;

import android.databinding.DataBindingUtil;
import android.view.View;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.databinding.ChoosePictureDialogBinding;
import nguyenhoangviet.vpcorp.thankscard.databinding.LimitStickerDialogBinding;
import nguyenhoangviet.vpcorp.thankscard.fragments.dialogs.TCDialog;

/**
 * Created by tien on 7/19/2017.
 */

public class LimitStickerDialog extends TCDialog {

    @Override
    public int getDialogLayoutId() {
        return R.layout.limit_sticker_dialog;
    }

    @Override
    public void buildDialogLayout(View rootView) {
        LimitStickerDialogBinding binding = DataBindingUtil.bind(rootView);
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPositiveListener.onClick(view);
            }
        });
    }
}
