package trente.asia.calendar.services.user;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * UserListAdapter.
 *
 * @author TrungND
 */
public class UserListAdapter extends ArrayAdapter<UserModel>{

	private List<UserModel>	mLstUser		= new ArrayList<>();
	private Context			mContext;
	private List<UserModel>	mLstUserDisplay	= new ArrayList<>();

	public class UserViewHolder{

		public LinearLayout	lnrAvatar;
		public ImageView	imgAvatar;
		public TextView		txtUserName;

		public UserViewHolder(View view){
			lnrAvatar = (LinearLayout)view.findViewById(R.id.lnr_avatar_container);
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtUserName = (TextView)view.findViewById(R.id.txt_id_user_name);
		}
	}

	public UserListAdapter(Context context, List<UserModel> userList){
		super(context, R.layout.item_user, userList);
		this.mContext = context;
		this.mLstUserDisplay = userList;
		for(UserModel userModel : userList){
			this.mLstUser.add(userModel);
		}
	}

	@Override
	public int getCount(){
		return this.mLstUserDisplay.size();
	}

	public View getView(int position, View convertView, ViewGroup parent){

		final UserModel userModel = this.mLstUserDisplay.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_user, null);
		UserViewHolder holder = new UserViewHolder(convertView);

		holder.txtUserName.setText(userModel.userName);

		holder.lnrAvatar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.wf_background_round_border_white));
		GradientDrawable bgShape = (GradientDrawable)holder.lnrAvatar.getBackground();
		bgShape.setColor(Color.parseColor(userModel.userColor));

		if(userModel.bitmap != null){
			holder.imgAvatar.setImageBitmap(userModel.bitmap);
		}else{
			if(!CCStringUtil.isEmpty(userModel.avatarPath)){
				WfPicassoHelper.loadImage(this.getContext(), BuildConfig.HOST + userModel.avatarPath, holder.imgAvatar, null, userModel);
			}
		}

		return convertView;
	}
}
