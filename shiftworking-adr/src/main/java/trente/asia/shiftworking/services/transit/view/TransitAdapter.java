package trente.asia.shiftworking.services.transit.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.services.transit.model.TransitModel;
import trente.asia.welfare.adr.utils.WelfareFormatUtil;

/**
 * TransitAdapter.
 *
 * @author TrungND
 */
public class TransitAdapter extends ArrayAdapter<TransitModel>{

	private List<TransitModel>	lstTransit;
	private Context				mContext;

	public class BoardViewHolder{

		public TextView	txtLeave;
		public TextView	txtArrive;
		public TextView	txtWayType;
		public TextView	txtCostType;
		public TextView	txtTransitType;
		public TextView	txtFee;

		public BoardViewHolder(View view){
			txtLeave = (TextView)view.findViewById(R.id.txt_id_leave);
			txtArrive = (TextView)view.findViewById(R.id.txt_id_arrive);
			txtWayType = (TextView)view.findViewById(R.id.txt_id_way_type);
			txtCostType = (TextView)view.findViewById(R.id.txt_id_cost_type);
			txtTransitType = (TextView)view.findViewById(R.id.txt_id_transit_type);
			txtFee = (TextView)view.findViewById(R.id.txt_id_fee);
		}
	}

	public TransitAdapter(Context context, List<TransitModel> lstTransit){
		super(context, R.layout.item_shift_working_list, lstTransit);
		this.mContext = context;
		this.lstTransit = lstTransit;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		TransitModel model = this.lstTransit.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_transit_list, null);
		BoardViewHolder holder = new BoardViewHolder(convertView);

        holder.txtLeave.setText(WelfareFormatUtil.connect2String(mContext.getString(R.string.sw_work_transit_leave_item), model.transLeave, ":"));
        holder.txtArrive.setText(WelfareFormatUtil.connect2String(mContext.getString(R.string.sw_work_transit_arrive_item), model.transArrive, ":"));
        holder.txtWayType.setText(WelfareFormatUtil.connect2String(mContext.getString(R.string.sw_work_transit_way_type_item), model.wayTypeName, ":"));
        holder.txtCostType.setText(WelfareFormatUtil.connect2String(mContext.getString(R.string.sw_work_transit_cost_type_item), model.costTypeName, ":"));
        holder.txtTransitType.setText(WelfareFormatUtil.connect2String(mContext.getString(R.string.sw_work_transit_trans_type_item), model.transTypeName, ":"));
        holder.txtFee.setText(WelfareFormatUtil.connect2String(mContext.getString(R.string.sw_work_transit_fee_item), CCFormatUtil.formatAmount(model.fee), ":"));

		return convertView;
	}

	public void clearAll(){
		this.lstTransit.clear();
		this.notifyDataSetChanged();
	}
}
