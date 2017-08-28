package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bluelinelabs.logansquare.LoganSquare;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import asia.chiase.core.define.CCConst;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterUserLinearLayout;
import trente.asia.calendar.services.calendar.model.MyGroup;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.GroupModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

/**
 * UserFilterFragment
 *
 * @author VietNH
 */
public class UserFilterFragment extends AbstractClFragment{

	private FilterUserLinearLayout	mLnrFilterUser;
	private List<UserModel>			users;
	private List<GroupModel>		groups;
	private List<DeptModel>			depts;
	private List<MyGroup>			myGroups;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_filter_user, container, false);
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
			List<UserModel> selectedUsers = ClUtil.getTargetUserList(users, prefAccUtil.get(ClConst.PREF_ACTIVE_USER_LIST));
			this.mLnrFilterUser.addUserList(users, selectedUsers, null);
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
		((EditText)getView().findViewById(R.id.edt_filter_search)).addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){

			}

			@Override
			public void afterTextChanged(Editable s){
				mLnrFilterUser.search(s.toString());
			}
		});
	}

	// // TODO: 7/26/17 save selected user into pref is not good esp when there are a lot of user
	public void saveActiveUserList(){
		List<UserModel> lstSelectedUser = setSelectedUserList();
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_ACTIVE_USER_LIST, ClUtil.convertUserList2String(lstSelectedUser));
		prefAccUtil.set(ClConst.PREF_FILTER_TYPE, ClConst.PREF_FILTER_TYPE_USER);
		((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_UPDATE, CCConst.YES);
		onClickBackBtn();
	}

	private List<UserModel> setSelectedUserList(){
		List<UserModel> lstSelectedUser = new ArrayList<>();
		for(int index = 0; index < mLnrFilterUser.lstCheckable.size(); index++){
			CheckableLinearLayout checkableLinearLayout = mLnrFilterUser.lstCheckable.get(index);
			UserModel userModel = users.get(index);
			if(checkableLinearLayout.isChecked()){
				lstSelectedUser.add(userModel);
			}else{
				UserModel.removeUser(lstSelectedUser, userModel);
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
			gotoGroupFilterFragment();
		default:
			break;
		}
	}

	private void gotoGroupFilterFragment(){
		GroupFilterFragment groupFilterFragment = new GroupFilterFragment();
		groupFilterFragment.setGroups(groups);
		groupFilterFragment.setDepts(depts);
		groupFilterFragment.setMyGroups(myGroups);
		groupFilterFragment.setUsers(users);
		List<UserModel> lstSelectedUser = setSelectedUserList();
		groupFilterFragment.setSelectedUsers(lstSelectedUser);
		gotoFragment(groupFilterFragment);
	}

	private void onClickSaveIcon(){
		saveActiveUserList();
	}

}
