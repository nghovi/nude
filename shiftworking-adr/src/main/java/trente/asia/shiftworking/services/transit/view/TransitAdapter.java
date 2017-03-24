package trente.asia.shiftworking.services.transit.view;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asia.chiase.core.util.CCFormatUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.services.transit.model.TransitModel;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;

/**
 * TransitAdapter.
 *
 * @author TrungND
 */
public class TransitAdapter extends ArrayAdapter<TransitModel>{

	private List<TransitModel>	lstTransit;
	private Context				mContext;

	public class BoardViewHolder{

		public ImageView	imgTransitType;
		// public ImageView imgWayType;

		public TextView		txtLeave;
		public TextView		txtDate;
		public TextView		txtArrive;
		public TextView		txtFee;

		public BoardViewHolder(View view){
			txtLeave = (TextView)view.findViewById(R.id.txt_id_leave);
			txtDate = (TextView)view.findViewById(R.id.txt_id_transit_date);
			txtArrive = (TextView)view.findViewById(R.id.txt_id_arrive);
			txtFee = (TextView)view.findViewById(R.id.txt_id_fee);

			imgTransitType = (ImageView)view.findViewById(R.id.img_id_transit_type);
			// imgWayType = (ImageView)view.findViewById(R.id.img_id_way_type);
		}
	}

	public TransitAdapter(Context context, List<TransitModel> lstTransit){
		super(context, R.layout.item_transit_list, lstTransit);
		this.mContext = context;
		this.lstTransit = lstTransit;
	}

	public View getView(int position, View convertView, ViewGroup parent){

		TransitModel model = this.lstTransit.get(position);

		LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_transit_list, null);
		BoardViewHolder holder = new BoardViewHolder(convertView);

		holder.txtLeave.setText(model.transLeave);
		Date transDate = WelfareUtil.makeDate(model.transDate);
		holder.txtDate.setText(CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, transDate));
		holder.txtArrive.setText(model.transArrive);
		holder.txtFee.setText(CCFormatUtil.formatAmount(model.fee));

		if(SwConst.SW_TRANSIT_TYPE_BUS.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_ONE_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_bus1way);
		}else if(SwConst.SW_TRANSIT_TYPE_TAXI.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_ONE_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_taxi1way);
		}else if(SwConst.SW_TRANSIT_TYPE_TRAIN.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_ONE_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_train1way);
		}else if(SwConst.SW_TRANSIT_TYPE_OTHER.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_ONE_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_other1way);
		}else if(SwConst.SW_TRANSIT_TYPE_BUS.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_TWO_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_bus2way);
		}else if(SwConst.SW_TRANSIT_TYPE_TAXI.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_TWO_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_taxi2way);
		}else if(SwConst.SW_TRANSIT_TYPE_TRAIN.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_TWO_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_train2way);
		}else if(SwConst.SW_TRANSIT_TYPE_OTHER.equals(model.transType) && SwConst.SW_TRANSIT_WAY_TYPE_TWO_WAY.equals(model.wayType)){
			holder.imgTransitType.setImageResource(R.drawable.sw_icon_transit_other2way);
		}

		return convertView;
	}

	public void clearAll(){
		this.lstTransit.clear();
		this.notifyDataSetChanged();
	}
}
