package trente.asia.shiftworking.services.offer.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.services.offer.model.WorkOffer;

/**
 * ShiftWorkingAdapter.
 *
 * @author TrungND
 */
public class WorkOfferAdapter extends ArrayAdapter<WorkOffer>{

	private List<WorkOffer>	lstHistory;
	private Context					mContext;
	private int layoutId;

	public WorkOfferAdapter(Context context, List<WorkOffer> lstHistory){
		super(context, R.layout.item_offer, lstHistory);
		this.mContext = context;
		this.layoutId = R.layout.item_offer;
		this.lstHistory = lstHistory;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;// ViewHolder pattern http://developer.android
		// .com/training/improving-layouts/smooth-scrolling.html
		if (convertView == null) {
			LayoutInflater vi =
					(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			// // TODO: 5/13/2016
			viewHolder.txtUsername =
					(TextView) convertView.findViewById(R.id.txt_item_offer_username);
			viewHolder.txtDateStart =
					(TextView) convertView.findViewById(R.id.txt_item_offer_date_from);
			viewHolder.txtDateEnd =
					(TextView) convertView.findViewById(R.id.txt_item_offer_date_to);
			viewHolder.txtType = (TextView) convertView.findViewById(R.id.txt_item_offer_type);
			viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.txt_item_offer_status);
			viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txt_item_offer_number);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		WorkOffer offer = getItem(position);
		viewHolder.txtNumber.setText(String.valueOf(position + 1));
		viewHolder.txtUsername.setText(offer.targetUserName);
		viewHolder.txtDateStart.setText(offer.startDateString);
		viewHolder.txtDateEnd.setText(offer.endDateString);
		viewHolder.txtType.setText(SwConst.offerTypes.get(offer.offerType) + "/"
				+ SwConst.subTypes.get(offer.offerType).get(offer.subType));
		viewHolder.txtStatus.setText(SwConst.offerStatus.get(offer.status));
		convertView.findViewById(R.id.lnr_item_offer_main).setBackgroundResource(
				getBackgroundColor(offer));

		return convertView;
	}

	private int getBackgroundColor(WorkOffer offer) {
		if (offer.status == WorkOffer.OFFER_STATUS_OFFERING) {
			return R.color.core_white;
		} else if (offer.status == WorkOffer.OFFER_STATUS_APPROVING) {
			return R.color.chiase_blue;
		} else if (offer.status == WorkOffer.OFFER_STATUS_DISABLED) {
			return R.color.core_silver;
		}
		return R.color.chiase_filechooser_txt_actived;
	}

	private class ViewHolder {

		TextView txtUsername;
		TextView txtDateStart;
		TextView txtDateEnd;
		TextView txtType;
		TextView txtStatus;
		TextView txtNumber;
	}
}
