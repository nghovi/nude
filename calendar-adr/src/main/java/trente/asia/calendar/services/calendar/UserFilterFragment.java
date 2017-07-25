package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterUserLinearLayout;
import trente.asia.calendar.commons.views.UserListLinearLayout;
import trente.asia.calendar.services.todo.model.Todo;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * UserFilterFragment
 *
 * @author VietNH
 */
public class UserFilterFragment extends AbstractClFragment{

	private FilterUserLinearLayout	mLnrFilterUser;
	private List<UserModel>			mLstUser;
	private CheckBox				mCbxAll;
	private List<UserModel>			mSelectedUsers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_filter_user, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		// loadTodoDetail();
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.todo_title), R.drawable.cl_action_save);
		mCbxAll = (CheckBox)getView().findViewById(R.id.cbx_id_all);
		mLnrFilterUser = (FilterUserLinearLayout)getView().findViewById(R.id.lnr_id_user);
		mCbxAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				clickCheckbox();
			}
		});
		this.mLnrFilterUser.addUserList(this.mLstUser, this.mSelectedUsers, mCbxAll);
	}

	private void clickCheckbox(){
		boolean isChecked = mCbxAll.isChecked();
		for(CheckableLinearLayout checkableLinearLayout : mLnrFilterUser.lstCheckable){
			checkableLinearLayout.setChecked(isChecked);
		}
	}

	public void saveActiveUserList(){
		List<UserModel> lstSelectedUser = new ArrayList<>();
		for(int index = 0; index < mLnrFilterUser.lstCheckable.size(); index++){
			CheckableLinearLayout checkableLinearLayout = mLnrFilterUser.lstCheckable.get(index);
			if(checkableLinearLayout.isChecked()){
				UserModel userModel = this.mLstUser.get(index);
				lstSelectedUser.add(userModel);
			}
		}

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, ClUtil.convertUserList2String(lstSelectedUser));
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			onClickSaveIcon();
			break;
		default:
			break;
		}
	}

	private void onClickSaveIcon(){
		saveActiveUserList();
	}

	public void setData(ArrayList<UserModel> userModels, ArrayList<UserModel> selectedUsers){
		mLstUser = userModels;
		mSelectedUsers = selectedUsers;
	}
}
