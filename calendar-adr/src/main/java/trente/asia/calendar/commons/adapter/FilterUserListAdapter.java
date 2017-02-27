package trente.asia.calendar.commons.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.listener.CsOnCheckedChangeListener;
import trente.asia.android.view.layout.CheckableLinearLayout;
import trente.asia.calendar.BuildConfig;
import trente.asia.calendar.R;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * FilterUserListAdapter
 *
 * @author TrungND
 */
public class FilterUserListAdapter extends ArrayAdapter<UserModel>{

	private List<UserModel>	lstUser;
	private List<UserModel>	mLstSelectedUser;
	public List<UserModel>	mLstShowUser	= new ArrayList<>();

	public FilterUserListAdapter(Context context, List<UserModel> lstUser, List<UserModel> lstSelectedUser){
		super(context, R.layout.adapter_dialog_user_item, lstUser);
		this.lstUser = lstUser;
		this.mLstSelectedUser = lstSelectedUser;
		if(!CCCollectionUtil.isEmpty(lstSelectedUser)){
			for(UserModel userModel : lstSelectedUser){
				mLstShowUser.add(userModel);
			}
		}
	}

	/* private view holder class */
	private class ViewHolder{

		public ImageView				imgAvatar;
		public TextView					txtUserName;
		public CheckableLinearLayout	lnrItem;
		public ImageView				imgCheck;

		public ViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtUserName = (TextView)view.findViewById(R.id.txt_id_user_name);
			lnrItem = (CheckableLinearLayout)view.findViewById(R.id.lnr_id_item);
			imgCheck = (ImageView)view.findViewById(R.id.img_id_check);
		}
	}

	@Override
	public int getCount(){
		return this.lstUser.size();
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent){
		final ViewHolder holder;
		final UserModel userModel = this.getItem(position);
		if(convertView == null){
			LayoutInflater mInflater = (LayoutInflater)this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.adapter_dialog_user_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}

		holder.txtUserName.setText(userModel.userName);
		if(userModel.bitmap != null){
			holder.imgAvatar.setImageBitmap(userModel.bitmap);
		}else{
			if(!CCStringUtil.isEmpty(userModel.avatarPath)){
				WfPicassoHelper.loadImage(this.getContext(), BuildConfig.HOST + userModel.avatarPath, holder.imgAvatar, null, userModel);
			}
		}

		if(WelfareUtil.containUserInList(this.mLstShowUser, userModel)){
			(((ListView)parent)).setItemChecked(position, true);
			holder.lnrItem.setChecked(true);
			holder.imgCheck.setVisibility(View.VISIBLE);
		}else{
			(((ListView)parent)).setItemChecked(position, false);
			holder.lnrItem.setChecked(false);
			holder.imgCheck.setVisibility(View.INVISIBLE);
		}

		holder.lnrItem.setOnCheckedChangeListener(new CsOnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(Checkable view, boolean isChecked){
				if(isChecked){
					holder.imgCheck.setVisibility(View.VISIBLE);
					WelfareUtil.addInList(mLstShowUser, userModel);
				}else{
					holder.imgCheck.setVisibility(View.INVISIBLE);
					WelfareUtil.removeInList(mLstShowUser, userModel);
				}
			}
		});
		return convertView;
	}

	public List<UserModel> getSelectedUser(){
		return this.mLstSelectedUser;
	}
}
