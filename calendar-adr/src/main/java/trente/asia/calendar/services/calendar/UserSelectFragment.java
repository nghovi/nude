package nguyenhoangviet.vpcorp.calendar.services.calendar;

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
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import nguyenhoangviet.vpcorp.android.view.layout.CheckableLinearLayout;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.defines.ClConst;
import nguyenhoangviet.vpcorp.calendar.commons.fragments.AbstractClFragment;
import nguyenhoangviet.vpcorp.calendar.commons.utils.ClUtil;
import nguyenhoangviet.vpcorp.calendar.commons.views.FilterUserLinearLayout;
import nguyenhoangviet.vpcorp.calendar.services.calendar.model.MyGroup;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.GroupModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

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
	private List<UserModel>			selectedUsers	= new ArrayList<>();
	private boolean					isGroupAll		= true;
	private TextView				txtGroupName;

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
			appendGroupAll();
			depts = LoganSquare.parseList(response.optString("depts"), DeptModel.class);
			this.mLnrFilterUser.enableSimpleAvatar(true);
			this.mLnrFilterUser.addUserList(users, joinUsers, mCbxAll);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void appendGroupAll(){
		MyGroup groupAll = new MyGroup();
		groupAll.listGroupUser = users;
		groupAll.key = "-1";
		groupAll.groupName = getString(R.string.chiase_common_all);
		myGroups.add(0, groupAll);
		selectedMyGroups.add(groupAll);
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.choose_user), null);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		getView().findViewById(R.id.lnr_select_group).setOnClickListener(this);
		mLnrFilterUser = (FilterUserLinearLayout)getView().findViewById(R.id.lnr_id_user);
		mCbxAll = (CheckBox)getView().findViewById(R.id.cbx_id_all_select_user);
		txtGroupName = (TextView)getView().findViewById(R.id.txt_fragment_select_user);
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
		// List<UserModel> lstSelectedUser = mLnrFilterUser.lstSelectedUser;
		for(UserModel userModel : mLnrFilterUser.lstSelectedUser){
			UserModel.addUserIfNotExist(selectedUsers, userModel);
		}
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_SELECT_USER_LIST, ClUtil.convertUserList2String(selectedUsers));
		formFragment.updateJoinUsers(selectedUsers);
	}

	@Override
	protected void onClickBackBtn(){
		saveSelectUserList();
		onClickBackBtnWithoutRefresh();
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
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
		groupSelectFragment.setUserFragment(this);
		gotoFragment(groupSelectFragment);
	}

	public void setJoinUsers(List<UserModel> joinUsers){
		this.joinUsers = joinUsers;
	}

	public void addJoinUsers(List<UserModel> additionalUsers, String groupName, boolean isAll){
		List<UserModel> lstSelectedUser = new ArrayList<>();

		if(!isGroupAll){
			for(UserModel user : mLnrFilterUser.lstUser){
				if(UserModel.contain(mLnrFilterUser.lstSelectedUser, user)){
					UserModel.addUserIfNotExist(selectedUsers, user);
				}else{
					UserModel.removeUser(selectedUsers, user);
				}
			}
		}else{
			selectedUsers = new ArrayList<>();
			selectedUsers.addAll(mLnrFilterUser.lstSelectedUser);
		}
		lstSelectedUser.addAll(selectedUsers);
		this.mLnrFilterUser.addUserList(additionalUsers, lstSelectedUser, mCbxAll);
		isGroupAll = isAll;
		txtGroupName.setText(groupName);
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
