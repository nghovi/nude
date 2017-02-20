package trente.asia.calendar.commons.dialogs;

import java.util.List;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.adapter.FilterUserListAdapter;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.welfare.adr.models.UserModel;

/**
 * QkChiaseDialog
 *
 * @author TrungND
 */
public class ClFilterUserListDialog extends ChiaseDialog{

	private Context					mContext;

	private ListView				mLsvUser;
	private FilterUserListAdapter	mAdapter;

	private List<UserModel>			mLstUser;
	private UserListLinearLayout	mLnrUserList;

	private ImageView				mImgDone;

	public ClFilterUserListDialog(Context context, UserListLinearLayout lnrUserList){
		super(context);
		this.setContentView(R.layout.dialog_common_filter_user);
		this.mContext = context;
		this.mLnrUserList = lnrUserList;

		mImgDone = (ImageView)this.findViewById(R.id.img_id_done);
		mLsvUser = (ListView)this.findViewById(R.id.lsv_id_user);

		mImgDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				ClFilterUserListDialog.this.dismiss();
				saveActiveUserList();
			}
		});
	}

	public void updateUserList(List<UserModel> lstUser){
		this.mLstUser = lstUser;
		mAdapter = new FilterUserListAdapter(mContext, mLstUser);

		mLsvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			}
		});
		mLsvUser.setAdapter(mAdapter);
	}

	private void saveActiveUserList(){
		this.mAdapter.getSelectedUser().clear();
		SparseBooleanArray array = mLsvUser.getCheckedItemPositions();
		for(int index = 0; index < array.size(); index++){
			boolean isChecked = array.valueAt(index);
			if(isChecked){
				UserModel userModel = (UserModel)this.mLsvUser.getItemAtPosition(index);
				this.mAdapter.getSelectedUser().add(userModel);
			}
		}

		if(mLnrUserList != null){
			mLnrUserList.show(this.mAdapter.getSelectedUser(), (int)this.mContext.getResources().getDimension(R.dimen.margin_30dp));
		}
	}

	public List<UserModel> getSelectedUser(){
		return this.mAdapter.getSelectedUser();
	}
}
