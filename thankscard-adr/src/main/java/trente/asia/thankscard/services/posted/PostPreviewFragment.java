package trente.asia.thankscard.services.posted;

import java.util.Date;
import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.util.TypedValue;
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
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.common.AbstractPagerFragment;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.mypage.model.StampModel;
import trente.asia.thankscard.services.posted.model.ApiStickerModel;
import trente.asia.thankscard.services.posted.view.StickerViewDetail;
import trente.asia.thankscard.utils.TCUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public class PostPreviewFragment extends AbstractPagerFragment{

	public static final String	DETAIL_TC_TITLE			= "DETAIL_TC_TITLE";
	public static final String	DETAIL_TC_DEFAULT_POS	= "DETAIL_TC_DEFAULT_POS";

	private HistoryModel		currentHistory;
	private List<DeptModel>		depts;

	private float				normalTextSize;
	private float				photoTextSize;

	public void setDepts(List<DeptModel> depts){
		this.depts = depts;
	}

	public void setCurrentHistory(HistoryModel currentHistory){
		this.currentHistory = currentHistory;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
		PreferencesSystemUtil preference = new PreferencesSystemUtil(getContext());
		normalTextSize = Float.parseFloat(preference.get(TcConst.PREF_NORMAL_TEXT_SIZE));
		photoTextSize = Float.parseFloat(preference.get(TcConst.PREF_PHOTO_TEXT_SIZE));
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_post_preview;
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public int getTitle(){
		return R.string.tc_preview;
	}

	@Override
	protected void initView() {
		super.initView();
		initHeader(null, getString(R.string.tc_preview), null);
	}

	@Override
	public void initData(){

	}

	@Override
	protected void onPageHistorySelected(int position){
	}

	private void buildTextMessage(HistoryModel historyModel){
		LinearLayout lnrMessage = (LinearLayout)getView().findViewById(R.id.lnr_message);
		TextView textMessage = (TextView)getView().findViewById(R.id.text_message);
		TextView textDate = (TextView)getView().findViewById(R.id.txt_tc_detail_date);
		TextView textTo = (TextView)getView().findViewById(R.id.txt_tc_detail_to);
		ImageView imgCard = (ImageView)getView().findViewById(R.id.img_card);
		ImageView photoView = (ImageView)getView().findViewById(R.id.layout_photo);
		TCUtil.loadImageWithGlide(historyModel.template.templateUrl, imgCard);
		textMessage.setText(historyModel.message);
		Date postDate = CCDateUtil.makeDateCustom(historyModel.postDate, WelfareConst.WF_DATE_TIME);
		String postDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, postDate);
		textDate.setText(postDateFormat);
		textTo.setText(getString(R.string.fragment_tc_detail_to, historyModel.receiverName));

		boolean isSecret = false;
		if(historyModel.isSecret && !myself.key.equals(historyModel.receiverId) && !myself.key.equals(historyModel.posterId)){
			isSecret = true;
		}

		photoView.setImageBitmap(null);

		textMessage.setTypeface(Typeface.MONOSPACE);
		if("NM".equals(historyModel.templateType)){
			setLayoutMessageCenter(lnrMessage);
			textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, normalTextSize);
		}else{
			setLayoutMessageRight(lnrMessage);
			textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoTextSize);
			if(historyModel.attachment != null && historyModel.attachment.fileUrl != null && !isSecret){
				WfPicassoHelper.loadImage(getContext(), BuildConfig.HOST + historyModel.attachment.fileUrl, photoView, null);
			}
		}

		rltStickers.removeAllViews();
		if(!isSecret){
			restoreStickers(currentHistory.stickers);
		}
	}

	private void setLayoutMessageCenter(LinearLayout lnrMessage){
		PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		params.getPercentLayoutInfo().widthPercent = 1f;
		params.setMargins(WelfareUtil.dpToPx(60), WelfareUtil.dpToPx(76), WelfareUtil.dpToPx(60), WelfareUtil.dpToPx(60));
		lnrMessage.setLayoutParams(params);
	}

	private void setLayoutMessageRight(LinearLayout lnrMessage){
		PercentRelativeLayout.LayoutParams params = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_END);
		params.getPercentLayoutInfo().widthPercent = 0.5f;
		params.setMargins(0, WelfareUtil.dpToPx(36), 0, 0);
		lnrMessage.setLayoutParams(params);
	}

	private void restoreStickers(List<ApiStickerModel> stickers){
		for(ApiStickerModel sticker : stickers){
			StampModel stamp = StampModel.getStamp(Realm.getDefaultInstance(), sticker.stickerId);
			log("sticker id = " + sticker.stickerId);
			StickerViewDetail stickerViewDetail = new StickerViewDetail(getContext());
			rltStickers.addView(stickerViewDetail);
			stickerViewDetail.restoreSticker(stamp.stampPath, Float.valueOf(sticker.locationX), Float.valueOf(sticker.locationY), Float.valueOf(sticker.scale), Float.valueOf(sticker.degree));
		}
	}

	public void buildBodyLayout(){
	}

	@Override
	protected void buildPagerHeader() {

	}

	private void log(String msg){
		Log.e("TCDetail", msg);
	}

	public void setLstHistory(List<HistoryModel> lstHistory){
		this.lstHistory = lstHistory;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
