package trente.asia.messenger.services.message.view;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.utils.MsUtils;
import trente.asia.welfare.adr.define.EmotionConst;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by Huy-nq on 7/22/2016.
 */
public class CommentAdapter extends ArrayAdapter<CommentModel>{

	private List<CommentModel>	commentList;
	private Context				mContext;

	public class CommentViewHolder{

		public ImageView	imgAvatarCmt;
		public TextView		txtUserNameCmt;
		public TextView		txtContentCmt;

		public CommentViewHolder(View view){
			imgAvatarCmt = (ImageView)view.findViewById(R.id.img_avatar_cmt);
			txtUserNameCmt = (TextView)view.findViewById(R.id.txt_userName_cmt);
			txtContentCmt = (TextView)view.findViewById(R.id.txt_contentCmt);
		}
	}

	public CommentAdapter(Context context, List<CommentModel> commentList){
		super(context, R.layout.item_comment_list, commentList);
		this.mContext = context;
		this.commentList = commentList;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		CommentModel model = this.commentList.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_comment_list, null);

		CommentAdapter.CommentViewHolder holder = new CommentViewHolder(convertView);

		Date commentDate = CCDateUtil.makeDateCustom(model.commentDate, WelfareConst.WL_DATE_TIME_1);
		String commentDateFormat = CCFormatUtil.formatDateCustom(mContext.getString(R.string.wf_date_time_format1), commentDate);
		holder.txtUserNameCmt.setText(mContext.getString(R.string.msg_message_info, model.commentUser.userName, commentDateFormat));
		if(!CCStringUtil.isEmpty(model.commentUser.avatarPath)){
			WfPicassoHelper.loadImage(mContext, BuildConfig.HOST + model.commentUser.avatarPath, holder.imgAvatarCmt, null);
		}else{
			WfPicassoHelper.loadImage(mContext, "https://upload.wikimedia.org/wikipedia/en/8/86/Avatar_Aang.png", holder.imgAvatarCmt, null);
		}

		if(!model.commentContent.contains(EmotionConst.EMO_LIKE)){
			holder.txtContentCmt.setText(model.commentContent);
		}else{
			SpannableStringBuilder builder = MsUtils.replaceAll(mContext, model.commentContent, EmotionConst.EMO_LIKE, R.drawable.wf_good);
			holder.txtContentCmt.setText(builder);
		}
		return convertView;
	}
}
