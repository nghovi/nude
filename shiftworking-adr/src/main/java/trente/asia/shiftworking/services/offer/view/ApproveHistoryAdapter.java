package trente.asia.shiftworking.services.offer.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.offer.model.ApproveHistory;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author VietNH
 */
public class ApproveHistoryAdapter extends ArrayAdapter<ApproveHistory>{

	private List<ApproveHistory>	lstHistory;
	private Context					mContext;
	private int						layoutId;

	public ApproveHistoryAdapter(Context context, List<ApproveHistory> lstHistory){
		super(context, R.layout.item_approve_history, lstHistory);
		this.mContext = context;
		this.layoutId = R.layout.item_approve_history;
		this.lstHistory = lstHistory;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;// ViewHolder pattern http://developer.android
		// .com/training/improving-layouts/smooth-scrolling.html
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.imgAvatar = (ImageView)convertView.findViewById(R.id.img_item_approve_history_avatar);
			viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.txt_item_approve_history_username);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_approve_history_date);
			viewHolder.txtComment = (TextView)convertView.findViewById(R.id.txt_item_approve_history_comment);
			viewHolder.txtResult = (TextView)convertView.findViewById(R.id.txt_item_approve_history_status);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		ApproveHistory history = getItem(position);
		WfPicassoHelper.loadImage(mContext, history.userAvatarPath, viewHolder.imgAvatar, null);
		viewHolder.txtUsername.setText(history.userName);
		viewHolder.txtDate.setText(history.historyDate);
		viewHolder.txtResult.setText(history.historyStatus);
		viewHolder.txtComment.setText(history.resultTypeName);
		return convertView;
	}

	private class ViewHolder{

		TextView	txtUsername;
		TextView	txtDate;
		TextView	txtResult;
		TextView	txtComment;
		ImageView	imgAvatar;
	}
}
