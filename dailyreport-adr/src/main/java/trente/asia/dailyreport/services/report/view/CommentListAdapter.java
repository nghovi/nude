package trente.asia.dailyreport.services.report.view;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.dailyreport.BuildConfig;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.report.model.ReportComment;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 5/13/2016.
 */
public class CommentListAdapter extends ArrayAdapter<ReportComment>{

	Context	context;
	int		layoutId;

	public CommentListAdapter(Context context, int resource, List<ReportComment> objects){
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			// // TODO: 5/13/2016
			viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.item_report_comment_username);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.item_report_comment_date);
			viewHolder.txtCommentContent = (TextView)convertView.findViewById(R.id.item_report_comment_content);
			viewHolder.imgAvatar = (ImageView)convertView.findViewById(R.id.item_user_img_avatar);
			viewHolder.imgPhoto = (ImageView)convertView.findViewById(R.id.item_report_comment_photo);
			viewHolder.txtFileName = (TextView)convertView.findViewById(R.id.item_report_comment_file_name);
			viewHolder.lnrFile = (LinearLayout)convertView.findViewById(R.id.item_report_comment_lnr_file);
			viewHolder.lnrPreview = (RelativeLayout)convertView.findViewById(R.id.item_report_comment_lnr_preview);
			viewHolder.imgPlayIcon = (ImageView)convertView.findViewById(R.id.item_report_comment_play_icon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		CommentModel commentModel = getItem(position);
		Date activityDate = CCDateUtil.makeDateCustom(commentModel.commentDate, WelfareConst.WF_DATE_TIME);
		String commentDateFormat = CCFormatUtil.formatDateCustom(DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM, activityDate);
		viewHolder.txtUsername.setText(commentModel.commentUser.userName);
		viewHolder.txtDate.setText(context.getString(R.string.dr_at_time, commentDateFormat));
		viewHolder.txtCommentContent.setText(commentModel.commentContent);

		if(DRUtil.isNotEmpty(commentModel.commentContent)){
			viewHolder.txtCommentContent.setVisibility(View.VISIBLE);
		}else{
			viewHolder.txtCommentContent.setVisibility(View.GONE);
		}

		WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, viewHolder.imgAvatar, commentModel.commentUser.avatarPath, R.drawable.wf_profile);
		if(DRUtil.isNotEmpty(commentModel.thumbnail.fileUrl) && !CommentModel.COMMENT_TYPE_FILE.equals(commentModel.commentType)){
			viewHolder.lnrPreview.setVisibility(View.VISIBLE);
			WfPicassoHelper.loadImage2(context, BuildConfig.HOST, viewHolder.imgPhoto, commentModel.thumbnail.fileUrl);
		}else{
			viewHolder.lnrPreview.setVisibility(View.GONE);
		}

		if(!CommentModel.COMMENT_TYPE_MOVIE.equals(commentModel.commentType)){
			viewHolder.imgPlayIcon.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.imgPlayIcon.setVisibility(View.VISIBLE);
		}

		if(DRUtil.isNotEmpty(commentModel.attachment.fileName) && (CommentModel.COMMENT_TYPE_MOVIE.equals(commentModel.commentType) || CommentModel.COMMENT_TYPE_FILE.equals(commentModel.commentType))){
			viewHolder.lnrFile.setVisibility(View.VISIBLE);
			viewHolder.txtFileName.setText(commentModel.attachment.fileName);
		}else{
			viewHolder.lnrFile.setVisibility(View.GONE);
		}

		return convertView;
	}

	@Override
	public boolean isEnabled(int position){
		CommentModel commentModel = getItem(position);
		return !CommentModel.COMMENT_TYPE_TEXT.equals(commentModel.commentType);
	}

	private class ViewHolder{

		TextView			txtUsername;
		TextView			txtDate;
		TextView			txtCommentContent;
		ImageView			imgAvatar;
		ImageView			imgPhoto;
		TextView			txtFileName;
		LinearLayout		lnrFile;
		RelativeLayout		lnrPreview;
		public ImageView	imgPlayIcon;
	}
}
