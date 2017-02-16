package trente.asia.welfare.adr.view.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import trente.asia.welfare.adr.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet-nh on 7/30/2015.
 */
public class UserListMultipleChoiceAdapter extends ArrayAdapter<UserModel>{

	List<UserModel>	users;

	public UserListMultipleChoiceAdapter(Context context, List<UserModel> users){
		super(context, R.layout.item_user_list_multiple_choice_dialog, users);
		this.users = users;
	}

	/* private view holder class */
	private class ViewHolder{

		TextView	txtUserName;
		ImageView	imgUser;
	}

	@Override
	public int getCount(){
		return users.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		UserModel user = this.getItem(position);
		if(convertView == null){
			convertView = ((LayoutInflater)this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_dialog_list_item, null);
			holder = new ViewHolder();
			holder.txtUserName = (TextView)convertView.findViewById(R.id.txt_item_value);
			holder.imgUser = (ImageView)convertView.findViewById(R.id.img_user_list_multiple_choice_dialog);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.txtUserName.setText(user.userName);
		WfPicassoHelper.loadImage(getContext(), user.avatarPath, holder.imgUser, null);
		return convertView;
	}

	public static Integer findPosition4Code(String key, List<UserModel> users){
		int position = -1;
		for(int i = 0; i < users.size(); i++){
			UserModel user = users.get(i);
			if(user.key.equals(key)){
				position = i;
				break;
			}
		}
		return position;
	}
}
