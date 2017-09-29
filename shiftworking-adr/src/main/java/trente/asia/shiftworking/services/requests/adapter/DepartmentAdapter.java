package trente.asia.shiftworking.services.requests.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.shiftworking.BR;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.interfaces.OnDepartmentAdapterListener;
import trente.asia.shiftworking.databinding.ItemDepartmentBinding;
import trente.asia.welfare.adr.models.DeptModel;

/**
 * Created by tien on 7/12/2017.
 */

public class DepartmentAdapter extends RecyclerView.Adapter<ViewHolder>{
	private FragmentManager fragmentManager;
	private List<DeptModel>	departments;
	private List<DeptModel>	tempList = new ArrayList<>();
	private List<DeptModel>	searchList;
	private Context			context;
	private DeptModel		selectDept;
	private OnDepartmentAdapterListener callback;

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	public void setCallback(OnDepartmentAdapterListener callback) {
		this.callback = callback;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.item_department, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		final ItemDepartmentBinding binding = (ItemDepartmentBinding)holder.getBinding();
		final DeptModel department = searchList.get(position);
		binding.setVariable(BR.dept, department);
		binding.executePendingBindings();
        if (selectDept != null && selectDept.key != null && selectDept.key.equals(department.key)) {
            binding.imgRight.setVisibility(View.VISIBLE);
        } else {
            binding.imgRight.setVisibility(View.INVISIBLE);
        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (callback != null) {
					callback.onSelectDepartment(department);
					fragmentManager.popBackStack();
				}
			}
		});
	}

	@Override
	public int getItemCount(){
		return searchList == null ? 0 : searchList.size();
	}

	public void setDepartments(List<DeptModel> departments, DeptModel selectDept){
		this.departments = departments;
		this.searchList = departments;
        this.selectDept = selectDept;
		notifyDataSetChanged();
	}

	public void showSearchList(String text) {
		if ("".equals(text)) {
			searchList = departments;
		} else {
			tempList.clear();
			for (DeptModel deptModel : departments) {
				if (deptModel.deptName.toLowerCase().contains(text)) {
					tempList.add(deptModel);
				}
			}
			searchList = tempList;
		}
		notifyDataSetChanged();
	}
}
