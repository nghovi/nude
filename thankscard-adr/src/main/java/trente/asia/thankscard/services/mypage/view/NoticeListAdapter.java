package nguyenhoangviet.vpcorp.thankscard.services.mypage.view;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.services.mypage.model.NoticeModel;
import nguyenhoangviet.vpcorp.thankscard.utils.TCUtil;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;

/**
 * Created by viet on 3/16/2016.
 */
public class NoticeListAdapter extends ArrayAdapter<NoticeModel>{

	private Context	context;
	private int		resourceId;
	private int		mSelectedPosition;

	public NoticeListAdapter(Context context, int resource, List<NoticeModel> objects){
		super(context, resource, objects);
		this.context = context;
		this.resourceId = resource;
		this.mSelectedPosition = 0;
	}

	public int getSelectedPosition(){
		return mSelectedPosition;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;// ViewHolder pattern http://developer.android.com/training/improving-layouts/smooth-scrolling.html
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.txtNoticeDate = (TextView)convertView.findViewById(R.id.txt_item_notice_date);
			viewHolder.txtNoticeMessage = (TextView)convertView.findViewById(R.id.txt_item_notice_message);
			viewHolder.txtStatus = (TextView)convertView.findViewById(R.id.txt_item_notice_status);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		NoticeModel notice = getItem(position);
		viewHolder.txtStatus.setVisibility(isNewNotice(notice) ? View.VISIBLE : View.INVISIBLE);
		viewHolder.txtNoticeDate.setText(TCUtil.getDateString(notice.noticeDate, WelfareConst.WF_DATE_TIME, WelfareConst.WF_DATE_TIME_DATE));
		viewHolder.txtNoticeMessage.setText(notice.noticeMessage);
		return convertView;
	}

	private boolean isNewNotice(NoticeModel notice){
		Date noticeDate = CCDateUtil.makeDateCustom(notice.noticeDate, WelfareConst.WF_DATE_TIME);
		long diff = System.currentTimeMillis() - noticeDate.getTime();
		if(diff > 5 * 24 * 60 * 60 * 1000){ // notice is older than 5 days
			return false;
		}
		return true;
	}

	private class ViewHolder{

		TextView	txtNoticeMessage;
		TextView	txtNoticeDate;
		TextView	txtStatus;
	}
}
