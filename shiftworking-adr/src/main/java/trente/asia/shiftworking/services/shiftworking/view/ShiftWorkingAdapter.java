package nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.view;

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
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.defines.SwConst;
import nguyenhoangviet.vpcorp.shiftworking.services.shiftworking.model.WorkHistoryModel;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareFormatUtil;

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
        public TextView	txtCheckIn;
		public TextView	txtWorktime;

		public TextView	txtTitle;
		public TextView	txtValue;

		public BoardViewHolder(View view, String itemType){
			if(SwConst.SW_SHIFTWORKING_TYPE_SHIFT.equals(itemType)){
				txtWorkDate = (TextView)view.findViewById(R.id.txt_id_work_date);
				txtLocation = (TextView)view.findViewById(R.id.txt_id_location);
				txtWorkShift = (TextView)view.findViewById(R.id.txt_id_work_shift);
                txtCheckIn = (TextView)view.findViewById(R.id.txt_id_check_in);
				txtWorktime = (TextView)view.findViewById(R.id.txt_id_work_time);
			}else if(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY.equals(itemType)){
				txtTitle = (TextView)view.findViewById(R.id.txt_id_summary_title);
				txtValue = (TextView)view.findViewById(R.id.txt_id_summary_value);
			}else if(SwConst.SW_SHIFTWORKING_TYPE_TITLE.equals(itemType)){
				txtTitle = (TextView)view.findViewById(R.id.txt_id_title);
			}
		}
	}

	public ShiftWorkingAdapter(Context context, List<WorkHistoryModel> lstHistory){
		super(context, R.layout.item_shiftworking_shift_list, lstHistory);
		this.mContext = context;
		this.lstHistory = lstHistory;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		WorkHistoryModel model = this.lstHistory.get(position);
		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		BoardViewHolder holder = null;

		if(SwConst.SW_SHIFTWORKING_TYPE_SHIFT.equals(model.itemType)){
			convertView = mInflater.inflate(R.layout.item_shiftworking_shift_list, null);
			holder = new BoardViewHolder(convertView, model.itemType);

			holder.txtWorkDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_WEEKDAY, CCDateUtil.makeDateCustom(model.workDate, WelfareConst.WF_DATE_TIME)));
			if(model.project != null){
                holder.txtLocation.setVisibility(View.VISIBLE);
                holder.txtLocation.setText(model.project.projectLocation);
            }else{
                holder.txtLocation.setVisibility(View.GONE);
            }

            if(CCStringUtil.isEmpty(model.startShift) && CCStringUtil.isEmpty(model.endShift)){
                holder.txtWorkShift.setVisibility(View.GONE);
            }else{
                holder.txtWorkShift.setVisibility(View.VISIBLE);
                holder.txtWorkShift.setText(WelfareFormatUtil.connect2String(model.startShift, model.endShift, "-"));
            }

            if(CCStringUtil.isEmpty(model.startCheckin) && CCStringUtil.isEmpty(model.endCheckin)){
                holder.txtCheckIn.setVisibility(View.GONE);
            }else{
                holder.txtCheckIn.setVisibility(View.VISIBLE);
                holder.txtCheckIn.setText(WelfareFormatUtil.connect2String(model.startCheckin, model.endCheckin, "-"));
            }

            if(CCStringUtil.isEmpty(model.startWorking) && CCStringUtil.isEmpty(model.endWorking)){
                holder.txtWorktime.setVisibility(View.GONE);
            }else{
                holder.txtWorktime.setVisibility(View.VISIBLE);
                holder.txtWorktime.setText(WelfareFormatUtil.connect2String(model.startWorking, model.endWorking, "-"));
            }
		}else if(SwConst.SW_SHIFTWORKING_TYPE_SUMMARY.equals(model.itemType)){
			convertView = mInflater.inflate(R.layout.item_shiftworking_summary_list, null);
			holder = new BoardViewHolder(convertView, model.itemType);

            holder.txtTitle.setText(model.title);
            holder.txtValue.setText(model.value);
		}else if(SwConst.SW_SHIFTWORKING_TYPE_TITLE.equals(model.itemType)){
			convertView = mInflater.inflate(R.layout.item_shiftworking_title_list, null);
			holder = new BoardViewHolder(convertView, model.itemType);
            holder.txtTitle.setText(model.title);
		}

		return convertView;
	}

	public void clearAll(){
		this.lstHistory.clear();
		this.notifyDataSetChanged();
	}
}
