package nguyenhoangviet.vpcorp.thankscard.services.rank.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nguyenhoangviet.vpcorp.thankscard.BuildConfig;
import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.services.rank.model.RankStage;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 3/16/2016.
 */
public class RankStageListAdapter extends ArrayAdapter<RankStage>{

	private Context	context;
	private int		resourceId;
	private int		mSelectedPosition;

	// private int totalPost = 0;

	public RankStageListAdapter(Context context, int resource, List<RankStage> objects){
		super(context, resource, objects);
		this.context = context;
		this.resourceId = resource;
		this.mSelectedPosition = 0;
	}

	public int getSelectedPosition(){
		return mSelectedPosition;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder;// ViewHolder pattern
								// http://developer.android.com/training/improving-layouts/smooth-scrolling.html
		if(convertView == null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.txtStageName = (TextView)convertView.findViewById(R.id.txt_item_dialog_rank_stage_name);
			viewHolder.txtStageRequire = (TextView)convertView.findViewById(R.id.txt_item_dialog_rank_stage_require);
			viewHolder.txtStageStatus = (TextView)convertView.findViewById(R.id.txt_item_dialog_rank_stage_status);
			viewHolder.imgStageImage = (ImageView)convertView.findViewById(R.id.img_item_dialog_rank_stage);
			viewHolder.lnrStageStatus = (LinearLayout)convertView.findViewById(R.id.lnr_item_dialog_rank_stage_status);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		RankStage rankStage = getItem(position);
		if(rankStage.status == RankStage.RANK_STAGE_ACHIEVED){
			viewHolder.txtStageStatus.setText(context.getResources().getString(R.string.dialog_rank_stage_status_ok));
			viewHolder.lnrStageStatus.setBackgroundResource(R.drawable.tc_background_corner_pink);
		}else{
			viewHolder.txtStageStatus.setText(context.getResources().getString(R.string.dialog_rank_stage_status_not_ok));
			viewHolder.lnrStageStatus.setBackgroundResource(R.drawable.tc_background_corner_green);
		}

		viewHolder.txtStageName.setText(rankStage.rankName);
		viewHolder.txtStageRequire.setText(context.getResources().getString(R.string.dialog_rank_stage_require, rankStage.require));
		WfPicassoHelper.loadImage2(context, BuildConfig.HOST, viewHolder.imgStageImage, rankStage.url);
		viewHolder.imgStageImage.setScaleType(ImageView.ScaleType.FIT_XY);
		return convertView;
	}

	private class ViewHolder{

		TextView		txtStageName;
		TextView		txtStageStatus;
		TextView		txtStageRequire;
		ImageView		imgStageImage;
		LinearLayout	lnrStageStatus;
	}
}
