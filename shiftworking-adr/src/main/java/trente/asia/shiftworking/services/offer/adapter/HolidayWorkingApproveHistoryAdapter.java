package trente.asia.shiftworking.services.offer.adapter;

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
import trente.asia.shiftworking.BuildConfig;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.offer.model.ApproveHistory;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author VietNH
 */
public class HolidayWorkingApproveHistoryAdapter extends ArrayAdapter<ApproveHistory>{

	// private List<ApproveHistory> lstHistory;
	private Context			mContext;
	private int				layoutId;
	private LayoutInflater	mInflater;

	public HolidayWorkingApproveHistoryAdapter(Context context, List<ApproveHistory> lstHistory){
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
		viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.txt_item_approve_history_username);
		viewHolder.txtComment = (TextView)convertView.findViewById(R.id.txt_item_approve_history_comment);
		viewHolder.txtResult = (TextView)convertView.findViewById(R.id.txt_item_approve_history_status);
		viewHolder.txtTime = (TextView)convertView.findViewById(R.id.txt_item_approve_history_time);

		ApproveHistory history = getItem(position);
		viewHolder.txtUsername.setText(history.userName);
		viewHolder.txtTime.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, CCDateUtil.makeDate(history.historyDate)));
		viewHolder.txtResult.setText(history.historyStatus);
		viewHolder.txtComment.setText(history.historyComment);
		return convertView;
	}

	private class ViewHolder{

		TextView		txtUsername;
		// TextView txtDate;
		TextView		txtResult;
		TextView		txtComment;
		public TextView	txtTime;
	}
}
