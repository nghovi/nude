package nguyenhoangviet.vpcorp.shiftworking.services.requests.filter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnDepartmentAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.databinding.FragmentSelectDeptBinding;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter.DepartmentAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * Created by tien on 9/21/2017.
 */

public class SelectDeptFragment extends AbstractSwFragment {
    private DepartmentAdapter adapter = new DepartmentAdapter();

    public void setData(OnDepartmentAdapterListener callback, List<DeptModel> depts, DeptModel selectedDept) {
        adapter.setDepartments(depts, selectedDept);
        adapter.setCallback(callback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_select_dept, container, false);
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
        FragmentSelectDeptBinding binding = DataBindingUtil.bind(mRootView);
        binding.btnCancel.setOnClickListener(this);

        binding.listDepartments.setAdapter(adapter);
        binding.listDepartments.setLayoutManager(new LinearLayoutManager(getContext()));
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
