package trente.asia.calendar.services.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.defines.ClConst;
import trente.asia.calendar.commons.fragments.AbstractClFragment;
import trente.asia.calendar.commons.utils.ClUtil;
import trente.asia.calendar.commons.views.FilterDeptLinearLayout;
import trente.asia.calendar.services.calendar.model.RoomModel;
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
public class RoomFilterFragment extends AbstractClFragment{

	private FilterDeptLinearLayout	mLnrFilterDept;
	private List<UserModel>			mSelectedUsers;
	private List<GroupModel>		groups;
	private List<DeptModel>			depts;
	private List<GroupModel>		myGroups;
	private List<UserModel>			users;
	private List<UserModel>			selectedUsers;
	private List<RoomModel>			rooms;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_filter_room, container, false);
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
			rooms = LoganSquare.parseList(response.optString("rooms"), RoomModel.class);
			String selectedRoomIds = prefAccUtil.get(ClConst.PREF_ACTIVE_ROOM);
			if("0".equals(selectedRoomIds)){
				prefAccUtil.set(ClConst.PREF_ACTIVE_ROOM, ClUtil.convertRoomList2String(rooms));
			}
			List<RoomModel> selectedRooms = getSelectedRooms(rooms);

			this.mLnrFilterDept.fillInRoomData(getListRoomSubBegin(rooms), selectedRooms, null);

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public List<RoomModel> getListRoomSubBegin(List<RoomModel> rooms){
		List<RoomModel> results = new ArrayList<>();
		if(rooms.size()<=1)return results;
		for(int temp = 1;temp<rooms.size();temp++){
			results.add(rooms.get(temp));
		}
		return results;
	}
	@Override
	public void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.select_facility), R.drawable.cl_action_save);
		getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
		mLnrFilterDept = (FilterDeptLinearLayout)getView().findViewById(R.id.lnr_id_filter_dept);

		((EditText)getView().findViewById(R.id.edt_filter_search_room)).addTextChangedListener(new TextWatcher() {

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

	}

	private List<RoomModel> getSelectedRooms(List<RoomModel> rooms){

		String targetRoomData = prefAccUtil.get(ClConst.PREF_ACTIVE_ROOM);

		List<RoomModel> result = new ArrayList<>();
		if(CCStringUtil.isEmpty(targetRoomData) || CCCollectionUtil.isEmpty(rooms)){
			return result;
		}

		List<String> targetRoomListId = Arrays.asList(targetRoomData.split(","));
		for(RoomModel room : rooms){
			if(targetRoomListId.contains(room.key)){
				result.add(room);
			}
		}
		return result;
	}

	public void saveFilteredRoomIds(){
		List<RoomModel> selectedRooms = new ArrayList<>();
		for(int index = 0; index < mLnrFilterDept.lstCheckable.size(); index++){
			CheckableLinearLayout checkableLinearLayout = mLnrFilterDept.lstCheckable.get(index);
			if(checkableLinearLayout.isChecked()){
				selectedRooms.add(rooms.get(index));
			}
		}

		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(activity);
		prefAccUtil.set(ClConst.PREF_FILTER_TYPE, ClConst.PREF_FILTER_TYPE_ROOM);
		prefAccUtil.set(ClConst.PREF_ACTIVE_ROOM, ClUtil.convertRoomList2String(selectedRooms));
		((WelfareActivity)activity).dataMap.put(ClConst.ACTION_SCHEDULE_UPDATE, CCConst.YES);
		onClickBackBtn();
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

	}

	private void onClickSaveIcon(){
		saveFilteredRoomIds();
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
