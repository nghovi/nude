package trente.asia.shiftworking.services.requests.adapter;

import java.util.List;

import com.android.databinding.library.baseAdapters.BR;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.shiftworking.BuildConfig;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.interfaces.OnUserAdapterListener;
import trente.asia.shiftworking.databinding.ItemUserAvatarBinding;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by tien on 9/25/2017.
 */

public class UserEditAdapter extends RecyclerView.Adapter<ViewHolder>{

	private List<UserModel>	users;
	private UserModel		seletedUser;
    private OnUserAdapterListener callback;
	private FragmentManager fragmentManager;

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	public void setCallback(OnUserAdapterListener callback) {
        this.callback = callback;
    }

	public void setUsers(List<UserModel> users) {
		this.users = users;
		notifyDataSetChanged();
	}

	public void setSeletedUser(UserModel seletedUser){
		this.seletedUser = seletedUser;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_avatar, parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position){
		ItemUserAvatarBinding binding = (ItemUserAvatarBinding)holder.getBinding();
		final UserModel user = users.get(position);
		binding.setVariable(BR.user, user);
		binding.executePendingBindings();
		WfPicassoHelper.loadImage(binding.userAvatar.getContext(), BuildConfig.HOST + user.avatarPath, binding.userAvatar, null);
		if(seletedUser != null && seletedUser.key.equals(user.key)){
			binding.imgRight.setVisibility(View.VISIBLE);
		}else{
			binding.imgRight.setVisibility(View.INVISIBLE);
		}
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onSelectUser(user);
                }
                if (fragmentManager != null) {
					fragmentManager.popBackStack();
				}
            }
        });
	}

	@Override
	public int getItemCount(){
		return users == null ? 0 : users.size();
	}
}
