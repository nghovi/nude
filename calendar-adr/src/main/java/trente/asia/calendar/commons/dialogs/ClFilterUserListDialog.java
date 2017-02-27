package trente.asia.calendar.commons.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import trente.asia.android.view.ChiaseDialog;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterUserLinearLayout;
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
	private FilterUserLinearLayout	mLnrFilterUser;

	private List<UserModel>			mLstUser;
	private UserListLinearLayout	mLnrUserList;
	private CheckBox				mCbxAll;

	public ClFilterUserListDialog(Context context, UserListLinearLayout lnrUserList){
		super(context);
		this.setContentView(R.layout.dialog_common_filter_user);
		this.mContext = context;
		this.mLnrUserList = lnrUserList;

		mCbxAll = (CheckBox)this.findViewById(R.id.cbx_id_all);
		mLnrFilterUser = (FilterUserLinearLayout)this.findViewById(R.id.lnr_id_user);
		mCbxAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				clickCheckbox();
			}
		});
	}

	private void clickCheckbox(){
		boolean isChecked = mCbxAll.isChecked();
		for(CheckableLinearLayout checkableLinearLayout : mLnrFilterUser.lstCheckable){
			checkableLinearLayout.setChecked(isChecked);
		}
	}

	public void updateUserList(List<UserModel> lstUser){
		this.mLstUser = lstUser;
	}

	public void saveActiveUserList(){
		List<UserModel> lstSelectedUser = new ArrayList<>();
		for(int index = 0; index < mLnrFilterUser.lstCheckable.size(); index++){
			CheckableLinearLayout checkableLinearLayout = mLnrFilterUser.lstCheckable.get(index);
			if(checkableLinearLayout.isChecked()){
				UserModel userModel = (UserModel)this.mLstUser.get(index);
				lstSelectedUser.add(userModel);
			}
		}

		if(mLnrUserList != null){
			mLnrUserList.show(lstSelectedUser, (int)this.mContext.getResources().getDimension(R.dimen.margin_30dp));
		}

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(mContext);
		prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, ClUtil.convertUserList2String(lstSelectedUser));
	}

    @Override
    public void show(){
        this.mLnrFilterUser.addUserList(this.mLstUser, mLnrUserList.getLstUser(), mCbxAll);
        super.show();
    }
}
