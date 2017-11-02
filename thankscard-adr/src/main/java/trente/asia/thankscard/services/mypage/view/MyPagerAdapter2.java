package nguyenhoangviet.vpcorp.thankscard.services.mypage.view;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import nguyenhoangviet.vpcorp.thankscard.R;
import nguyenhoangviet.vpcorp.thankscard.services.common.model.HistoryModel;
import nguyenhoangviet.vpcorp.thankscard.services.posted.view.PhotoViewDetail;
import nguyenhoangviet.vpcorp.thankscard.utils.TCUtil;

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
		public ImageView		imgTemplate;
		public ImageView		imgSecret;
		public PhotoViewDetail photoViewDetail;
		public RelativeLayout	layoutCard;

		public HistoryViewHolder(View view){
			imgTemplate = (ImageView)view.findViewById(R.id.img_card);
			imgSecret = (ImageView)view.findViewById(R.id.img_secret);
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

		if(model.template != null){
			TCUtil.loadImageWithGlide(model.template.templateUrl, viewHolder.imgTemplate);
		}

		if(hideMessage){
			viewHolder.imgSecret.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imgSecret.setVisibility(View.INVISIBLE);
		}
		container.addView(view);

		if("NM".equals(model.templateType)){

		}else{
			if(model.attachment != null && model.attachment.fileUrl != null){
				viewHolder.photoViewDetail.restoreImage(model.attachment.fileUrl, Float.valueOf(model.photoLocationX),
						Float.valueOf(model.photoLocationY), Float.valueOf(model.photoScale));
			}
		}



		return view;
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
