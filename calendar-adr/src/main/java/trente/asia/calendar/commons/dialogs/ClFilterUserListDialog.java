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
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

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
	}

	public void updateUserList(List<UserModel> lstUser){
		this.mLstUser = lstUser;
		mAdapter = new FilterUserListAdapter(mContext, mLstUser, mLnrUserList.getLstUser());

		mLsvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			}
		});
		mLsvUser.setAdapter(mAdapter);
	}

	public void saveActiveUserList(){
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

        PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(mContext);
        prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, ClUtil.convertUserList2String(this.mAdapter.getSelectedUser()));
	}

	public List<UserModel> getSelectedUser(){
		return this.mAdapter.getSelectedUser();
	}
}
