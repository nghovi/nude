package trente.asia.calendar.services.calendar;

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
 * GroupSelectFragment
 *
 * @author VietNH
 */
public class GroupSelectFragment extends AbstractClFragment implements FilterDeptLinearLayout.OnDeptSelectedListener{

	private FilterDeptLinearLayout	mLnrFilterDept;
	private List<GroupModel>		groups;
	private List<DeptModel>			depts;
	private List<MyGroup>			myGroups;
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
		initHeader(R.drawable.wf_back_white, getString(R.string.choose_group), null);
		mLnrFilterDept = (FilterDeptLinearLayout)getView().findViewById(R.id.lnr_id_filter_dept);
		this.mLnrFilterDept.enableSimpleAvatar(true);
		this.mLnrFilterDept.fillInData(myGroups, userFragment.selectedMyGroups, groups, userFragment.selectedGroups, depts, userFragment.selectedDepts, null);
		this.mLnrFilterDept.setOnDeptSelectedListenter(this);
	}

	public void saveActiveUserList(List<UserModel> userModels, Object object){
		if(!CCCollectionUtil.isEmpty(userModels)){
			boolean isAll = false;
			String groupName = "";
			userFragment.resetSelectedGroups();
			if(object.getClass().toString().equals(MyGroup.class.toString())){
				MyGroup myGroup = (MyGroup)object;
				userFragment.selectedMyGroups.add(myGroup);
				if(myGroup.key.equals("-1")){
					isAll = true;
				}
				groupName = myGroup.groupName;
			}else if(object.getClass().toString().equals(GroupModel.class.toString())){
				GroupModel groupModel = (GroupModel)object;
				userFragment.selectedGroups.add(groupModel);
				groupName = groupModel.groupName;
			}else if(object.getClass().toString().equals(DeptModel.class.toString())){
				DeptModel deptModel = (DeptModel)object;
				userFragment.selectedDepts.add(deptModel);
				groupName = deptModel.deptName;
			}

			userFragment.addJoinUsers(userModels, groupName, isAll);
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

	@Override
	public void onSelectDept(List<UserModel> userModels, Object object, boolean isChecked){
		saveActiveUserList(userModels, object);
	}

	public void setUserFragment(UserSelectFragment userFragment){
		this.userFragment = userFragment;
	}

}
