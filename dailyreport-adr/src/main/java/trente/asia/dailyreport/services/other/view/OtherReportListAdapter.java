package trente.asia.dailyreport.services.other.view;

import static trente.asia.dailyreport.services.report.view.MyReportListAdapter.setReportStatsNumberColor;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import trente.asia.dailyreport.BuildConfig;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.utils.DRUtil;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.utils.WfPicassoHelper;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Created by viet on 5/13/2016.
 */
public class OtherReportListAdapter extends ArrayAdapter<ReportModel>{

	Context					context;
	int						layoutId;
	WelfareFragment.OnAvatarClickListener onAvatarClickListener;

	public OtherReportListAdapter(Context context, int resource, List<ReportModel> objects, WelfareFragment.OnAvatarClickListener onAvatarClickedListener){
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.onAvatarClickListener = onAvatarClickedListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		final ReportModel reportModel = getItem(position);
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.lnrContainer = (LinearLayout)convertView.findViewById(R.id.item_my_report_container);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_my_report_date);
			viewHolder.txtContent = (TextView)convertView.findViewById(R.id.txt_item_my_report_content);
			viewHolder.txtCheckedNum = (TextView)convertView.findViewById(R.id.share_lnr_check_txt_checked_num);
			viewHolder.txtLikedNum = (TextView)convertView.findViewById(R.id.share_lnr_like_txt_num);
			viewHolder.txtCommentedNum = (TextView)convertView.findViewById(R.id.share_lnr_comment_txt_num);
			viewHolder.txtFileNum = (TextView)convertView.findViewById(R.id.share_lnr_file_txt_num);
			viewHolder.lnrIcons = (LinearLayout)convertView.findViewById(R.id.item_report_icons);
			viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.txt_item_my_report_username);
			viewHolder.avatar = (SelectableRoundedImageView)convertView.findViewById(R.id.txt_item_my_report_avatar);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		Calendar c = Calendar.getInstance();
		c.setTime(DRUtil.getDateFromString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
		String dayStr = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);// Sun,
																							// Sat,..
		String contentStr = "";

		if(reportModel.holiday != null){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_item_calendar_background_holiday);
		}else if(DRUtil.getDateString(Calendar.getInstance().getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD).equals(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD))){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_background_selectable);
		}else if("Sat".equals(dayStr)){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_item_calendar_background_sat);
		}else if("Sun".equals(dayStr)){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_item_calendar_background_sun);
		}else{
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_background_selectable);
		}

		contentStr = reportModel.reportContent;

		viewHolder.txtContent.setText(contentStr);

		int checkedNum = reportModel.checks.size();
		int fileNum = DRUtil.getFileNum(reportModel);
		int likeNum = reportModel.likes.size();
		int commentNum = reportModel.comments.size();

		if(checkedNum > 0 || fileNum > 0 || likeNum > 0 || commentNum > 0){
			viewHolder.lnrIcons.setVisibility(View.VISIBLE);
		}else{
			viewHolder.lnrIcons.setVisibility(View.GONE);
		}

		viewHolder.txtCheckedNum.setText(String.valueOf(checkedNum));
		viewHolder.txtFileNum.setText(String.valueOf(fileNum));
		viewHolder.txtLikedNum.setText(String.valueOf(likeNum));
		viewHolder.txtCommentedNum.setText(String.valueOf(commentNum));

		setReportStatsNumberColor(context, viewHolder.txtCheckedNum, checkedNum);
		setReportStatsNumberColor(context, viewHolder.txtFileNum, fileNum);
		setReportStatsNumberColor(context, viewHolder.txtLikedNum, likeNum);
		setReportStatsNumberColor(context, viewHolder.txtCommentedNum, commentNum);

		viewHolder.txtUsername.setText(reportModel.reportUser.userName);
		WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, viewHolder.avatar, reportModel.reportUser.avatarPath, R.drawable.wf_profile);
		viewHolder.avatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				onAvatarClickListener.OnAvatarClick(reportModel.reportUser.userName, reportModel.reportUser.avatarPath);
			}
		});
		viewHolder.txtDate.setText(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD));
		return convertView;
	}

	private class ViewHolder{

		LinearLayout				lnrContainer;
		TextView					txtDate;
		TextView					txtContent;
		TextView					txtCheckedNum;
		TextView					txtLikedNum;
		SelectableRoundedImageView	avatar;
		TextView					txtUsername;
		TextView					txtCommentedNum;
		TextView					txtFileNum;
		public LinearLayout			lnrComment;
		public LinearLayout			lnrFile;

		public LinearLayout			lnrIcons;
	}
}
