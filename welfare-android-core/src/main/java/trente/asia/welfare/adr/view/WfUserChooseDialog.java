package trente.asia.welfare.adr.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
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
	private OnUserClicked					onUserClickedListener;
	private List<UserModel>					userModels;

	public interface OnUserClicked{

		public void onClicked(String selectedKey, boolean isSelected);
	}

	public WfUserChooseDialog(Context context, String title, List<UserModel> selectedOnes, final List<UserModel> userModels, OnUserClicked itemClickListener){
		super(context);
		this.setContentView(R.layout.dialog_user_list_multiple_choice);
		this.mContext = context;
		this.selectedUsers = selectedOnes;
		this.userModels = userModels;
		this.onUserClickedListener = itemClickListener;

		if(!CCStringUtil.isEmpty(title)){
			TextView txtTitle = (TextView)this.findViewById(R.id.txt_title);
			txtTitle.setText(title);
		}

		listView = (ListView)this.findViewById(R.id.lst_values_select);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mAdapter = new UserListMultipleChoiceAdapter(mContext, this.userModels);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				UserModel selectedUser = (UserModel)parent.getItemAtPosition(position);
				boolean check = getCheckedStatus(selectedUser.key);
				if(!check){
					selectedUsers.add(userModels.get(position));
				}else{
					selectedUsers.remove(userModels.get(position));
				}
				listView.setItemChecked(position, !check);
				if(onUserClickedListener != null) onUserClickedListener.onClicked(selectedUser.key, !check);
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
		super.show();
	}
}
