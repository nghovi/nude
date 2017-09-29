package trente.asia.shiftworking.services.requests.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import trente.asia.shiftworking.BuildConfig;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.interfaces.OnVacationAdapterListener;
import trente.asia.welfare.adr.models.VacationRequestModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author TrungND
 */
public class VacationAdapter extends ArrayAdapter<VacationRequestModel>{

	private List<VacationRequestModel>	lstHistory;
	private Context						mContext;
	private int							layoutId;
	private OnVacationAdapterListener	callback;
	private String						type;

	public void setType(String type){
		this.type = type;
	}

	public void setCallback(OnVacationAdapterListener callback){
		this.callback = callback;
	}

	public VacationAdapter(Context context, List<VacationRequestModel> lstHistory){
		super(context, R.layout.item_offer, lstHistory);
		this.mContext = context;
		this.layoutId = R.layout.item_offer;
		this.lstHistory = lstHistory;
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
		final VacationRequestModel vactionRequest = getItem(position);
		WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, viewHolder.imgAvatar, vactionRequest.userAvatarPath, R.drawable.wf_profile);
		viewHolder.txtUsername.setText(vactionRequest.userName);
		viewHolder.txtDate.setText(vactionRequest.startDateString);
		viewHolder.txtType.setText(vactionRequest.vacationName);
		viewHolder.txtStatus.setText(vactionRequest.offerStatusName);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(callback != null){
					callback.onVationAdapterClick(vactionRequest, type);
				}
			}
		});
		return convertView;
	}

	private class ViewHolder{

		TextView	txtUsername;
		TextView	txtDate;
		TextView	txtType;
		TextView	txtStatus;
		ImageView	imgAvatar;
	}
}
