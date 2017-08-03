package trente.asia.thankscard.services.mypage.view;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import io.realm.Realm;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.mypage.model.StampModel;
import trente.asia.thankscard.services.posted.model.ApiStickerModel;
import trente.asia.thankscard.services.posted.view.PhotoViewDetail;
import trente.asia.thankscard.services.posted.view.StickerViewDetail;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * MyPagerAdapter2
 * Created by viet on 2/18/2016.
 */
public class MyPagerAdapter2 extends PagerAdapter{

	private Context				mContext;
	private List<HistoryModel>	lstHistory;
	private LayoutInflater		mInflater;
	private boolean				hideMessage;

	public class HistoryViewHolder{

		public TextView			txtTo;
		public TextView			txtFrom;
		public TextView			txtDate;

		public TextView			txtMessage;
		public ImageView		imgTemplate;
		public ImageView		imgSecret;
		public LinearLayout		lnrMessage;
		public PhotoViewDetail photoViewDetail;
		public RelativeLayout	layoutCard;

		public HistoryViewHolder(View view){
			txtTo = (TextView)view.findViewById(R.id.txt_tc_detail_to);
			txtFrom = (TextView)view.findViewById(R.id.txt_tc_detail_from);
			txtDate = (TextView)view.findViewById(R.id.txt_tc_detail_date);

			txtMessage = (TextView)view.findViewById(R.id.txt_tc_detail_message);
			imgTemplate = (ImageView)view.findViewById(R.id.img_item_thanks_card_frame);
			imgSecret = (ImageView)view.findViewById(R.id.img_secret);
			lnrMessage = (LinearLayout)view.findViewById(R.id.lnr_thanks_card_frame_container);
			photoViewDetail = (PhotoViewDetail)view.findViewById(R.id.layout_photo);
			layoutCard = (RelativeLayout) view.findViewById(R.id.layout_card);
		}
	}

	public MyPagerAdapter2(Context context, List<HistoryModel> lstHistory){
		this.mContext = context;
		this.lstHistory = lstHistory;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void showMessage(){
		hideMessage = false;
		notifyDataSetChanged();
	}

	public void hideMessage(){
		hideMessage = true;
		notifyDataSetChanged();
	}

	/**
	 * @return the number of pages to display
	 */
	@Override
	public int getCount(){
		return lstHistory.size();
	}

	@Override
	public int getItemPosition(Object object){
		return POSITION_NONE;
	}

	/**
	 * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
	 * same object as the {@link android.view.View} added to the {@link android.support.v4.view.ViewPager}.
	 */
	@Override
	public boolean isViewFromObject(View view, Object o){
		return o == view;
	}

	/**
	 * Instantiate the {@link android.view.View} which should be displayed at {@code position}. Here we
	 * inflate a layout from the apps resources and then change the text view to signify the position.
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position){
		View view = mInflater.inflate(R.layout.page_tc_detail, null);
		HistoryViewHolder viewHolder = new HistoryViewHolder(view);
		HistoryModel model = lstHistory.get(position);

		Date postDate = CCDateUtil.makeDateCustom(model.postDate, WelfareConst.WF_DATE_TIME);
		String postDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, postDate);
		viewHolder.txtDate.setText(mContext.getResources().getString(R.string.fragment_tc_detail_date, postDateFormat));
		viewHolder.txtTo.setText(mContext.getResources().getString(R.string.fragment_tc_detail_to, model.receiverName));
		viewHolder.txtFrom.setText(mContext.getResources().getString(R.string.fragment_tc_detail_from, model.posterName));
		viewHolder.txtFrom.setVisibility(View.GONE);

		viewHolder.txtMessage.setText(model.message);
		viewHolder.txtMessage.setMovementMethod(new ScrollingMovementMethod());
		if(model.template != null){
			WfPicassoHelper.loadImage2(mContext, BuildConfig.HOST, viewHolder.imgTemplate, model.template.templateUrl);
		}

		if(hideMessage){
			viewHolder.txtMessage.setVisibility(View.INVISIBLE);
			viewHolder.imgSecret.setVisibility(View.VISIBLE);
		}else{
			viewHolder.txtMessage.setVisibility(View.VISIBLE);
			viewHolder.imgSecret.setVisibility(View.INVISIBLE);
		}
		container.addView(view);

		if("NM".equals(model.templateType)){
			setLayoutMessageCenter(viewHolder.lnrMessage);
		}else{
			if(model.attachment != null && model.attachment.fileUrl != null){
				viewHolder.photoViewDetail.restoreImage(model.attachment.fileUrl, Float.valueOf(model.photoLocationX),
						Float.valueOf(model.photoLocationY), Float.valueOf(model.photoScale));
			}
		}
		for (ApiStickerModel sticker : model.stickers) {
			StampModel stamp = StampModel.getStamp(Realm.getDefaultInstance(), sticker.stickerId);
			StickerViewDetail stickerViewDetail = new StickerViewDetail(mContext);
			viewHolder.layoutCard.addView(stickerViewDetail);
			stickerViewDetail.restoreSticker(stamp.stampPath, Float.valueOf(sticker.locationX), Float.valueOf(sticker.locationY),
					Float.valueOf(sticker.scale), Float.valueOf(sticker.degree));
		}
		return view;
	}

	private void setLayoutMessageCenter(LinearLayout lnrMessage){
		PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams)lnrMessage.getLayoutParams();
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.getPercentLayoutInfo().widthPercent = 1f;
		params.setMargins(WelfareUtil.dpToPx(50), WelfareUtil.dpToPx(40), WelfareUtil.dpToPx(50), WelfareUtil.dpToPx(40));
	}

	private void log(String msg){
		Log.e("MyPager", msg);
	}

	/**
	 * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the {@link android.view.View}.
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		container.removeView((View)object);
	}
}
