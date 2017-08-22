package trente.asia.calendar.services.calendar;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.activity.ChiaseActivity;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterDeptLinearLayout;
import trente.asia.calendar.services.calendar.model.MyGroup;
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
	private List<MyGroup>			myGroups;
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

		((EditText)getView().findViewById(R.id.edt_filter_search_group)).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}

			@Override
			public void afterTextChanged(Editable s){
				mLnrFilterDept.search(s.toString());
			}
		});

		List<MyGroup> selectedMyGroups = getSelectedMyGroups(selectedUsers, myGroups);
		List<GroupModel> selectedGroups = getSelectedGroups(selectedUsers, groups);
		List<DeptModel> selectedDepts = getSelectedDepts(selectedUsers);
		this.mLnrFilterDept.fillInData(myGroups, selectedMyGroups, groups, selectedGroups, depts, selectedDepts, mCbxAll);
		mLnrFilterDept.setOnDeptSelectedListenter(new FilterDeptLinearLayout.OnDeptSelectedListener() {
			@Override
			public void onSelectDept(List<UserModel> userModels, Object object) {

			}
		});

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

	//// TODO: 8/21/17 duplicate code
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

	private List<MyGroup> getSelectedMyGroups(List<UserModel> selectedUsers, List<MyGroup> MyGroups){

		List<MyGroup> result = new ArrayList<>();
		if(!CCCollectionUtil.isEmpty(selectedUsers)){
			for(MyGroup MyGroup : MyGroups){
				boolean isSelected = true;
				if(CCCollectionUtil.isEmpty(MyGroup.listUsers)){
					isSelected = false;
				}else{
					for(UserModel userModel : MyGroup.listUsers){
						if(!FilterDeptLinearLayout.checkSelectedUser(userModel, selectedUsers)){
							isSelected = false;
							break;
						}
					}
				}
				if(isSelected){
					result.add(MyGroup);
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
				if(!CCCollectionUtil.isEmpty(userModels)){
					for(UserModel user : userModels){
						if(!FilterDeptLinearLayout.checkSelectedUser(user, lstSelectedUser)){
							lstSelectedUser.add(user);
						}
					}
				}
			}
		}

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_FILTER_TYPE, ClConst.PREF_FILTER_TYPE_USER);
		prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, ClUtil.convertUserList2String(lstSelectedUser));
		((ChiaseActivity)activity).isInitData = true;
		((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_UPDATE, CCConst.YES);
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

	public void setMyGroups(List<MyGroup> myGroups){
		this.myGroups = myGroups;
	}

	public void setUsers(List<UserModel> users){
		this.users = users;
	}

	public void setSelectedUsers(List<UserModel> selectedUsers){
		this.selectedUsers = selectedUsers;
	}
}
