package asia.trente.officeletter.commons.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.defines.OLConst;
import asia.trente.officeletter.commons.utils.OLUtils;
import asia.trente.officeletter.databinding.DialogConfirmPasswordBinding;

/**
 * Created by tien on 8/18/2017.
 */

public abstract class AbstractListFragment extends AbstractOLFragment {
    public static final String MSG_PASSWORD_NOT_MATCH_EN = "Password does not match";
    public static final String MSG_PASSWORD_NOT_MATCH_JP = "パスワードが一致しません";
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

    @Override
    protected void commonNotSuccess(JSONObject response) {
        String msg = response.optString("messages");
        log(msg);
        if (msg != null && (msg.contains(MSG_PASSWORD_NOT_MATCH_EN) || msg.contains(MSG_PASSWORD_NOT_MATCH_JP))) {
            super.commonNotSuccess(response);
        } else {
            OLUtils.showAlertDialog(getContext(), R.string.ol_message_file_not_found);
            initData();
        }
    }

    public void successPassword() {}

    private void log(String msg) {
        Log.e("AbstractList", msg);
    }
}
