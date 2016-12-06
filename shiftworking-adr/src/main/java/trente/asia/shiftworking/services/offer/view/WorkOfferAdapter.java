package trente.asia.shiftworking.services.offer.view;

import java.util.List;
import java.util.Map;

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
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.services.offer.model.WorkOffer;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author TrungND
 */
public class WorkOfferAdapter extends ArrayAdapter<WorkOffer>{

	private List<WorkOffer>		lstHistory;
	private Context				mContext;
	private int					layoutId;
	private Map<String, String>	offerTypesMaster;
	private Map<String, String>	offerStatusMaster;

	public WorkOfferAdapter(Context context, List<WorkOffer> lstHistory, Map<String, String> types, Map<String, String> statuses){
		super(context, R.layout.item_offer, lstHistory);
		this.mContext = context;
		this.layoutId = R.layout.item_offer;
		this.lstHistory = lstHistory;
		this.offerTypesMaster = types;
		this.offerStatusMaster = statuses;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(layoutId, null);
			viewHolder = new ViewHolder();
			viewHolder.imgAvatar = (ImageView)convertView.findViewById(R.id.img_item_offer_avatar);
			viewHolder.txtUsername = (TextView)convertView.findViewById(R.id.txt_item_offer_username);
			viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_item_offer_date);
			viewHolder.txtType = (TextView)convertView.findViewById(R.id.txt_item_offer_type);
			viewHolder.txtStatus = (TextView)convertView.findViewById(R.id.txt_item_offer_status);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		WorkOffer offer = getItem(position);
		WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, viewHolder.imgAvatar, offer.userAvatarPath, R.drawable.wf_profile);
		viewHolder.txtUsername.setText(offer.userName);
		viewHolder.txtDate.setText(offer.startDateString);
		viewHolder.txtType.setText(this.offerTypesMaster.get(offer.offerType));
		viewHolder.txtStatus.setText(this.offerStatusMaster.get(offer.approveResult));
		// convertView.findViewById(R.id.lnr_item_offer_main)
		// .setBackgroundResource(getBackgroundColor(offer));

		return convertView;
	}

	// private int getBackgroundColor(WorkOffer offer) {
	// if (offer.status == WorkOffer.OFFER_STATUS_OFFERING) {
	// return R.color.core_white;
	// } else if (offer.status == WorkOffer.OFFER_STATUS_APPROVING) {
	// return R.color.chiase_blue;
	// } else if (offer.status == WorkOffer.OFFER_STATUS_DISABLED) {
	// return R.color.core_silver;
	// }
	// return R.color.chiase_filechooser_txt_actived;
	// }

	private class ViewHolder{

		TextView	txtUsername;
		TextView	txtDate;
		TextView	txtType;
		TextView	txtStatus;
		ImageView	imgAvatar;
	}
}
