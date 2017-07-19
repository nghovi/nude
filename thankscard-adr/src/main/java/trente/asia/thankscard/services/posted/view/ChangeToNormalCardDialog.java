package trente.asia.thankscard.services.posted.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.ChangeToNormalCardBinding;
import trente.asia.thankscard.databinding.ChoosePictureDialogBinding;
import trente.asia.thankscard.fragments.dialogs.TCDialog;

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
