package trente.asia.shiftworking.services.shiftworking.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.shiftworking.model.WorkHistoryModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * ShiftWorkingAdapter.
 *
 * @author TrungND
 */
public class ShiftWorkingAdapter extends ArrayAdapter<WorkHistoryModel>{

	private List<WorkHistoryModel>	lstHistory;
	private Context					mContext;

	public class BoardViewHolder{

		public TextView	txtWorkDate;
		public TextView	txtLocation;
		public TextView	txtWorkShift;
		public TextView	txtWorktime;

		public BoardViewHolder(View view){
			txtWorkDate = (TextView)view.findViewById(R.id.txt_id_work_date);
			txtLocation = (TextView)view.findViewById(R.id.txt_id_location);
			txtWorkShift = (TextView)view.findViewById(R.id.txt_id_work_shift);
			txtWorktime = (TextView)view.findViewById(R.id.txt_id_work_time);
		}
	}

	public ShiftWorkingAdapter(Context context, List<WorkHistoryModel> lstHistory){
		super(context, R.layout.item_shift_working_list, lstHistory);
		this.mContext = context;
		this.lstHistory = lstHistory;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		WorkHistoryModel model = this.lstHistory.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_shift_working_list, null);
		BoardViewHolder holder = new BoardViewHolder(convertView);

		holder.txtWorkDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_10, CCDateUtil.makeDateCustom(model.workDate, WelfareConst.WL_DATE_TIME_1)));
		holder.txtLocation.setText(model.project.projectLocation);
		holder.txtWorkShift.setText(WelfareFormatUtil.connect2String(model.startShift, model.endShift, "-"));
		holder.txtWorktime.setText(WelfareFormatUtil.connect2String(model.startTime, model.endTime, "-"));

		return convertView;
	}

	public void clearAll(){
		this.lstHistory.clear();
		this.notifyDataSetChanged();
	}
}
