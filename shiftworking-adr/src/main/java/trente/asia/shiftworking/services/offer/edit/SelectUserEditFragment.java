package trente.asia.shiftworking.services.offer.edit;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnDepartmentAdapterListener;
import trente.asia.shiftworking.common.interfaces.OnUserAdapterListener;
import trente.asia.shiftworking.databinding.FragmentSelectUserEditBinding;
import trente.asia.shiftworking.services.offer.adapter.UserEditAdapter;
import trente.asia.shiftworking.services.offer.filter.SelectDeptFragment;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 9/25/2017.
 */

public class SelectUserEditFragment extends AbstractSwFragment implements OnDepartmentAdapterListener{

	private FragmentSelectUserEditBinding	binding;
	private UserEditAdapter					adapter	= new UserEditAdapter();
	private DeptModel						selectedDept;
	private List<DeptModel>					depts;

	public void setSelectedUser(UserModel selectedUser){
		adapter.setSeletedUser(selectedUser);
	}

	public void setCallback(OnUserAdapterListener callback){
		adapter.setCallback(callback);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_select_user_edit, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.sw_select_user), null);
		binding = DataBindingUtil.bind(mRootView);
		adapter.setFragmentManager(getFragmentManager());
		binding.listUsers.setAdapter(adapter);
		binding.listUsers.setLayoutManager(new LinearLayoutManager(getContext()));
		binding.lnrDept.setOnClickListener(this);
	}

	@Override
	protected void initData(){
		super.initData();
        JSONObject jsonObject = new JSONObject();
        requestLoad(WfUrlConst.WF_ACC_INFO_DETAIL, jsonObject, true);
	}

    @Override
    protected void successLoad(JSONObject response, String url) {
        if (WfUrlConst.WF_ACC_INFO_DETAIL.equals(url)) {
            depts = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
            selectedDept = new DeptModel(CCConst.ALL, getString(R.string.chiase_common_all));
            selectedDept.members = new ArrayList<>();
            for (DeptModel deptModel : depts) {
                selectedDept.members.addAll(deptModel.members);
            }
            depts.add(0, selectedDept);
			binding.deptName.setText(selectedDept.deptName);
            adapter.setUsers(selectedDept.members);
        } else {
            super.successLoad(response, url);
        }
    }

    @Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View view){
		switch (view.getId()) {
			case R.id.lnr_dept:
				SelectDeptEditFragment fragment = new SelectDeptEditFragment();
				fragment.setData(this, depts, selectedDept);
				gotoFragment(fragment);
				break;
		}
	}

	@Override
	public void onSelectDepartment(DeptModel deptModel) {
		selectedDept = deptModel;
		adapter.setUsers(deptModel.members);
        binding.deptName.setText(selectedDept.deptName);
	}
}
