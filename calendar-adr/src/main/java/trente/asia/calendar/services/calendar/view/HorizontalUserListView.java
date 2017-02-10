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

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseListDialog;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 2/9/2017.
 */

public class HorizontalUserListView extends LinearLayout{

	private List<UserModel>		selectedUsers;
	private List<UserModel>		allUsers;
	private ChiaseListDialog	dlgChooseUser;
	private boolean				isViewOnly;
	private int					imgSizePx			= 80;
	private int					imageNum			= 10;
	private TextView			txtShowMore;
	private GridLayout			gridUsers;
	private int					onDisplayingUserIdx	= 0;
	private LayoutParams		vp;

	public HorizontalUserListView(Context context){
		super(context);
	}

	public HorizontalUserListView(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public void inflateWith(List<UserModel> users, List<UserModel> calendarUsers, boolean isViewOnly, int imageSizeDp, int imgNum){
		selectedUsers = users;
		allUsers = calendarUsers;
		this.isViewOnly = isViewOnly;
		this.imgSizePx = WelfareUtil.dpToPx(imageSizeDp);
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

		int paddingPx = WelfareUtil.dpToPx(6);
		this.imageNum = gridUsers.getMeasuredWidth() / (this.imgSizePx + paddingPx);
		this.imageNum = imgNum < this.imageNum ? imgNum : this.imageNum;
		gridUsers.removeAllViews();
		gridUsers.setColumnCount(this.imageNum);
		vp = new LinearLayout.LayoutParams(this.imgSizePx, this.imgSizePx);
		vp.setMargins(paddingPx, paddingPx, paddingPx, paddingPx);
		for(; onDisplayingUserIdx < this.imageNum && onDisplayingUserIdx < selectedUsers.size(); onDisplayingUserIdx++){
			addUserImage();
		}
		if(imageNum < selectedUsers.size()){
			txtShowMore.setVisibility(View.VISIBLE);
			txtShowMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v){
					showMoreUsers();
				}
			});
			txtShowMore.setText("+" + (selectedUsers.size() - imageNum));
		}else{
			txtShowMore.setVisibility(View.GONE);
		}
	}

	private void addUserImage(){
		SelectableRoundedImageView imgAvatar = new SelectableRoundedImageView(getContext());
		imgAvatar.setLayoutParams(vp);
		WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + selectedUsers.get(onDisplayingUserIdx).avatarPath, imgAvatar, null);
		imgAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
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
			dlgChooseUser = new ChiaseListDialog(getContext(), "Select attendant", getUserMap(), null, null);
			dlgChooseUser.show();
		}
	}

	public String getUserListString(){
		List<String> userIds = new ArrayList<>();
		for(UserModel userModel : selectedUsers){// // TODO: 2/10/2017
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
