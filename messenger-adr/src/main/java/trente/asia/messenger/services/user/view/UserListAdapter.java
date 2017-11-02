package nguyenhoangviet.vpcorp.messenger.services.user.view;

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
import nguyenhoangviet.vpcorp.messenger.services.user.listener.OnAddUserListener;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareFragment;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * UserListAdapter.
 *
 * @author TrungND
 */
public class UserListAdapter extends ArrayAdapter<UserModel>{

	private final WelfareFragment.OnAvatarClickListener	onAvatarClickListener;
	private List<UserModel>								mLstUser		= new ArrayList<>();
	private Context										mContext;
	private OnAddUserListener							mListener;
	private List<UserModel>								mLstUserDisplay	= new ArrayList<>();

	public class UserViewHolder{

		public ImageView	imgAvatar;
		public TextView		txtUserName;
		public TextView		txtAccountName;
		public ImageView	imgAdd;

		public UserViewHolder(View view){
			imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
			txtAccountName = (TextView)view.findViewById(R.id.txt_id_account_name);
			txtUserName = (TextView)view.findViewById(R.id.txt_id_user_name);
			imgAdd = (ImageView)view.findViewById(R.id.img_id_add);
		}
	}

	public UserListAdapter(Context context, List<UserModel> userList, OnAddUserListener listener, WelfareFragment.OnAvatarClickListener onAvatarClickListener){
		super(context, R.layout.item_user_list, userList);
		this.mContext = context;
		this.mLstUserDisplay = userList;
		this.mListener = listener;
		for(UserModel userModel : userList){
			this.mLstUser.add(userModel);
		}
		this.onAvatarClickListener = onAvatarClickListener;
	}

	@Override
	public int getCount(){
		return this.mLstUserDisplay.size();
	}

	public View getView(int position, View convertView, ViewGroup parent){

		final UserModel model = this.mLstUserDisplay.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_user_list, null);
		UserViewHolder holder = new UserViewHolder(convertView);

		holder.txtUserName.setText(model.userName);
		holder.txtAccountName.setText(model.userAccount);
		if(!CCStringUtil.isEmpty(model.avatarPath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.avatarPath, holder.imgAvatar, null);
		}else{
		}
		holder.imgAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onAvatarClickListener.OnAvatarClick(model.userName, model.avatarPath);
			}
		});

		holder.imgAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				if(mListener != null){
					mListener.onAddUsertListener(model);
				}
			}
		});

		return convertView;
	}

	@Override
	public Filter getFilter(){
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results){

				mLstUserDisplay = (ArrayList<UserModel>)results.values; // has the filtered values
				notifyDataSetChanged(); // notifies the data with new filtered values
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint){
				FilterResults results = new FilterResults(); // Holds the results of a filtering operation in values
				ArrayList<UserModel> filterArrayList = new ArrayList<UserModel>();

				// if(mOriginalValues == null){
				// mOriginalValues = new ArrayList<Product>(mDisplayedValues); // saves the original data in mOriginalValues
				// }

				/********
				 * If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
				 * else does the Filtering and returns FilteredArrList(Filtered)
				 ********/
				if(CCStringUtil.isEmpty(CCStringUtil.toString(constraint))){

					// set the Original result to return
					results.count = mLstUser.size();
					results.values = mLstUser;
				}else{
					constraint = constraint.toString().toLowerCase().trim();
					for(UserModel userModel : mLstUser){
						String data = userModel.userName;
						if(data.toLowerCase().contains(constraint)){
							filterArrayList.add(userModel);
						}
					}
					// set the Filtered result to return
					results.count = filterArrayList.size();
					results.values = filterArrayList;
				}
				return results;
			}
		};
		return filter;
	}
}
