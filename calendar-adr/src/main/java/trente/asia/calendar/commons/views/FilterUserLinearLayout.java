package trente.asia.calendar.commons.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * FilterUserLinearLayout
 *
 * @author TrungND
 */

public class FilterUserLinearLayout extends LinearLayout{

	private Context						mContext;
	// private List<UserModel> lstUser;
	private List<UserModel>				lstSelectedUser;

	public List<CheckableLinearLayout>	lstCheckable;
	private CheckBox					mCbxAll;

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

	public FilterUserLinearLayout(Context context){
		super(context);
	}

	public FilterUserLinearLayout(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	public FilterUserLinearLayout(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	public void addUserList(List<UserModel> lstUser, List<UserModel> lstSelectedUser, final CheckBox cbxAll){
		// this.lstUser = lstUser;
		this.lstSelectedUser = lstSelectedUser;
		lstCheckable = new ArrayList<>();
		this.mCbxAll = cbxAll;
		this.removeAllViews();

		if(!CCCollectionUtil.isEmpty(lstUser)){
			for(UserModel userModel : lstUser){
				LayoutInflater mInflater = (LayoutInflater)this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				View userView = mInflater.inflate(R.layout.adapter_dialog_user_item, null);

				final ViewHolder holder = new ViewHolder(userView);
				holder.txtUserName.setText(userModel.userName);
				if(userModel.bitmap != null){
					holder.imgAvatar.setImageBitmap(userModel.bitmap);
				}else{
					if(!CCStringUtil.isEmpty(userModel.avatarPath)){
						WfPicassoHelper.loadImage(this.getContext(), BuildConfig.HOST + userModel.avatarPath, holder.imgAvatar, null, userModel);
					}
				}

				if(WelfareUtil.containUserInList(this.lstSelectedUser, userModel)){
					holder.lnrItem.setChecked(true);
					holder.imgCheck.setVisibility(View.VISIBLE);
				}else{
					holder.lnrItem.setChecked(false);
					holder.imgCheck.setVisibility(View.INVISIBLE);
				}

				holder.lnrItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v){
						boolean isChecked = holder.lnrItem.isChecked();
						holder.lnrItem.setChecked(!isChecked);
					}
				});
				holder.lnrItem.setOnCheckedChangeListener(new CsOnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(Checkable view, boolean isChecked){
						if(isChecked){
							holder.imgCheck.setVisibility(View.VISIBLE);
							judgeCheckAll();
						}else{
							holder.imgCheck.setVisibility(View.INVISIBLE);
							if(mCbxAll != null){
								cbxAll.setChecked(false);
							}
						}
					}
				});
				this.lstCheckable.add(holder.lnrItem);
				this.addView(userView);
			}
			judgeCheckAll();
		}
	}

	private void judgeCheckAll(){
		boolean isChecked = true;
		for(CheckableLinearLayout checkableLinearLayout : lstCheckable){
			if(!checkableLinearLayout.isChecked()){
				isChecked = false;
			}
		}
		if(mCbxAll != null){
			mCbxAll.setChecked(isChecked);
		}
	}
}
