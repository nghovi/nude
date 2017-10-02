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
import trente.asia.shiftworking.common.interfaces.OnOvertimeAdapterListener;
import trente.asia.shiftworking.services.requests.model.OvertimeRequestModel;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author TrungND
 */
public class OvertimeAdapter extends ArrayAdapter<OvertimeRequestModel>{

	private List<OvertimeRequestModel>		otRequestModels;
	private Context						mContext;
	private int							layoutId;
	private String						type;
	private OnOvertimeAdapterListener	callback;

	public void setType(String type){
		this.type = type;
	}

	public void setCallback(OnOvertimeAdapterListener callback){
		this.callback = callback;
	}

	public OvertimeAdapter(Context context, List<OvertimeRequestModel> otRequestModels){
		super(context, R.layout.item_offer, otRequestModels);
		this.mContext = context;
		this.layoutId = R.layout.item_offer;
		this.otRequestModels = otRequestModels;
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
		final OvertimeRequestModel otRequest = getItem(position);
		WfPicassoHelper.loadImageWithDefaultIcon(mContext, BuildConfig.HOST, viewHolder.imgAvatar, otRequest.userAvatarPath, R.drawable.wf_profile);
		viewHolder.txtUsername.setText(otRequest.userName);
		viewHolder.txtDate.setText(otRequest.startDateString);
		viewHolder.txtType.setText(otRequest.startTimeString + "-" + otRequest.endTimeString);
		viewHolder.txtStatus.setText(otRequest.offerStatusName);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				if(callback != null){
					callback.onOvertimeAdapterClick(otRequest, type);
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
