package nguyenhoangviet.vpcorp.thankscard.services.posted.dialog;

import android.databinding.DataBindingUtil;
import android.view.View;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.databinding.ChangeToNormalCardBinding;
import nguyenhoangviet.vpcorp.thankscard.databinding.ChoosePictureDialogBinding;
import nguyenhoangviet.vpcorp.thankscard.fragments.dialogs.TCDialog;

/**
 * Created by tien on 7/19/2017.
 */

public class ChangeToNormalCardDialog extends TCDialog {

    @Override
    public int getDialogLayoutId() {
        return R.layout.change_to_normal_card;
    }

    @Override
    public void buildDialogLayout(View rootView) {
        ChangeToNormalCardBinding binding = DataBindingUtil.bind(rootView);
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
    }
}
