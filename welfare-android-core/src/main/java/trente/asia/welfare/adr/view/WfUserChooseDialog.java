package trente.asia.welfare.adr.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.view.adapter.UserListMultipleChoiceAdapter;

/**
 * WfUserChooseDialog
 *
 * @author vietnh
 */
public class WfUserChooseDialog extends ChiaseDialog{

	private Context							mContext;

	private ListView						listView;
	private UserListMultipleChoiceAdapter	mAdapter;
	private List<UserModel>					selectedUsers;
	private OnUserClickedListener			onUserClickedListenerListener;
	private List<UserModel>					userModels;

	private onSelectedUsersChangedListener	onSelectedUsersChangedListener;

	public void setOnSelectedUsersChangedListener(WfUserChooseDialog.onSelectedUsersChangedListener onSelectedUsersChangedListener){
		this.onSelectedUsersChangedListener = onSelectedUsersChangedListener;
	}

	public interface OnUserClickedListener{

		public void onClicked(String selectedKey, boolean isSelected);
	}

	public interface onSelectedUsersChangedListener{

		public void onChange();
	}

	public WfUserChooseDialog(Context context, String title, List<UserModel> selectedOnes, final List<UserModel> userModels, OnUserClickedListener itemClickListener){
		super(context);
		this.setContentView(R.layout.dialog_user_list_multiple_choice);
		this.mContext = context;
		this.selectedUsers = selectedOnes;
		this.userModels = userModels;
		this.onUserClickedListenerListener = itemClickListener;

		if(!CCStringUtil.isEmpty(title)){
			TextView txtTitle = (TextView)this.findViewById(R.id.txt_title);
			txtTitle.setText(title);
		}

		listView = (ListView)this.findViewById(R.id.lst_values_select);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mAdapter = new UserListMultipleChoiceAdapter(mContext, this.userModels);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
				UserModel selectedUser = (UserModel)parent.getItemAtPosition(position);
				final boolean check = getCheckedStatus(selectedUser.key);
				if(!check){
					view.findViewById(R.id.img_checked).setVisibility(View.VISIBLE);
					selectedUsers.add(userModels.get(position));
				}else{
					view.findViewById(R.id.img_checked).setVisibility(View.GONE);
					selectedUsers.remove(userModels.get(position));
				}
				if(onUserClickedListenerListener != null) onUserClickedListenerListener.onClicked(selectedUser.key, !check);
			}
		});
		listView.setAdapter(mAdapter);
	}

	private boolean getCheckedStatus(String userId){
		for(UserModel userModel : selectedUsers){
			if(userModel.key.equals(userId)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void show(){
		for(UserModel userModel : selectedUsers){
			int position = mAdapter.findPosition4Code(userModel.key, userModels);
			listView.setItemChecked(position, true);
		}

		final Set<String> userIdsBefore = new HashSet<String>(UserModel.getSelectedUserIds(selectedUsers));
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog){

				Set<String> userIdsAfter = new HashSet<String>(UserModel.getSelectedUserIds(selectedUsers));

				if(onSelectedUsersChangedListener != null && !userIdsBefore.equals(userIdsAfter)){
					onSelectedUsersChangedListener.onChange();
				}
			}
		});
		super.show();
	}
}
