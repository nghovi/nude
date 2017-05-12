package trente.asia.calendar.commons.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * UserListLinearLayout
 *
 * @author TrungND
 */

public class UserListLinearLayout extends LinearLayout{

	private static final int	OWNER_USER_DISTANCE_DP	= 10;
	private static final int	AVATAR_IMG_MARGIN_PX	= WelfareUtil.dpToPx(2);
	private Context				mContext;
	private boolean				isGravityLeft			= false;

	private List<UserModel>		lstUser;

	// private UserModel ownerUser;

	public void setGravityLeft(boolean gravityLeft){
		isGravityLeft = gravityLeft;
	}

	public UserListLinearLayout(Context context){
		super(context);
		this.mContext = context;
		this.setGravity(Gravity.CENTER);
		this.setOrientation(HORIZONTAL);
	}

	public UserListLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
		this.setGravity(Gravity.CENTER);
		this.setOrientation(HORIZONTAL);
	}

	public void show(List<UserModel> lstUser, int imageSizePx){
		// lstUser = sortByOwner(lstUser);
		if(isGravityLeft){
			this.setGravity(Gravity.LEFT);
		}
		this.removeAllViews();
		this.lstUser = lstUser;
		if(!CCCollectionUtil.isEmpty(lstUser)){
			// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			// this.setLayoutParams(params);

			// image size 20 x 20:
			int maxUserNumber = (int)((this.getWidth() - imageSizePx) / (imageSizePx + AVATAR_IMG_MARGIN_PX));
			if(lstUser.size() <= maxUserNumber){
				for(UserModel userModel : lstUser){
					addUserView(userModel, imageSizePx);
				}
			}else{
				for(int index = 0; index < maxUserNumber - 1; index++){
					UserModel userModel = lstUser.get(index);
					addUserView(userModel, imageSizePx);
				}

				// add text
				TextView txtRemainingUserCount = new TextView(mContext);
				// int textDimension = (int)mContext.getResources()
				// .getDimension(R.dimen.margin_40dp);
				LayoutParams textLayout = new LayoutParams(imageSizePx * 2, imageSizePx);
				txtRemainingUserCount.setLayoutParams(textLayout);
				int textSize = (int)(getResources().getDimension(R.dimen.margin_20dp) / getResources().getDisplayMetrics().density);
				txtRemainingUserCount.setTextSize(textSize);
				txtRemainingUserCount.setText("+" + (lstUser.size() - maxUserNumber + 1));
				txtRemainingUserCount.setGravity(Gravity.CENTER);
				this.addView(txtRemainingUserCount);
			}
		}

	}

	public void showAll(List<UserModel> lstUser, int imageSizePx){
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setOrientation(HORIZONTAL);
		this.removeAllViews();
		this.lstUser = lstUser;
		if(!CCCollectionUtil.isEmpty(lstUser)){
			for(UserModel userModel : lstUser){
				addUserView(userModel, imageSizePx);
			}
		}
	}

	// private List<UserModel> sortByOwner(List<UserModel> lstUser){
	// if(ownerUser == null){
	// return lstUser;
	// }
	// List<UserModel> result = new ArrayList<>();
	// for(UserModel userModel : lstUser){
	// if(userModel.key.equals(ownerUser.key)){
	// result.add(0, userModel);
	// }else{
	// result.add(userModel);
	// }
	// }
	// return result;
	// }

	private void addUserView(UserModel userModel, int imageSizePx){
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.view_user_item, null);
		if(!CCStringUtil.isEmpty(userModel.avatarPath)){
			ImageView imgAvatar = (ImageView)userItemView.findViewById(R.id.img_id_avatar);
			LayoutParams layoutParams = new LayoutParams(imageSizePx, imageSizePx);
			layoutParams.setMargins(0, 0, AVATAR_IMG_MARGIN_PX, 0);
			imgAvatar.setLayoutParams(layoutParams);
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + userModel.avatarPath, imgAvatar, null);
		}
		this.addView(userItemView);
	}

	public String formatUserList(){
		StringBuilder builder = new StringBuilder();
		if(!CCCollectionUtil.isEmpty(lstUser)){
			for(UserModel userModel : lstUser){
				builder.append(userModel.key + ",");
			}
		}
		return builder.toString();
	}

	public List<UserModel> getLstUser(){
		return lstUser;
	}
}
