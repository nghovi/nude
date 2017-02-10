package trente.asia.calendar.services.calendar.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/9/2017.
 */

public class HorizontalUserListView extends LinearLayout{

	private List<UserModel>		selectedUsers;
	private List<UserModel>		allUsers;
	private ChiaseListDialog	dlgChooseUser;
	private boolean				isViewOnly;

	public HorizontalUserListView(Context context){
		super(context);
	}

	public HorizontalUserListView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void inflateWith(List<UserModel> users, List<UserModel> calendarUsers, boolean isViewOnly){
		selectedUsers = users;
		allUsers = calendarUsers;
		this.isViewOnly = isViewOnly;
		if(!isViewOnly){
			setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v){
					showChooseUserDialog();
				}
			});
		}
		LinearLayout lnrUserList = (LinearLayout)findViewById(R.id.lnr_view_horizontal_user_list);
		lnrUserList.removeAllViews();
		LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(100, 100);
		for(UserModel userModel : selectedUsers){
			ImageView imgAvatar = new ImageView(getContext());
			imgAvatar.setPadding(20, 20, 20, 20);
			imgAvatar.setLayoutParams(vp);
			WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + userModel.avatarPath, imgAvatar, null);
			lnrUserList.addView(imgAvatar);
		}
	}

	private void showChooseUserDialog(){
		if(dlgChooseUser != null){
			dlgChooseUser.show();
		}else{
			dlgChooseUser = new ChiaseListDialog(getContext(), "Select user", getUserMap(), null, null);
			dlgChooseUser.show();
		}
	}

	public String getUserListString(){
		List<String> userIds = new ArrayList<>();
		for(UserModel userModel : selectedUsers){
			userIds.add(userModel.key);
		}
		return TextUtils.join(",", userIds);
	}

	public Map<String, String> getUserMap(){
		Map<String, String> userMap = new HashMap<>();
		for(UserModel userModel : allUsers){
			userMap.put(userModel.key, userModel.userName);
		}
		return userMap;
	}
}
