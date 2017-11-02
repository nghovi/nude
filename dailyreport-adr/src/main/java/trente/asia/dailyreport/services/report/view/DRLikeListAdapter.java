package nguyenhoangviet.vpcorp.dailyreport.services.report.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.dailyreport.BuildConfig;
import nguyenhoangviet.vpcorp.dailyreport.DRConst;
import nguyenhoangviet.vpcorp.dailyreport.R;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.DRCheckModel;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.DRLikeModel;
import nguyenhoangviet.vpcorp.dailyreport.services.report.model.ReportModel;
import nguyenhoangviet.vpcorp.dailyreport.utils.DRUtil;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 5/13/2016.
 */
public class DRLikeListAdapter extends ArrayAdapter{

	Context	context;
	int		layoutId;
	boolean	isLikers;

	public DRLikeListAdapter(Context context, int resource, ReportModel reportModel, boolean isLikers){
		super(context, resource, isLikers ? reportModel.likes : reportModel.checks);
		this.isLikers = isLikers;
		this.context = context;
		this.layoutId = resource;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		UserModel user;
		String date;
		if(isLikers){
			DRLikeModel drLikeModel = (DRLikeModel)getItem(position);
			user = drLikeModel.likeUser;
			date = drLikeModel.likeDate;
		}else{
			DRCheckModel drCheckModel = (DRCheckModel)getItem(position);
			user = drCheckModel.checkUser;
			date = drCheckModel.checkDate;
		}
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.item_user_txt_name);
			viewHolder.txtUserMail = (TextView)convertView.findViewById(R.id.item_user_txt_mail);
			viewHolder.imgAvatar = (ImageView)convertView.findViewById(R.id.item_user_img_avatar);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		viewHolder.txtUsername.setText(user.userName);
		viewHolder.txtUserMail.setText(DRUtil.getDateString(date, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DRConst.DATE_FORMAT_YYYY_MM_DD_HH_MM));
		WfPicassoHelper.loadImageWithDefaultIcon(context, BuildConfig.HOST, viewHolder.imgAvatar, user.avatarPath, R.drawable.wf_profile);
		return convertView;
	}

	private class ViewHolder{

		TextView	txtUsername;
		TextView	txtUserMail;
		ImageView	imgAvatar;
	}
}
