package asia.trente.officeletter.commons.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.databinding.DialogConfirmPasswordBinding;

/**
 * Created by tien on 8/18/2017.
 */

public abstract class AbstractListFragment extends AbstractOLFragment {
    public void showConfirmPassDialog(String passwordHint, final String letterType, final int key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_password, null);
        final DialogConfirmPasswordBinding binding = DataBindingUtil.bind(view);
        binding.passwordHint.setText(getString(R.string.ol_password_hint, passwordHint));
        builder.setView(view);
        final Dialog dialog = builder.create();
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmPassword(binding.edtPassword.getText().toString(), letterType, key);
            }
        });

        dialog.show();
    }

    private void confirmPassword(String password, String letterType, int key) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", String.valueOf(key));
            jsonObject.put("letterType", letterType);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(OLConst.API_OL_PASSWORD_CONFIRM, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (OLConst.API_OL_PASSWORD_CONFIRM.equals(url)) {
            successPassword();
        } else {
            super.successUpdate(response, url);
        }
    }

    public void successPassword() {}
}
