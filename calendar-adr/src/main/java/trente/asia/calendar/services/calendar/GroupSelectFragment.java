package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.views.FilterDeptLinearLayout;
import trente.asia.calendar.services.calendar.model.MyGroup;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.GroupModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * fragment_filter_user.xml
 *
 * @author VietNH
 */
public class GroupSelectFragment extends AbstractClFragment implements FilterDeptLinearLayout.OnDeptSelectedListener{

	private FilterDeptLinearLayout	mLnrFilterDept;
	private List<GroupModel>		groups;
	private List<DeptModel>			depts;
	private List<MyGroup>			myGroups;
	// private List<UserModel> selectedUsers;
	private List<UserModel>			users;
	private UserSelectFragment		userFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_select_group, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.select_group), null);
		mLnrFilterDept = (FilterDeptLinearLayout)getView().findViewById(R.id.lnr_id_filter_dept);
		// List<GroupModel> selectedMyGroups = getSelectedGroups(selectedUsers, myGroups);
		// List<GroupModel> selectedGroups = getSelectedGroups(selectedUsers, groups);
		// List<DeptModel> selectedDepts = getSelectedDepts(selectedUsers);
		this.mLnrFilterDept.enableSimpleAvatar(true);
		this.mLnrFilterDept.fillInData(myGroups, userFragment.selectedMyGroups, groups, userFragment.selectedGroups, depts, userFragment.selectedDepts, null);
		this.mLnrFilterDept.setOnDeptSelectedListenter(this);
	}

	private List<DeptModel> getSelectedDepts(List<UserModel> selectedUsers){
		List<DeptModel> result = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(selectedUsers)){
			for(DeptModel deptModel : depts){
				boolean isSelected = true;
				if(CCCollectionUtil.isEmpty(deptModel.members)){
					isSelected = false;
				}else{
					for(UserModel userModel : deptModel.members){
						if(!FilterDeptLinearLayout.checkSelectedUser(userModel, selectedUsers)){
							isSelected = false;
							break;
						}
					}
				}
				if(isSelected){
					result.add(deptModel);
				}
			}
		}

		return result;
	}

	private List<GroupModel> getSelectedGroups(List<UserModel> selectedUsers, List<GroupModel> groupModels){

		List<GroupModel> result = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(selectedUsers)){
			for(GroupModel groupModel : groupModels){
				boolean isSelected = true;
				if(CCCollectionUtil.isEmpty(groupModel.listUsers)){
					isSelected = false;
				}else{
					for(UserModel userModel : groupModel.listUsers){
						if(!FilterDeptLinearLayout.checkSelectedUser(userModel, selectedUsers)){
							isSelected = false;
							break;
						}
					}
				}
				if(isSelected){
					result.add(groupModel);
				}
			}
		}

		return result;
	}

	public void saveActiveUserList(List<UserModel> userModels, Object object){
		if(!CCCollectionUtil.isEmpty(userModels)){
			// List<UserModel> lstSelectedUser = new ArrayList<>();
			// lstSelectedUser.addAll(userModels);

			// PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
			// List<UserModel> selectedUsers = ClUtil.getTargetUserList(users, prefAccUtil.get(ClConst.PREF_SELECT_USER_LIST));
			//
			// for(UserModel userModel : lstSelectedUser){
			// if(!UserModel.contain(selectedUsers, userModel)){
			// selectedUsers.add(userModel);
			// }
			// }

			userFragment.resetSelectedGroups();
			if(object.getClass().toString().equals(MyGroup.class.toString())){
				userFragment.selectedMyGroups.add((MyGroup)object);
			}else if(object.getClass().toString().equals(GroupModel.class.toString())){
				userFragment.selectedGroups.add((GroupModel)object);
			}else if(object.getClass().toString().equals(DeptModel.class.toString())){
				userFragment.selectedDepts.add((DeptModel)object);
			}

			// prefAccUtil.set(ClConst.PREF_SELECT_USER_LIST, ClUtil.convertUserList2String(selectedUsers));
			userFragment.addJoinUsers(userModels);
		}
		onClickBackBtnWithoutRefresh();
	}

	@Override
	protected void onClickBackBtn(){
		onClickBackBtnWithoutRefresh();
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_footer_monthly;
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_select_group:
			gotoGroupFilterFragment();
		default:
			break;
		}
	}

	private void gotoGroupFilterFragment(){

	}

	public void setGroups(List<GroupModel> groups){
		this.groups = groups;
	}

	public void setDepts(List<DeptModel> depts){
		this.depts = depts;
	}

	public void setMyGroups(List<MyGroup> myGroups){
		this.myGroups = myGroups;
	}

	public void setUsers(List<UserModel> users){
		this.users = users;
	}

	// public void setSelectedUsers(List<UserModel> selectedUsers){
	// this.selectedUsers = selectedUsers;
	// }

	@Override
	public void onSelectDept(List<UserModel> userModels, Object object){
		saveActiveUserList(userModels, object);
	}

	public void setUserFragment(UserSelectFragment userFragment){
		this.userFragment = userFragment;
	}

	// public void setSelectedGroups(List<GroupModel> selectedGroups, List<DeptModel> selectedDepts, List<GroupModel> selectedMyGroups) {
	// this.selectedGroups = selectedGroups;
	// this.selectedMyGroup = selectedMyGroups;
	// this.selectedDepts = selectedDepts;
	// }
}
