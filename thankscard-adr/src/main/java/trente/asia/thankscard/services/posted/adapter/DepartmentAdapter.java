package nguyenhoangviet.vpcorp.thankscard.services.posted.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nguyenhoangviet.vpcorp.thankscard.BR;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.databinding.ItemDepartmentBinding;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * Created by tien on 7/12/2017.
 */

public class DepartmentAdapter extends RecyclerView.Adapter<ViewHolder>{

	private List<DeptModel>	departments;
	private Context			context;
	private DeptModel		selectDept;
	private OnDepartmentAdapterListener callback;

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
		final DeptModel department = departments.get(position);
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
				callback.onSelectDepartment(department);
				selectDept = department;
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getItemCount(){
		return departments == null ? 0 : departments.size();
	}

	public void setDepartments(List<DeptModel> departments, DeptModel selectDept){
		this.departments = departments;
        this.selectDept = selectDept;
		notifyDataSetChanged();
	}

	public interface OnDepartmentAdapterListener {
		void onSelectDepartment(DeptModel deptModel);
	}
}
