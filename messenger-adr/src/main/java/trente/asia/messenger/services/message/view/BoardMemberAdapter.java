package trente.asia.messenger.services.message.view;

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
import trente.asia.messenger.services.message.model.RealmUserModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * BoardMemberAdapter
 *
 * @author TrungND
 */
public class BoardMemberAdapter extends ArrayAdapter<RealmUserModel>{

	private List<RealmUserModel>	mMemberList;
	private Context			mContext;

	public BoardMemberAdapter(Context context, List<RealmUserModel> memberList){
		super(context, R.layout.item_board_member, memberList);
		this.mContext = context;
		this.mMemberList = memberList;
	}

	public class MembersViewHolder{

		public ImageView	imgMemberAvatar;
		public TextView		txtAccountName;
		public TextView		txtUserName;

		public MembersViewHolder(View view){
			imgMemberAvatar = (ImageView)view.findViewById(R.id.img_member_avatar);
			txtAccountName = (TextView)view.findViewById(R.id.txt_account_name);
			txtUserName = (TextView)view.findViewById(R.id.txt_user_name);
		}
	}

	@Override
	public int getCount(){
		return this.mMemberList.size();
	}

	public View getView(int position, View convertView, ViewGroup parent){
		RealmUserModel model = this.mMemberList.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_board_member, null);

		BoardMemberAdapter.MembersViewHolder holder = new MembersViewHolder(convertView);
		holder.txtAccountName.setText(model.userAccount);
		holder.txtUserName.setText(model.userName);
		if(!CCStringUtil.isEmpty(model.avatarPath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.avatarPath, holder.imgMemberAvatar, null);
		}
		return convertView;
	}
}
