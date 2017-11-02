package nguyenhoangviet.vpcorp.messenger.services.message.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmUserModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by Huy-nq on 7/28/2016.
 */
public class MembersAdapter extends ArrayAdapter<RealmUserModel>{

	private List<RealmUserModel>	mMemberList = new ArrayList<>();
	private Context			mContext;
    private List<RealmUserModel>	fullMemberList;

	public MembersAdapter(Context context, List<RealmUserModel> mMembers){
		super(context, R.layout.item_members, mMembers);
		this.mContext = context;
		this.fullMemberList = mMembers;
        for(RealmUserModel userModel : fullMemberList){
            this.mMemberList.add(userModel);
        }
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
		convertView = mInflater.inflate(R.layout.item_members, null);

		MembersAdapter.MembersViewHolder holder = new MembersViewHolder(convertView);
		holder.txtAccountName.setText(model.userAccount);
		if(!CCStringUtil.isEmpty(model.avatarPath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.avatarPath, holder.imgMemberAvatar, null);
		}
		holder.txtUserName.setText(model.userName);
		return convertView;
	}

	@Override
	public Filter getFilter(){
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results){
				if(results.count > 0){
					mMemberList = (ArrayList<RealmUserModel>)results.values;
					notifyDataSetChanged();
				}else{
					notifyDataSetInvalidated();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint){
				FilterResults results = new FilterResults();
				ArrayList<RealmUserModel> filterArrayList = new ArrayList<>();
				if(CCStringUtil.isEmpty(CCStringUtil.toString(constraint))){
					results.count = filterArrayList.size();
					results.values = filterArrayList;
				}else{
					constraint = constraint.subSequence(1, constraint.length()).toString().toLowerCase();
					for(RealmUserModel userModel : fullMemberList){
						if(userModel.userAccount.toLowerCase().contains(constraint)){
							filterArrayList.add(userModel);
						}
					}
					results.count = filterArrayList.size();
					results.values = filterArrayList;
				}
				return results;
			}
		};
		return filter;
	}
}
