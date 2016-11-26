package trente.asia.thankscard.services.rank.view;

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

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.services.rank.model.RankModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * RankingListAdapter.
 *
 * @author TrungND
 */
public class RankingListAdapter extends ArrayAdapter<RankModel>{

	private List<RankModel>	mLstRanking	= new ArrayList<>();
	private Context			mContext;
	private UserModel		myself;

	public class RankingViewHolder{

		public TextView		txtTitle;
		public ImageView	imgMedal;
		public TextView		txtRanking;
		public ImageView	imgAvatar;
		public TextView		txtUserName;
		public TextView		txtPoint;

		public RankingViewHolder(View view, boolean isTitle){
			if(isTitle){
				txtTitle = (TextView)view.findViewById(R.id.txt_id_title);
			}else{
				imgMedal = (ImageView)view.findViewById(R.id.img_id_medal);
				txtRanking = (TextView)view.findViewById(R.id.txt_id_ranking);
				imgAvatar = (ImageView)view.findViewById(R.id.img_id_avatar);
				txtUserName = (TextView)view.findViewById(R.id.txt_id_user_name);
				txtPoint = (TextView)view.findViewById(R.id.txt_id_point);
			}
		}
	}

	public RankingListAdapter(Context context, List<RankModel> lstRanking){
		super(context, R.layout.item_title_ranking_list, lstRanking);
		this.mContext = context;
		this.mLstRanking = lstRanking;
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(context);
		myself = prefAccUtil.getUserPref();
	}

	public View getView(int position, View convertView, ViewGroup parent){

		RankModel model = this.mLstRanking.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(model.isTitle){
			convertView = mInflater.inflate(R.layout.item_title_ranking_list, null);
		}else{
			convertView = mInflater.inflate(R.layout.item_user_ranking_list, null);
		}

		RankingViewHolder holder = new RankingViewHolder(convertView, model.isTitle);
		if(model.isTitle){
			holder.txtTitle.setText(model.title);
		}else{
			if(myself.key.equals(model.userId)){
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.wf_login_background_color));
			}
			holder.imgMedal.setVisibility(View.VISIBLE);
			holder.txtRanking.setVisibility(View.GONE);
			holder.txtUserName.setText(model.userName);
			if(!CCStringUtil.isEmpty(model.avatarPath)){
				WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.avatarPath, holder.imgAvatar, null);
			}
			holder.txtPoint.setText(mContext.getString(R.string.tc_common_point, String.valueOf(model.cnt)));
			if(model.rank.equals(1)){
			}else if(model.rank.equals(2)){
				holder.imgMedal.setImageResource(R.drawable.tc_ranking_medal2);
			}else if(model.rank.equals(3)){
				holder.imgMedal.setImageResource(R.drawable.tc_ranking_medal3);
			}else{
				holder.imgMedal.setVisibility(View.GONE);
				holder.txtRanking.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

    @Override
    public boolean isEnabled(int position){
        RankModel model = getItem(position);
        return !(model.isTitle || CCStringUtil.isEmpty(model.userId) || CCConst.NONE.equals(model.userId));
    }
}
