package trente.asia.calendar.commons.adapter;

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
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * FilterUserListAdapter
 *
 * @author TrungND
 */
public class FilterUserListAdapter extends ArrayAdapter<UserModel>{

	private List<UserModel> lstUser;

	public FilterUserListAdapter(Context context, List<UserModel> lstUser){
		super(context, R.layout.adapter_dialog_user_item, lstUser);
		this.lstUser = lstUser;
	}

	/* private view holder class */
	private class ViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtUserName;

		public ViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtUserName = (TextView)view.findViewById(R.id.txt_id_user_name);
		}
	}

	@Override
	public int getCount(){
		return this.lstUser.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		UserModel userModel = this.getItem(position);
		if(convertView == null){
			LayoutInflater mInflater = (LayoutInflater)this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.adapter_dialog_user_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}

		holder.txtUserName.setText(userModel.userName);
		if(!CCStringUtil.isEmpty(userModel.avatarPath)){
			WfPicassoHelper.loadImage(this.getContext(), BuildConfig.HOST + userModel.avatarPath, holder.imgAvatar, null);
		}
		return convertView;
	}
}
