package trente.asia.thankscard.services.posted;

import java.util.ArrayList;
import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import trente.asia.thankscard.R;
import trente.asia.thankscard.databinding.FragmentSelectUserBinding;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.services.posted.presenter.UserAdapter;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 7/12/2017.
 */

public class SelectUserFragment extends AbstractTCFragment implements UserAdapter.OnUserAdapterListener{

	private FragmentSelectUserBinding binding;
	private UserAdapter adapter	= new UserAdapter();
	private UserModel					user;
	private OnSelectUserListener callback;
	private List<UserModel>				users;
	private List<UserModel>				searchList = new ArrayList<>();

	public void setCallback(OnSelectUserListener callback){
		this.callback = callback;
	}

	public void setDepartments(List<UserModel> users, UserModel user){
		adapter.setDepartments(users, user);
		this.user = user;
		this.users = users;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_user, container, false);
			mRootView = binding.getRoot();
			binding.listUsers.setAdapter(adapter);
			binding.listUsers.setLayoutManager(new LinearLayoutManager(getContext()));
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
					if(editable.length() > 0){
						showSearchList(editable.toString().toLowerCase());
					}else{
						if (adapter != null) {
							adapter.setDepartments(users, user);
						}
					}
				}
			});
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}
		return mRootView;
	}

	private void showSearchList(String text) {
		searchList.clear();
		for (UserModel userModel : users) {
			if (userModel.userName.toLowerCase().contains(text)) {
				searchList.add(userModel);
			}
		}
		adapter.setDepartments(searchList, user);
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
	public void onSelectUser(UserModel userModel) {
		this.user = userModel;
		if(callback != null){
			callback.onSelectUserDone(user);
			getFragmentManager().popBackStack();
		}
	}

	public interface OnSelectUserListener {
		void onSelectUserDone(UserModel userModel);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		callback = null;
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	private void log(String msg){
		Log.e("SelectDept", msg);
	}
}
