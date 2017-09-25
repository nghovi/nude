package trente.asia.shiftworking.services.offer.filter;

import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnDepartmentAdapterListener;
import trente.asia.shiftworking.common.interfaces.OnUserAdapterListener;
import trente.asia.shiftworking.databinding.FragmentSelectDeptBinding;
import trente.asia.shiftworking.databinding.FragmentSelectUserBinding;
import trente.asia.shiftworking.services.offer.adapter.DepartmentAdapter;
import trente.asia.shiftworking.services.offer.adapter.UserAdapter;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 9/21/2017.
 */

public class SelectUserFragment extends AbstractSwFragment {
    private UserAdapter adapter = new UserAdapter();

    public void setData(OnUserAdapterListener callback, List<UserModel> users, UserModel selectedUser) {
        adapter.setDepartments(users, selectedUser);
        adapter.setCallback(callback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_select_user, container, false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        FragmentSelectUserBinding binding = DataBindingUtil.bind(mRootView);
        binding.btnCancel.setOnClickListener(this);

        binding.listUsers.setAdapter(adapter);
        binding.listUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setFragmentManager(getFragmentManager());

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.showSearchList(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
