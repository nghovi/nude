package trente.asia.thankscard.services.posted.presenter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.thankscard.BR;
import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.ItemUserBinding;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 7/12/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<ViewHolder>{

	private List<UserModel>	users;
	private Context			context;
	private UserModel		selectUser;
	private OnUserAdapterListener callback;

	public void setCallback(OnUserAdapterListener callback) {
		this.callback = callback;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		context = parent.getContext();
		View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		final ItemUserBinding binding = (ItemUserBinding)holder.getBinding();
		final UserModel user = users.get(position);
		binding.setVariable(BR.user, user);
		binding.executePendingBindings();
        if (selectUser != null && selectUser.key != null && selectUser.key.equals(user.key)) {
            binding.imgRight.setVisibility(View.VISIBLE);
        } else {
            binding.imgRight.setVisibility(View.INVISIBLE);
        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				callback.onSelectUser(user);
				selectUser = user;
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getItemCount(){
		return users == null ? 0 : users.size();
	}

	public void setDepartments(List<UserModel> users, UserModel selectUser){
		this.users = users;
        this.selectUser = selectUser;
		notifyDataSetChanged();
	}

	public interface OnUserAdapterListener {
		void onSelectUser(UserModel userModel);
	}
}
