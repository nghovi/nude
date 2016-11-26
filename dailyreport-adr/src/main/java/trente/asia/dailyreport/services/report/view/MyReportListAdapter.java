package trente.asia.dailyreport.services.report.view;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.dailyreport.DRConst;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.other.OthersFragment;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.utils.DRUtil;

/**
 * Created by viet on 5/13/2016.
 */
public class MyReportListAdapter extends ArrayAdapter<ReportModel>{

	Context	context;
	int		layoutId;

	public MyReportListAdapter(Context context, int resource, List<ReportModel> objects){
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
			viewHolder.lnrContainer = (LinearLayout)convertView.findViewById(R.id.item_my_report_container);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_my_report_date);
			viewHolder.txtDay = (TextView)convertView.findViewById(R.id.txt_item_my_report_day);
			viewHolder.txtContent = (TextView)convertView.findViewById(R.id.txt_item_my_report_content);
			viewHolder.txtCheckedNum = (TextView)convertView.findViewById(R.id.share_lnr_check_txt_checked_num);
			viewHolder.txtLikedNum = (TextView)convertView.findViewById(R.id.share_lnr_like_txt_num);
			viewHolder.txtCommentedNum = (TextView)convertView.findViewById(R.id.share_lnr_comment_txt_num);
			viewHolder.txtFileNum = (TextView)convertView.findViewById(R.id.share_lnr_file_txt_num);
			viewHolder.txtLastComment = (TextView)convertView.findViewById(R.id.txt_item_my_report_comment);
			viewHolder.lnrIcons = (LinearLayout)convertView.findViewById(R.id.item_report_icons);
			// viewHolder.lnrFile =
			// (LinearLayout) convertView
			// .findViewById(R.id.item_report_follower_info_lnr_file);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		ReportModel reportModel = getItem(position);
		viewHolder.txtDate.setText(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_DD));
		Calendar c = Calendar.getInstance();
		c.setTime(DRUtil.getDateFromString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
		String dayStr = OthersFragment.getDayString(c.getTime(), context);
		String contentStr = "";
		String lastCommentStr = "";

		if(reportModel.holiday != null){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_item_calendar_background_holiday);
			viewHolder.txtDate.setBackgroundResource(R.drawable.dr_background_white);
			viewHolder.txtDate.setTextColor(Color.RED);
			viewHolder.txtDay.setTextColor(Color.RED);

		}else
		// background for today
		if(DRUtil.getDateString(Calendar.getInstance().getTime(), DRConst.DATE_FORMAT_YYYY_MM_DD).equals(DRUtil.getDateString(reportModel.reportDate, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD))){
			viewHolder.txtDate.setBackgroundResource(R.drawable.dr_background_base_color_circle);
			viewHolder.txtDate.setTextColor(Color.WHITE);
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_background_selectable);
			viewHolder.txtDay.setTextColor(Color.GRAY);
		}else if(context.getResources().getString(R.string.dlr_saturday).equals(dayStr)){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_item_calendar_background_sat);
			viewHolder.txtDate.setBackgroundResource(R.drawable.dr_background_white);
			viewHolder.txtDate.setTextColor(Color.BLUE);
			viewHolder.txtDay.setTextColor(Color.BLUE);
		}else if(context.getResources().getString(R.string.dlr_sunday).equals(dayStr)){
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_item_calendar_background_sun);
			viewHolder.txtDate.setBackgroundResource(R.drawable.dr_background_white);
			viewHolder.txtDate.setTextColor(Color.RED);
			viewHolder.txtDay.setTextColor(Color.RED);
		}else{
			viewHolder.lnrContainer.setBackgroundResource(R.drawable.dr_background_selectable);
			viewHolder.txtDate.setBackgroundResource(R.drawable.dr_background_white);
			viewHolder.txtDate.setTextColor(Color.GRAY);
			viewHolder.txtDay.setTextColor(Color.GRAY);
		}
		if(CCStringUtil.isEmpty(reportModel.key)){
			// imgStatusIcon.setImageResource(R.drawable.ic_action_new);
			viewHolder.lnrIcons.setVisibility(View.INVISIBLE);// why
			// set gone here, whole item will be disappear ?
			// viewHolder.txtLastComment.setVisibility(View.INVISIBLE);
		}else{
			if(reportModel.reportStatus.equals(ReportModel.REPORT_STATUS_DRTT)){
				viewHolder.lnrIcons.setVisibility(View.INVISIBLE);
				// viewHolder.txtLastComment.setVisibility(View.INVISIBLE);
			}else{
				// viewHolder.txtLastComment.setVisibility(View.VISIBLE);
				viewHolder.lnrIcons.setVisibility(View.VISIBLE);
			}
			lastCommentStr = reportModel.comments.size() > 0 ? reportModel.comments.get(reportModel.comments.size() - 1).commentContent : "";
			contentStr = reportModel.reportContent;
		}

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

		viewHolder.txtLastComment.setText(lastCommentStr);
		viewHolder.txtDay.setText(dayStr);
		return convertView;
	}

	public static void setReportStatsNumberColor(Context context, TextView txtNum, int num){
		if(num > 0){
			txtNum.setTextColor(ContextCompat.getColor(context, R.color.file_count));
		}else{
			txtNum.setTextColor(ContextCompat.getColor(context, R.color.file_count_zero));
		}
	}

	private class ViewHolder{

		LinearLayout	lnrContainer;
		LinearLayout	lnrComment;
		LinearLayout	lnrFile;
		LinearLayout	lnrIcons;
		TextView		txtDate;
		TextView		txtDay;
		TextView		txtContent;
		TextView		txtCheckedNum;
		TextView		txtLikedNum;
		TextView		txtLastComment;
		TextView		txtCommentedNum;
		public TextView	txtFileNum;

	}
}
