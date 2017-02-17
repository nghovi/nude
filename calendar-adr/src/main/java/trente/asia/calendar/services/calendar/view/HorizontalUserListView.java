package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;
import trente.asia.welfare.adr.view.WfUserChooseDialog;

/**
 * Created by viet on 2/9/2017.
 */

public class HorizontalUserListView extends LinearLayout{

	private List<UserModel>										selectedUsers		= new ArrayList<>();
	private List<UserModel>										allUsers			= new ArrayList<>();
	private WfUserChooseDialog									dlgChooseUser;
	private boolean												isViewOnly;
	private int													imgSizeDp			= 80;
	private int													imageNum			= 10;
	private TextView											txtShowMore;
	private GridLayout											gridUsers;
	private int													onDisplayingUserIdx	= 0;
	private LayoutParams										vp;
	private WfUserChooseDialog.onSelectedUsersChangedListener	onSelectedUsersChangedListener;

	public HorizontalUserListView(Context context){
		super(context);
	}

	public HorizontalUserListView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void show(List<UserModel> users, List<UserModel> calendarUsers, boolean isViewOnly, int imageSizeDp, int imgNum){
		selectedUsers = users;
		allUsers = calendarUsers;
		this.isViewOnly = isViewOnly;
		this.imgSizeDp = imageSizeDp;
		int imageSizePx = WelfareUtil.dpToPx(imageSizeDp);
		txtShowMore = (TextView)findViewById(R.id.txt_view_horizontal_user_list_more);

		if(!isViewOnly){
			setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v){
					showChooseUserDialog();
				}
			});
		}
		gridUsers = (GridLayout)this.findViewById(R.id.lnr_view_horizontal_user_list);

		int paddingPx = WelfareUtil.dpToPx(2);
		this.imageNum = gridUsers.getMeasuredWidth() / (imageSizePx + paddingPx);
		this.imageNum = imgNum < this.imageNum ? imgNum : this.imageNum;
		gridUsers.removeAllViews();
		gridUsers.setColumnCount(this.imageNum);
		vp = new LinearLayout.LayoutParams(imageSizePx, imageSizePx);
		vp.setMargins(paddingPx, paddingPx, paddingPx, paddingPx);
		for(; onDisplayingUserIdx < this.imageNum && onDisplayingUserIdx < selectedUsers.size(); onDisplayingUserIdx++){
			addUserImage();
		}
		int selectedUsersNum = selectedUsers.size();
		if(imageNum < selectedUsersNum){
			txtShowMore.setVisibility(View.VISIBLE);
			txtShowMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v){
					showMoreUsers();
				}
			});
			txtShowMore.setText("+" + (selectedUsers.size() - imageNum));
		}else if(selectedUsersNum == 0 && allUsers.size() > 0){
			txtShowMore.setVisibility(View.VISIBLE);
			txtShowMore.setText("Choose User");
		}else{
			txtShowMore.setVisibility(View.GONE);
		}
	}

	private void addUserImage(){
		SelectableRoundedImageView imgAvatar = new SelectableRoundedImageView(getContext());
		imgAvatar.setOval(true);
		imgAvatar.setLayoutParams(vp);
		UserModel userModel = selectedUsers.get(onDisplayingUserIdx);
		WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + userModel.avatarPath, imgAvatar, null);
		imgAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
		imgAvatar.setTag(userModel);
		gridUsers.addView(imgAvatar);
	}

	private void showMoreUsers(){
		txtShowMore.setVisibility(View.GONE);
		for(; onDisplayingUserIdx < selectedUsers.size(); onDisplayingUserIdx++){
			addUserImage();
		}
	}

	private void showChooseUserDialog(){
		if(dlgChooseUser != null){
			dlgChooseUser.show();
		}else{
			final Map<String, String> userMap = getUserMap();
			dlgChooseUser = new WfUserChooseDialog(getContext(), "Select " + "attendant", selectedUsers, allUsers, new WfUserChooseDialog.OnUserClickedListener() {

				@Override
				public void onClicked(String selectedKey, boolean isSelected){
					onUserClicked(selectedKey, isSelected);
				}
			});
			dlgChooseUser.setOnSelectedUsersChangedListener(this.onSelectedUsersChangedListener);

			dlgChooseUser.show();
		}
	}

	private void onUserClicked(String selectedKey, boolean isSelected){
		UserModel selectedUser = UserModel.getUserModel(selectedKey, allUsers);
		if(isSelected){
			addNewJoinedUser(selectedUser);
		}else{
			deleteJoinedUser(selectedUser);
		}
	}

	private void deleteJoinedUser(UserModel selectedUser){
		for(int i = 0; i < gridUsers.getChildCount(); i++){
			View v = gridUsers.getChildAt(i);
			if(((UserModel)v.getTag()).key.equals(selectedUser.key)){
				gridUsers.removeViewAt(i);
				onDisplayingUserIdx--;
				return;
			}
		}
	}

	private void addNewJoinedUser(UserModel selectedUser){
		if(onDisplayingUserIdx < selectedUsers.size() - 1){
			addUserImage();
		}else{
			showMoreUsers();
		}
	}

	private List<String> getSelectedUserIds(){
		return UserModel.getSelectedUserIds(selectedUsers);
	}

	public String getSelectedUserListString(){
		List<String> userIds = getSelectedUserIds();
		return TextUtils.join(",", userIds);
	}

	public Map<String, String> getUserMap(){
		Map<String, String> userMap = new HashMap<>();
		for(UserModel userModel : allUsers){
			userMap.put(userModel.key, userModel.userName);
		}
		return userMap;
	}

	public void updateAllUsers(List<UserModel> allUsers){
		this.selectedUsers = new ArrayList<>();
		this.allUsers = allUsers;
		this.dlgChooseUser = null;
		this.show(selectedUsers, allUsers, isViewOnly, imgSizeDp, imageNum);
	}

	public void setOnSelectedUsersChangedListener(WfUserChooseDialog.onSelectedUsersChangedListener onSelectedUsersChangedListener){
		this.onSelectedUsersChangedListener = onSelectedUsersChangedListener;
	}
}
