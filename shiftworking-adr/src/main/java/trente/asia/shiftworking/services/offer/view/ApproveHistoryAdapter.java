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
import trente.asia.shiftworking.BuildConfig;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.offer.model.ApproveHistory;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author VietNH
 */
public class ApproveHistoryAdapter extends ArrayAdapter<ApproveHistory>{

//	private List<ApproveHistory>	lstHistory;
	private Context					mContext;
	private int						layoutId;
    private LayoutInflater mInflater;

	public ApproveHistoryAdapter(Context context, List<ApproveHistory> lstHistory){
		super(context, R.layout.item_approve_history, lstHistory);
		this.mContext = context;
		this.layoutId = R.layout.item_approve_history;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
        convertView = mInflater.inflate(layoutId, null);
        viewHolder = new ViewHolder();
        viewHolder.imgAvatar = (ImageView)convertView.findViewById(R.id.img_item_approve_history_avatar);
        viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.txt_item_approve_history_username);
        viewHolder.txtComment = (TextView)convertView.findViewById(R.id.txt_item_approve_history_comment);
        viewHolder.txtResult = (TextView)convertView.findViewById(R.id.txt_item_approve_history_status);

		ApproveHistory history = getItem(position);
		WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, viewHolder.imgAvatar, history.userAvatarPath, R.drawable.wf_profile);
		viewHolder.txtUsername.setText(history.userName + " " + history.historyDate);
		viewHolder.txtResult.setText(history.historyStatus);
		viewHolder.txtComment.setText(history.historyComment);
		return convertView;
	}

	private class ViewHolder{

		TextView	txtUsername;
//		TextView	txtDate;
		TextView	txtResult;
		TextView	txtComment;
		ImageView	imgAvatar;
	}
}
