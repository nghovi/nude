package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterUserLinearLayout;
import trente.asia.calendar.services.calendar.model.MyGroup;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.GroupModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * UserFilterFragment
 *
 * @author VietNH
 */
public class UserSelectFragment extends AbstractClFragment{

	private FilterUserLinearLayout	mLnrFilterUser;
	private List<UserModel>			users;
	private List<GroupModel>		groups;
	private List<DeptModel>			depts;
	private List<MyGroup>			myGroups;
	private List<UserModel>			joinUsers;
	private CheckBox				mCbxAll;
	public List<DeptModel>			selectedDepts;
	public List<GroupModel>			selectedGroups;
	public List<MyGroup>			selectedMyGroups;
	public ScheduleFormFragment		formFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_select_user, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initData(){
		loadFilterData();
	}

	private void loadFilterData(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(ClConst.API_FILTER, jsonObject, true);
	}

	protected void successLoad(JSONObject response, String url){
		super.successLoad(response, url);
		try{
			users = LoganSquare.parseList(response.optString("users"), UserModel.class);
			groups = LoganSquare.parseList(response.optString("groups"), GroupModel.class);
			myGroups = LoganSquare.parseList(response.optString("myGroups"), MyGroup.class);
			depts = LoganSquare.parseList(response.optString("depts"), DeptModel.class);
			// List<UserModel> selectedUsers = ClUtil.getTargetUserList(users, prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST));
			this.mLnrFilterUser.enableSimpleAvatar(true);
			this.mLnrFilterUser.addUserList(users, joinUsers, mCbxAll);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.select_user), R.drawable.cl_action_save);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		getView().findViewById(R.id.lnr_select_group).setOnClickListener(this);
		mLnrFilterUser = (FilterUserLinearLayout)getView().findViewById(R.id.lnr_id_user);
		mCbxAll = (CheckBox)getView().findViewById(R.id.cbx_id_all);
		mCbxAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				clickCheckbox();
			}
		});
		resetSelectedGroups();
	}

	private void clickCheckbox(){
		boolean isChecked = mCbxAll.isChecked();
		for(CheckableLinearLayout checkableLinearLayout : mLnrFilterUser.lstCheckable){
			checkableLinearLayout.setChecked(isChecked);
		}
	}

	// // TODO: 7/26/17 save selected user into pref is not good esp when there are a lot of user
	public void saveSelectUserList(){
		List<UserModel> lstSelectedUser = getSelectedUserList();
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_SELECT_USER_LIST, ClUtil.convertUserList2String(lstSelectedUser));
		formFragment.updateJoinUsers(lstSelectedUser);
		onClickBackBtnWithoutRefresh();
	}

	@Override
	protected void onClickBackBtn(){
		onClickBackBtnWithoutRefresh();
	}

	private List<UserModel> getSelectedUserList(){
		List<UserModel> lstSelectedUser = new ArrayList<>();
		for(int index = 0; index < mLnrFilterUser.lstCheckable.size(); index++){
			CheckableLinearLayout checkableLinearLayout = mLnrFilterUser.lstCheckable.get(index);
			if(checkableLinearLayout.isChecked()){
				UserModel userModel = users.get(index);
				lstSelectedUser.add(userModel);
			}
		}
		return lstSelectedUser;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_right_icon:
			onClickSaveIcon();
			break;
		case R.id.lnr_select_group:
			gotoGroupSelectFragment();
		default:
			break;
		}
	}

	private void gotoGroupSelectFragment(){
		GroupSelectFragment groupSelectFragment = new GroupSelectFragment();
		groupSelectFragment.setGroups(groups);
		groupSelectFragment.setDepts(depts);
		groupSelectFragment.setMyGroups(myGroups);
		groupSelectFragment.setUsers(users);
		// groupSelectFragment.setSelectedGroups(selectedGroups, selectedDepts, selectedMyGroups);
		groupSelectFragment.setUserFragment(this);
		List<UserModel> lstSelectedUser = getSelectedUserList();
		// groupSelectFragment.setSelectedUsers(lstSelectedUser);
		gotoFragment(groupSelectFragment);
	}

	private void onClickSaveIcon(){
		saveSelectUserList();
	}

	public void setJoinUsers(List<UserModel> joinUsers){
		this.joinUsers = joinUsers;
	}

	public void addJoinUsers(List<UserModel> additionalUsers){
		List<UserModel> lstSelectedUser = getSelectedUserList();
		if(!CCCollectionUtil.isEmpty(additionalUsers)){
			for(UserModel userModel : additionalUsers){
				if(!UserModel.contain(lstSelectedUser, userModel)){
					lstSelectedUser.add(userModel);
				}
			}
			this.mLnrFilterUser.addUserList(users, lstSelectedUser, mCbxAll);
		}
	}

	public void resetSelectedGroups(){
		selectedDepts = new ArrayList<>();
		selectedGroups = new ArrayList<>();
		selectedMyGroups = new ArrayList<>();
	}

	public void setFormFragment(ScheduleFormFragment scheduleFormFragment){
		this.formFragment = scheduleFormFragment;
	}
}
