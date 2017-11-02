package nguyenhoangviet.vpcorp.messenger.services.message.view;

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
import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmUserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * CheckUserListAdapter.
 *
 * @author TrungND
 */
public class CheckUserListAdapter extends ArrayAdapter<RealmUserModel>{

	private List<RealmUserModel>	mLstUser	= new ArrayList<>();
	private Context			mContext;

	public class UserViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtName;

		public UserViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtName = (TextView)view.findViewById(R.id.txt_id_name);
		}
	}

	public CheckUserListAdapter(Context context, List<RealmUserModel> userList){
		super(context, R.layout.item_check_user_list, userList);
		this.mContext = context;
		this.mLstUser = userList;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		final RealmUserModel model = this.mLstUser.get(position);

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
