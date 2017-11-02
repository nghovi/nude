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
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnUserAdapterListener;
import nguyenhoangviet.vpcorp.shiftworking.databinding.ItemUserBinding;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by tien on 7/12/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<ViewHolder>{

	private FragmentManager				fragmentManager;
	private List<UserModel>				users;
	private List<UserModel>				tempList	= new ArrayList<>();
	private List<UserModel>				searchList;
	private UserModel					selectedUser;
	private OnUserAdapterListener callback;

	public void setFragmentManager(FragmentManager fragmentManager){
		this.fragmentManager = fragmentManager;
	}

	public void setCallback(OnUserAdapterListener callback) {
		this.callback = callback;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		final ItemUserBinding binding = (ItemUserBinding)holder.getBinding();
		final UserModel user = searchList.get(position);
		binding.setVariable(BR.user, user);
		binding.executePendingBindings();
		if(selectedUser != null && selectedUser.key != null && selectedUser.key.equals(user.key)){
			binding.imgRight.setVisibility(View.VISIBLE);
		}else{
			binding.imgRight.setVisibility(View.INVISIBLE);
		}

		binding.getRoot().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(callback != null){
					callback.onSelectUser(user);
					fragmentManager.popBackStack();
				}
			}
		});
	}

	@Override
	public int getItemCount(){
		return searchList == null ? 0 : searchList.size();
	}

	public void setDepartments(List<UserModel> users, UserModel user){
		this.users = users;
		this.searchList = users;
		this.selectedUser = user;
		notifyDataSetChanged();
	}

	public void showSearchList(String text){
		if("".equals(text)){
			searchList = users;
		}else{
			tempList.clear();
			for(UserModel user : users){
				if(user.userName.toLowerCase().contains(text)){
					tempList.add(user);
				}
			}
			searchList = tempList;
		}
		notifyDataSetChanged();
	}
}
