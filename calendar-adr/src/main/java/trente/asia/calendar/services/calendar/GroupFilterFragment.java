package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import asia.chiase.core.util.CCCollectionUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterDeptLinearLayout;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.GroupModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * fragment_filter_user.xml
 *
 * @author VietNH
 */
public class GroupFilterFragment extends AbstractClFragment{

	private FilterDeptLinearLayout	mLnrFilterDept;
	private CheckBox				mCbxAll;
	private List<UserModel>			mSelectedUsers;
	private List<GroupModel>		groups;
	private List<DeptModel>			depts;
	private List<GroupModel>		myGroups;
	private List<UserModel>			users;
	private List<UserModel>			selectedUsers;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_filter_group, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.select_group), R.drawable.cl_action_save);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		mCbxAll = (CheckBox)getView().findViewById(R.id.cbx_id_all);
		mLnrFilterDept = (FilterDeptLinearLayout)getView().findViewById(R.id.lnr_id_filter_dept);
		mCbxAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				clickCheckbox();
			}
		});

		List<GroupModel> selectedMyGroups = getSelectedGroups(selectedUsers, myGroups);
		List<GroupModel> selectedGroups = getSelectedGroups(selectedUsers, groups);
		List<DeptModel> selectedDepts = getSelectedDepts(selectedUsers);
		this.mLnrFilterDept.fillInData(myGroups, selectedMyGroups, groups, selectedGroups, depts, selectedDepts, mCbxAll);

	}

	private List<DeptModel> getSelectedDepts(List<UserModel> selectedUsers){
		List<DeptModel> result = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(selectedUsers)){
			for(DeptModel deptModel : depts){
				boolean isSelected = true;
				for(UserModel userModel : deptModel.members){
					if(!FilterDeptLinearLayout.checkSelectedUser(userModel, selectedUsers)){
						isSelected = false;
						break;
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
				for(UserModel userModel : groupModel.listUsers){
					if(!FilterDeptLinearLayout.checkSelectedUser(userModel, selectedUsers)){
						isSelected = false;
						break;
					}
				}
				if(isSelected){
					result.add(groupModel);
				}
			}
		}

		return result;
	}

	private void clickCheckbox(){
		boolean isChecked = mCbxAll.isChecked();
		for(CheckableLinearLayout checkableLinearLayout : mLnrFilterDept.lstCheckable){
			checkableLinearLayout.setChecked(isChecked);
		}
	}

	public void saveActiveUserList(){
		List<UserModel> lstSelectedUser = new ArrayList<>();
		for(int index = 0; index < mLnrFilterDept.lstCheckable.size(); index++){
			CheckableLinearLayout checkableLinearLayout = mLnrFilterDept.lstCheckable.get(index);
			if(checkableLinearLayout.isChecked()){
				List<UserModel> userModels = (List<UserModel>)checkableLinearLayout.getTag();
				for(UserModel user : userModels){
					if(!FilterDeptLinearLayout.checkSelectedUser(user, lstSelectedUser)){
						lstSelectedUser.add(user);
					}
				}
			}
		}

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, ClUtil.convertUserList2String(lstSelectedUser));
		((ChiaseActivity)activity).isInitData = true;
		getFragmentManager().popBackStack();
		getFragmentManager().popBackStack();

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
		case R.id.lnr_select_group:
			gotoGroupFilterFragment();
		default:
			break;
		}
	}

	private void gotoGroupFilterFragment(){

	}

	private void onClickSaveIcon(){
		saveActiveUserList();
	}

	public void setGroups(List<GroupModel> groups){
		this.groups = groups;
	}

	public void setDepts(List<DeptModel> depts){
		this.depts = depts;
	}

	public void setMyGroups(List<GroupModel> myGroups){
		this.myGroups = myGroups;
	}

	public void setUsers(List<UserModel> users){
		this.users = users;
	}

	public void setSelectedUsers(List<UserModel> selectedUsers){
		this.selectedUsers = selectedUsers;
	}
}
