package nguyenhoangviet.vpcorp.shiftworking.services.requests.edit;

import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.fragments.AbstractSwFragment;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnDepartmentAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.databinding.FragmentSelectDeptEditBinding;
import nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter.DepartmentAdapter;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * Created by tien on 9/21/2017.
 */

public class SelectDeptEditFragment extends AbstractSwFragment {
    private DepartmentAdapter adapter = new DepartmentAdapter();

    public void setData(OnDepartmentAdapterListener callback, List<DeptModel> depts, DeptModel selectedDept) {
        adapter.setDepartments(depts, selectedDept);
        adapter.setCallback(callback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_select_dept_edit, container, false);
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
        initHeader(R.drawable.wf_back_white, getString(R.string.sw_select_dept), null);
        FragmentSelectDeptEditBinding binding = DataBindingUtil.bind(mRootView);

        binding.listDepartments.setAdapter(adapter);
        binding.listDepartments.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setFragmentManager(getFragmentManager());
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
