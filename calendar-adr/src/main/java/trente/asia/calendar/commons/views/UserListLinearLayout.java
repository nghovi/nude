package trente.asia.calendar.commons.views;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * UserListLinearLayout
 *
 * @author TrungND
 */

public class UserListLinearLayout extends LinearLayout{

	private Context mContext;

	public UserListLinearLayout(Context context){
		super(context);
		this.mContext = context;
	}

	public UserListLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
		this.mContext = context;
	}

	public void show(List<UserModel> lstUser){
		if(!CCCollectionUtil.isEmpty(lstUser)){
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			this.setLayoutParams(params);
			this.setGravity(Gravity.CENTER);
			this.setOrientation(HORIZONTAL);

			// image size 20 x 20:
			int maxUserNumber = this.getWidth() / 40;
			if(lstUser.size() <= maxUserNumber){
				for(UserModel userModel : lstUser){
					addUserView(userModel);
				}
			}else{
				for(int index = 0; index < maxUserNumber - 1; index++){
					UserModel userModel = lstUser.get(index);
					addUserView(userModel);
				}
			}
		}
	}

	private void addUserView(UserModel userModel){
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.view_user_item, null);
		if(!CCStringUtil.isEmpty(userModel.avatarPath)){
			ImageView imgAvatar = (ImageView)userItemView.findViewById(R.id.img_id_avatar);
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + userModel.avatarPath, imgAvatar, null);
		}
		this.addView(userItemView);
	}
}
