package nguyenhoangviet.vpcorp.dailyreport.services.activities.view;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.dailyreport.BuildConfig;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.activities.model.ActivityModel;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 5/13/2016.
 */
public class ActivityListAdapter extends ArrayAdapter<ActivityModel>{

	private Context	context;
	private int		layoutId;

	public ActivityListAdapter(Context context, int resource, List<ActivityModel> objects){
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
			viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.item_user_txt_name);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.item_user_txt_date);
			viewHolder.txtContent = (TextView)convertView.findViewById(R.id.item_user_txt_content);
			viewHolder.imgAvatar = (ImageView)convertView.findViewById(R.id.item_user_img_avatar);
			viewHolder.txtDescription = (TextView)convertView.findViewById(R.id.item_user_txt_desc);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		ActivityModel activityModel = getItem(position);

		Date activityDate = CCDateUtil.makeDateCustom(activityModel.activityDate, WelfareConst.WF_DATE_TIME);
		String messageDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HH_MM, activityDate);
		viewHolder.txtUsername.setText(activityModel.activityUserName);
		viewHolder.txtDate.setText(messageDateFormat);
		viewHolder.txtContent.setText(activityModel.activityMessage);

		Date reportDate = CCDateUtil.makeDateCustom(activityModel.report.reportDate, WelfareConst.WF_DATE_TIME);
		String reportDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_MM_DD, reportDate);
		viewHolder.txtDescription.setText(context.getString(R.string.dr_activity_item_desc, reportDateFormat, activityModel.report.reportUser.userName));
		viewHolder.imgAvatar.setImageResource(R.drawable.wf_profile);
		if(!CCStringUtil.isEmpty(activityModel.activityUserAvatarPath)){
			// WfPicassoHelper.loadImage(context, BuildConfig.HOST + activityModel.activityUserAvatarPath, viewHolder.imgAvatar, null);
			WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, viewHolder.imgAvatar, activityModel.activityUserAvatarPath, R.drawable.wf_profile);
		}
		return convertView;
	}

	private class ViewHolder{

		TextView	txtUsername;
		TextView	txtDate;
		TextView	txtContent;
		ImageView	imgAvatar;
		TextView	txtDescription;
	}
}
