package trente.asia.messenger.services.user;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.BoardListFragment;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.RealmBoardModel;
import trente.asia.messenger.services.user.listener.OnAddUserListener;
import trente.asia.messenger.services.user.view.UserListAdapter;
import trente.asia.welfare.adr.dialog.WfProfileDialog;
import trente.asia.welfare.adr.models.UserModel;

/**
 * UserListFragment
 *
 * @author TrungND
 */
public class UserListFragment extends AbstractMsgFragment implements OnAddUserListener, View.OnClickListener {

    private ListView mLsvUser;
    private EditText mEdtSearch;
    private UserListAdapter mAdapter;
    private WfProfileDialog mDlgProfile;
    private BoardListFragment boardFragment;
    private LinearLayout lnrSelectDept;

    public void setOnAddUserSuccessListener(OnAddUserSuccessListener onAddUserSuccessListener) {
        this.onAddUserSuccessListener = onAddUserSuccessListener;
    }

    private OnAddUserSuccessListener onAddUserSuccessListener;

    public interface OnAddUserSuccessListener {
        public void onSuccess(RealmBoardModel boardModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.msg_contract_list), null);

        mLsvUser = (ListView) getView().findViewById(R.id.lsv_id_user);
        mEdtSearch = (EditText) getView().findViewById(R.id.edt_id_search);
        lnrSelectDept = (LinearLayout) getView().findViewById(R.id.lnr_select_dept);

        lnrSelectDept.setOnClickListener(this);

        mEdtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When user changed the Text
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mDlgProfile = new WfProfileDialog(activity);
        mDlgProfile.setDialogProfileDetail(50, 50);
    }

    @Override
    protected void initData() {
        loadUserList();
    }

    private void loadUserList() {
        JSONObject jsonObject = new JSONObject();
        requestLoad(MsConst.API_MESSAGE_CONTACT_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (MsConst.API_MESSAGE_CONTACT_LIST.equals(url)) {
            List<UserModel> lstUser = null;
            try {
                lstUser = LoganSquare.parseList(response.optString("userList"), UserModel.class);
                OnAvatarClickListener listener = new OnAvatarClickListener() {

                    @Override
                    public void OnAvatarClick(String userName, String avatarPath) {
                        mDlgProfile.show(BuildConfig.HOST, userName, avatarPath);
                    }
                };
                if (!CCCollectionUtil.isEmpty(lstUser)) {
                    mAdapter = new UserListAdapter(activity, lstUser, UserListFragment.this, listener);
                    mLsvUser.setAdapter(mAdapter);
                } else {
                    mAdapter = new UserListAdapter(activity, new ArrayList<UserModel>(), UserListFragment.this, listener);
                    mLsvUser.setAdapter(mAdapter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.successLoad(response, url);
        }
    }

    public void onAddUsertListener(UserModel userModel) {
        addContact(userModel);
    }

    private void addContact(UserModel userModel) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("targetUserId", userModel.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(MsConst.API_MESSAGE_CONTACT_UPDATE, jsonObject, true);
    }

    private void log(String msg) {
        Log.e("UserList", msg);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        if (MsConst.API_MESSAGE_CONTACT_UPDATE.equals(url)) {
            BoardModel boardModel = null;
            try {
                boardModel = LoganSquare.parse(response.optString("detail"), BoardModel.class);
                RealmBoardModel realmBoardModel = new RealmBoardModel(boardModel);
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(realmBoardModel);
                mRealm.commitTransaction();
                onAddUserSuccessListener.onSuccess(realmBoardModel);
                onClickBackBtn();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.successUpdate(response, url);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnr_select_dept:
                gotoFragment(new SelectDeptFragment());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLsvUser = null;
        mEdtSearch = null;
        mAdapter = null;
    }
}
