package trente.asia.dailyreport.services.activities.view;

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
import trente.asia.dailyreport.BuildConfig;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.activities.model.ActivityModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

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

		Date activityDate = CCDateUtil.makeDateCustom(activityModel.activityDate, WelfareConst.WL_DATE_TIME_1);
		String messageDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_2, activityDate);
		viewHolder.txtUsername.setText(activityModel.activityUserName);
		viewHolder.txtDate.setText(messageDateFormat);
		viewHolder.txtContent.setText(activityModel.activityMessage);

		Date reportDate = CCDateUtil.makeDateCustom(activityModel.report.reportDate, WelfareConst.WL_DATE_TIME_1);
		String reportDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_6, reportDate);
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
