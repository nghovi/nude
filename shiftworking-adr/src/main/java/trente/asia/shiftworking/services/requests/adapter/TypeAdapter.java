package nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.shiftworking.BR;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnTypeAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.databinding.ItemTypeBinding;
import nguyenhoangviet.vpcorp.welfare.adr.models.ApiObjectModel;

/**
 * Created by tien on 7/12/2017.
 */

public class TypeAdapter extends RecyclerView.Adapter<ViewHolder>{
	private FragmentManager fragmentManager;
	private List<ApiObjectModel>	types;
	private List<ApiObjectModel>	tempList = new ArrayList<>();
	private List<ApiObjectModel>	searchList;
	private ApiObjectModel selectedType;
	private OnTypeAdapterListener callback;

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	public void setCallback(OnTypeAdapterListener callback) {
		this.callback = callback;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		final ItemTypeBinding binding = (ItemTypeBinding)holder.getBinding();
		final ApiObjectModel type = searchList.get(position);
		binding.setVariable(BR.type, type);
		binding.executePendingBindings();
        if (selectedType != null && selectedType.key != null && selectedType.key.equals(type.key)) {
            binding.imgRight.setVisibility(View.VISIBLE);
        } else {
            binding.imgRight.setVisibility(View.INVISIBLE);
        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (callback != null) {
					callback.onSelectType(type);
					fragmentManager.popBackStack();
				}
			}
		});
	}

	@Override
	public int getItemCount(){
		return searchList == null ? 0 : searchList.size();
	}

	public void setDepartments(List<ApiObjectModel> types, ApiObjectModel type){
		this.types = types;
		this.searchList = types;
        this.selectedType = type;
		notifyDataSetChanged();
	}

	public void showSearchList(String text) {
		if ("".equals(text)) {
			searchList = types;
		} else {
			tempList.clear();
			for (ApiObjectModel type : types) {
				if (type.value.toLowerCase().contains(text)) {
					tempList.add(type);
				}
			}
			searchList = tempList;
		}
		notifyDataSetChanged();
	}
}
