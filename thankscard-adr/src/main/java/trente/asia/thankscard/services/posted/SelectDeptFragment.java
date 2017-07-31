package trente.asia.thankscard.services.posted;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.FragmentSelectDeptBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.posted.presenter.DepartmentAdapter;
import trente.asia.welfare.adr.models.DeptModel;

/**
 * Created by tien on 7/12/2017.
 */

public class SelectDeptFragment extends AbstractTCFragment implements DepartmentAdapter.OnDepartmentAdapterListener{

	private FragmentSelectDeptBinding	binding;
	private DepartmentAdapter			adapter	= new DepartmentAdapter();
	private DeptModel					department;
	private OnSelectDeptListener		callback;
	private List<DeptModel>				departments;
	private List<DeptModel>				searchList = new ArrayList<>();

	public void setCallback(OnSelectDeptListener callback){
		this.callback = callback;
	}

	public void setDepartments(List<DeptModel> departments, DeptModel selectDept){
		adapter.setDepartments(departments, selectDept);
		this.department = selectDept;
		this.departments = departments;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_dept, container, false);
			mRootView = binding.getRoot();
			binding.listDepartments.setAdapter(adapter);
			binding.listDepartments.setLayoutManager(new LinearLayoutManager(getContext()));
			adapter.setCallback(this);
			binding.btnDone.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){

				}
			});

			binding.btnCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					getFragmentManager().popBackStack();
				}
			});

			binding.edtSearch.addTextChangedListener(new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

				}

				@Override
				public void afterTextChanged(Editable editable){
					if(editable.length() > 2){
						showSearchList(editable.toString().toLowerCase());
					}else{
						if (adapter != null) {
							adapter.setDepartments(departments, department);
						}
					}
				}
			});
		}
		return mRootView;
	}

	private void showSearchList(String text) {
		searchList.clear();
		for (DeptModel deptModel : departments) {
			if (deptModel.deptName.toLowerCase().contains(text)) {
				searchList.add(deptModel);
			}
		}
		adapter.setDepartments(searchList, department);
	}

	@Override
	public int getFragmentLayoutId(){
		return 0;
	}

	@Override
	public boolean hasBackBtn(){
		return false;
	}

	@Override
	public boolean hasSettingBtn(){
		return false;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public int getTitle(){
		return 0;
	}

	@Override
	public void buildBodyLayout(){

	}

	@Override
	public void onSelectDepartment(DeptModel deptModel){
		this.department = deptModel;
		if(callback != null){
			callback.onSelectDeptDone(department);
			getFragmentManager().popBackStack();
		}
	}

	public interface OnSelectDeptListener{

		void onSelectDeptDone(DeptModel deptModel);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		callback = null;
	}

	private void log(String msg){
		Log.e("SelectDept", msg);
	}
}
