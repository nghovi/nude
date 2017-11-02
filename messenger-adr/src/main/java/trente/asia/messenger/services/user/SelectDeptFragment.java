package nguyenhoangviet.vpcorp.messenger.services.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.databinding.FragmentSelectDeptBinding;
import nguyenhoangviet.vpcorp.messenger.fragment.AbstractMsgFragment;
import nguyenhoangviet.vpcorp.messenger.services.user.view.DeptAdapter;

/**
 * Created by tien on 8/16/2017.
 */

public class SelectDeptFragment extends AbstractMsgFragment {
    private FragmentSelectDeptBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_dept, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.msg_select_department), null);
        binding.rlvDept.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rlvDept.setAdapter(new DeptAdapter());
    }
}
