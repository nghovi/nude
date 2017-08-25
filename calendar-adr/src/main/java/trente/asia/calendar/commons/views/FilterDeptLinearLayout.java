package trente.asia.calendar.commons.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.listener.CsOnCheckedChangeListener;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.R;
import trente.asia.calendar.services.calendar.model.MyGroup;
import trente.asia.calendar.services.calendar.model.RoomModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.GroupModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * FilterUserLinearLayout
 *
 * @author TrungND
 */

public class FilterDeptLinearLayout extends LinearLayout{

	public List<CheckableLinearLayout>	lstCheckable;
	private List<String>				names			= new ArrayList<>();
	private OnDeptSelectedListener		onDeptSelectedListenter;
	private boolean						isSimpleAvatar	= false;

	public void enableSimpleAvatar(boolean enable){
		isSimpleAvatar = enable;
	}

	public interface OnDeptSelectedListener{

		public void onSelectDept(List<UserModel> userModels, Object object, boolean isChecked);
	}

	public void search(String s){
		int i = 0;
		for(CheckableLinearLayout checkableLinearLayout : lstCheckable){
			String name = names.get(i);
			if(!name.contains(s)){
				checkableLinearLayout.setVisibility(View.GONE);
			}else{
				checkableLinearLayout.setVisibility(View.VISIBLE);
			}
			i++;
		}
	}

	public void setOnDeptSelectedListenter(OnDeptSelectedListener onDeptSelectedListenter){
		this.onDeptSelectedListenter = onDeptSelectedListenter;
	}

	private class ViewHolder{

		private final ImageView			imgAvatar;
		private final LinearLayout		lnrAvatar;
		public TextView					txtName;
		public CheckableLinearLayout	lnrItem;
		public ImageView				imgCheck;

		public ViewHolder(View view){
			lnrAvatar = (LinearLayout)view.findViewById(R.id.lnr_avatar_container);
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtName = (TextView)view.findViewById(R.id.txt_id_user_name);
			lnrItem = (CheckableLinearLayout)view.findViewById(R.id.lnr_id_item);
			imgCheck = (ImageView)view.findViewById(R.id.img_id_check);
		}
	}

	public FilterDeptLinearLayout(Context context){
		super(context);
	}

	public FilterDeptLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public FilterDeptLinearLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	public void fillInData(List<MyGroup> mygroups, List<MyGroup> selectedMyGroups, List<GroupModel> groupModels, List<GroupModel> selectedGroups, List<DeptModel> deptModels, List<DeptModel> selectedDepts, final CheckBox cbxAll){

		lstCheckable = new ArrayList<>();
		this.removeAllViews();

		if(!CCCollectionUtil.isEmpty(mygroups)){
			for(MyGroup myGroup : mygroups){
				addItem(myGroup.groupName, cbxAll, checkSelectedMyGroup(myGroup, selectedMyGroups), myGroup.listGroupUser, null, myGroup);
			}
		}

		if(!CCCollectionUtil.isEmpty(deptModels)){
			for(DeptModel deptModel : deptModels){
				addItem(deptModel.deptName, cbxAll, checkSelectedDept(deptModel, selectedDepts), deptModel.members, null, deptModel);
			}
		}

		if(!CCCollectionUtil.isEmpty(groupModels)){
			for(GroupModel group : groupModels){
				addItem(group.groupName, cbxAll, checkSelectedGroup(group, selectedGroups), group.listUsers, null, group);
			}
		}

		judgeCheckAll(cbxAll);
	}

	public void fillInRoomData(List<RoomModel> rooms, List<RoomModel> selectedRooms, final CheckBox cbxAll){

		lstCheckable = new ArrayList<>();
		this.removeAllViews();

		if(!CCCollectionUtil.isEmpty(rooms)){
			for(RoomModel roomModel : rooms){
				addItem(roomModel.roomName, cbxAll, checkSelectedRoom(roomModel, selectedRooms), null, roomModel.color, roomModel);
			}
		}

		judgeCheckAll(cbxAll);
	}

	private boolean checkSelectedRoom(RoomModel roomModel, List<RoomModel> selectedRooms){
		for(RoomModel room : selectedRooms){
			if(room.key.equals(roomModel.key)){
				return true;
			}
		}
		return false;
	}

	public static boolean checkSelectedDept(DeptModel deptModel, List<DeptModel> deptModels){
		for(DeptModel dept : deptModels){
			if(dept.key.equals(deptModel.key)){
				return true;
			}
		}
		return false;
	}

	public static boolean checkSelectedGroup(GroupModel group, List<GroupModel> groups){
		for(GroupModel groupModel : groups){
			if(group.key.equals(groupModel.key)){
				return true;
			}
		}
		return false;
	}

	public static boolean checkSelectedMyGroup(MyGroup group, List<MyGroup> groups){
		for(MyGroup groupModel : groups){
			if(group.key.equals(groupModel.key)){
				return true;
			}
		}
		return false;
	}

	public static boolean checkSelectedUser(UserModel userModel, List<UserModel> userModels){
		for(UserModel user : userModels){
			if(user.key.equals(userModel.key)){
				return true;
			}
		}
		return false;
	}

	// WelfareUtil.containUserInList(this.lstSelectedUser, userModel)
	private void addItem(String name, final CheckBox cbxAll, boolean alreadySelected, final List<UserModel> userModels, String colorCode, final Object object){
		LayoutInflater mInflater = (LayoutInflater)this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View userView = mInflater.inflate(R.layout.adapter_dialog_user_item, null);

		final ViewHolder holder = new ViewHolder(userView);
		holder.imgAvatar.setVisibility(View.GONE);
		holder.txtName.setText(name);

		if(!CCStringUtil.isEmpty(colorCode) && isSimpleAvatar == false){
			holder.lnrAvatar.setVisibility(View.VISIBLE);
			holder.lnrAvatar.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.wf_background_round_border_white));
			GradientDrawable bgShape = (GradientDrawable)holder.lnrAvatar.getBackground();
			bgShape.setColor(Color.parseColor(colorCode));
		}else if(CCStringUtil.isEmpty(colorCode)){
			holder.lnrAvatar.setVisibility(View.GONE);
		}

		if(alreadySelected){
			holder.lnrItem.setChecked(true);
			holder.imgCheck.setVisibility(View.VISIBLE);
		}else{
			holder.lnrItem.setChecked(false);
			holder.imgCheck.setVisibility(View.INVISIBLE);
		}

		holder.lnrItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v){
				boolean isChecked = holder.lnrItem.isChecked();
				holder.lnrItem.setChecked(!isChecked);
				if(onDeptSelectedListenter != null){
					onDeptSelectedListenter.onSelectDept(userModels, object, !isChecked);
				}
			}
		});

		holder.lnrItem.setOnCheckedChangeListener(new CsOnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(Checkable view, boolean isChecked){
				if(isChecked){
					holder.imgCheck.setVisibility(View.VISIBLE);
					judgeCheckAll(cbxAll);
				}else{
					holder.imgCheck.setVisibility(View.INVISIBLE);
					if(cbxAll != null){
						cbxAll.setChecked(false);
					}
				}
			}
		});
		holder.lnrItem.setTag(userModels);
		this.lstCheckable.add(holder.lnrItem);
		this.names.add(name);
		this.addView(userView);
	}

	public void updateChecked(boolean isChecked, List<UserModel> additionalUserModels){
		List<UserModel> selectingUsers = getSelectingUsers(additionalUserModels);
		if(isChecked){
			for(CheckableLinearLayout checkableLinearLayout : this.lstCheckable){
				List<UserModel> users = (List<UserModel>)checkableLinearLayout.getTag();
				boolean match = true;
				for(UserModel userModel : users){
					if(!UserModel.contain(selectingUsers, userModel)){
						match = false;
						break;
					}
				}
				if(match){
					checkableLinearLayout.setChecked(true);
				}
			}
		}else{
			// for(CheckableLinearLayout checkableLinearLayout : this.lstCheckable){
			// List<UserModel> users = (List<UserModel>)checkableLinearLayout.getTag();
			// boolean match = false;
			// for(UserModel userModel : selectingUsers){
			// if(UserModel.contain(users, userModel)){
			// match = true;
			// break;
			// }
			// }
			// if(match){
			// checkableLinearLayout.setChecked(false);
			// }
			// }
		}
	}

	private List<UserModel> getSelectingUsers(List<UserModel> additionalUserModels){
		List<UserModel> results = new ArrayList<>();
		results.addAll(additionalUserModels);
		for(CheckableLinearLayout checkableLinearLayout : this.lstCheckable){
			if(checkableLinearLayout.isChecked()){
				List<UserModel> users = (List<UserModel>)checkableLinearLayout.getTag();
				for(UserModel userModel : users){
					if(!UserModel.contain(results, userModel)){
						results.add(userModel);
					}
				}
			}
		}
		return results;
	}

	private void judgeCheckAll(CheckBox checkBox){
		boolean isChecked = true;
		for(CheckableLinearLayout checkableLinearLayout : lstCheckable){
			if(!checkableLinearLayout.isChecked()){
				isChecked = false;
			}
		}
		if(checkBox != null){
			checkBox.setChecked(isChecked);
		}
	}
}
