package trente.asia.messenger.services.message.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * CheckUserListAdapter.
 *
 * @author TrungND
 */
public class CheckUserListAdapter extends ArrayAdapter<UserModel>{

	private List<UserModel>	mLstUser	= new ArrayList<>();
	private Context			mContext;

	public class UserViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtName;

		public UserViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtName = (TextView)view.findViewById(R.id.txt_id_name);
		}
	}

	public CheckUserListAdapter(Context context, List<UserModel> userList){
		super(context, R.layout.item_check_user_list, userList);
		this.mContext = context;
		this.mLstUser = userList;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		final UserModel model = this.mLstUser.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_check_user_list, null);
		UserViewHolder holder = new UserViewHolder(convertView);

		holder.txtName.setText(model.userName);
		if(!CCStringUtil.isEmpty(model.avatarPath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.avatarPath, holder.imgAvatar, null);
		}

		return convertView;
	}

	@Override
	public boolean isEnabled(int position){
		return false;
	}
}
