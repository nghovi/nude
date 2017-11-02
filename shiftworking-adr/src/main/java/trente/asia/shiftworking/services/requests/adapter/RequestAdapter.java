package nguyenhoangviet.vpcorp.shiftworking.services.requests.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.shiftworking.BuildConfig;
import nguyenhoangviet.vpcorp.shiftworking.R;
import nguyenhoangviet.vpcorp.shiftworking.common.interfaces.OnRequestAdapterListener;
import nguyenhoangviet.vpcorp.welfare.adr.models.VacationRequestModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * ShiftWorkingAdapter.
 *
 * @author TrungND
 */
public class RequestAdapter extends ArrayAdapter<VacationRequestModel>{

	private List<VacationRequestModel>	lstHistory;
	private Context						mContext;
	private int							layoutId;
	private OnRequestAdapterListener callback;
	private String						type;

	public void setType(String type){
		this.type = type;
	}

	public void setCallback(OnRequestAdapterListener callback){
		this.callback = callback;
	}

	public RequestAdapter(Context context, List<VacationRequestModel> lstHistory){
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
					callback.onRequestAdapterClick(vactionRequest, type);
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
