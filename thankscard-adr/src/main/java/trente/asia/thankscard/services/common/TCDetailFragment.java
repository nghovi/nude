package trente.asia.thankscard.services.common;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import io.realm.Realm;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.mypage.model.StampModel;
import trente.asia.thankscard.services.posted.PostTCFragment;
import trente.asia.thankscard.services.posted.ThanksCardEditFragment;
import trente.asia.thankscard.services.posted.model.ApiStickerModel;
import trente.asia.thankscard.services.posted.view.PhotoViewDetail;
import trente.asia.thankscard.services.posted.view.StickerViewDetail;
import trente.asia.thankscard.services.received.ReceiveTCListFragment;
import trente.asia.thankscard.utils.TCUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public class TCDetailFragment extends AbstractPagerFragment{

	public static final String	DETAIL_TC_TITLE			= "DETAIL_TC_TITLE";
	public static final String	DETAIL_TC_DEFAULT_POS	= "DETAIL_TC_DEFAULT_POS";

	private HistoryModel		currentHistory;
	private List<DeptModel>		depts;

	private int					normalTextSize;
	private int					photoTextSize;

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
		normalTextSize = Integer.parseInt(preference.get(TcConst.PREF_NORMAL_TEXT_SIZE));
		photoTextSize = Integer.parseInt(preference.get(TcConst.PREF_PHOTO_TEXT_SIZE));
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_tc_detail;
	}

	@Override
	public int getFooterItemId(){
		Bundle args = getArguments();
		if(args != null){
			return args.getInt(TcConst.ACTIVE_FOOTER_ITEM_ID, 0);
		}else{
			return R.id.lnr_view_common_footer_receive;
		}
	}

	@Override
	public int getTitle(){
		Bundle args = getArguments();
		if(args != null){
			return args.getInt(DETAIL_TC_TITLE, R.string.fragment_tc_detail_title_pos);
		}
		return R.string.fragment_tc_detail_title_pos;
	}

	@Override
	public void initData(){
		if(isClickNotification){
			loadHistory();
		}
	}

	private void loadHistory(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("targetMonth", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, Calendar.getInstance().getTime()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(TcConst.API_GET_POST_CARD_HISTORY, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(TcConst.API_GET_POST_CARD_HISTORY.equals(url)){
			lstHistory = CCJsonUtil.convertToModelList(response.optString("histories"), HistoryModel.class);
			depts = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			defaultPos = TCUtil.findHistory4Key(lstHistory, currentHistory.key);
			onPageHistorySelected(defaultPos);
			buildEnabledButtons();
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	protected void onPageHistorySelected(int position){
		currentHistory = this.lstHistory.get(position);
		buildLayoutSender(currentHistory);
		buildTextMessage(currentHistory);
	}

	private void buildTextMessage(HistoryModel historyModel){
		LinearLayout lnrMessage = (LinearLayout)getView().findViewById(R.id.lnr_message);
		TextView textMessage = (TextView)getView().findViewById(R.id.text_message);
		TextView textDate = (TextView)getView().findViewById(R.id.txt_tc_detail_date);
		TextView textTo = (TextView)getView().findViewById(R.id.txt_tc_detail_to);
		ImageView imgCard = (ImageView) getView().findViewById(R.id.img_card);
		PhotoViewDetail photoView = (PhotoViewDetail) getView().findViewById(R.id.layout_photo);
		TCUtil.loadImageWithGlide(historyModel.template.templateUrl, imgCard);
		textMessage.setText(historyModel.message);
		Date postDate = CCDateUtil.makeDateCustom(historyModel.postDate, WelfareConst.WF_DATE_TIME);
		String postDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, postDate);
		textDate.setText(postDateFormat);
		textTo.setText(getString(R.string.fragment_tc_detail_to, historyModel.receiverName));

		boolean isSecret = false;
		if(historyModel.isSecret && !myself.key.equals(historyModel.receiverId) && !myself.key.equals(historyModel.posterId)) {
				isSecret = true;
		}

		if("NM".equals(historyModel.templateType)){
			setLayoutMessageCenter(lnrMessage);
			textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, normalTextSize);
		}else{
			setLayoutMessageRight(lnrMessage);
			textMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoTextSize);
			photoView.clearImage();
			if(historyModel.attachment != null && historyModel.attachment.fileUrl != null && !isSecret){
				photoView.restoreImage(historyModel.attachment.fileUrl, Float.valueOf(historyModel.photoLocationX),
						Float.valueOf(historyModel.photoLocationY), Float.valueOf(historyModel.photoScale));
			}
		}

		rltStickers.removeAllViews();
		if (!isSecret) {
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
			StickerViewDetail stickerViewDetail = new StickerViewDetail(getContext());
			rltStickers.addView(stickerViewDetail);
			stickerViewDetail.restoreSticker(stamp.stampPath, Float.valueOf(sticker.locationX), Float.valueOf(sticker.locationY), Float.valueOf(sticker.scale), Float.valueOf(sticker.degree));
		}
	}

	public void buildBodyLayout(){
		super.buildBodyLayout();
	}

	private void buildLayoutSender(final HistoryModel historyModel){
		TextView txtSend = (TextView)getView().findViewById(R.id.txt_fragment_tc_detail_send);
		TextView txtSenderName = (TextView)getView().findViewById(R.id.txt_sender_name);
		ImageView senderAvatar = (ImageView)getView().findViewById(R.id.img_sender_avatar);
		ImageView imgSeal = (ImageView)getView().findViewById(R.id.img_seal);
		TextView textMessage = (TextView)getView().findViewById(R.id.text_message);
		ImageView imgSecret = (ImageView) getView().findViewById(R.id.img_secret);
		ImageView imgStage = (ImageView) getView().findViewById(R.id.stage_icon);

		if(myself.key.equals(historyModel.receiverId) && getTitle() == R.string.fragment_tc_detail_title_receive){
			txtSend.setVisibility(View.VISIBLE);
			txtSend.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					// gotoPostedEditFragment(historyModel);
					PostTCFragment postTCFragment = new PostTCFragment();
					postTCFragment.setSelectedDepartment(WelfareUtil.getDept4UserId(depts, historyModel.posterId));
					postTCFragment.setSelectedUser(new UserModel(historyModel.posterId, historyModel.posterName));
					gotoFragment(postTCFragment);
				}
			});
		}else{
			txtSend.setVisibility(View.INVISIBLE);
		}

		if(historyModel.isSecret){
			if(myself.key.equals(historyModel.receiverId) || myself.key.equals(historyModel.posterId)){
				imgSeal.setVisibility(View.VISIBLE);
				textMessage.setVisibility(View.VISIBLE);
				imgSecret.setVisibility(View.INVISIBLE);
			}else{
				imgSeal.setVisibility(View.INVISIBLE);
				textMessage.setVisibility(View.INVISIBLE);
				imgSecret.setVisibility(View.VISIBLE);
			}
		}else{
			imgSeal.setVisibility(View.INVISIBLE);
			textMessage.setVisibility(View.VISIBLE);
			imgSecret.setVisibility(View.INVISIBLE);
		}
		txtSenderName.setText(historyModel.posterName);

		Glide.with(getContext()).load(BuildConfig.HOST + historyModel.posterAvatarPath).into(senderAvatar);

		int pointBronze = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_BRONZE));
		int pointSilver = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_SILVER));
		int pointGold = Integer.parseInt(prefAccUtil.get(TcConst.PREF_POINT_GOLD));

		if (historyModel.posterTotalPoint < pointBronze) {
			imgStage.setImageResource(R.drawable.tc_ranking_medal4);
		} else if (historyModel.posterTotalPoint < pointSilver) {
			imgStage.setImageResource(R.drawable.tc_ranking_medal3);
		} else if (historyModel.posterTotalPoint < pointGold) {
			imgStage.setImageResource(R.drawable.tc_ranking_medal2);
		} else {
			imgStage.setImageResource(R.drawable.tc_ranking_medal1);
		}
	}

	private void log(String msg){
		Log.e("TCDetail", msg);
	}

	private void gotoPostedEditFragment(HistoryModel historyModel){
		ThanksCardEditFragment thanksCardEditFragment = new ThanksCardEditFragment();
		Bundle args = new Bundle();
		args.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, getFooterItemId());
		String toUserId = myself.key.equals(historyModel.receiverId) ? historyModel.posterId : historyModel.receiverId;
		args.putString(ThanksCardEditFragment.MESSAGE, historyModel.message);
		thanksCardEditFragment.setSelectedDept(WelfareUtil.getDept4UserId(depts, toUserId));
		thanksCardEditFragment.setSelectedUser(new UserModel(toUserId));

		thanksCardEditFragment.setArguments(args);
		gotoFragment(thanksCardEditFragment);
	}

	public static void buildTCFrame(Context context, View lnrFrame, final HistoryModel historyModel, boolean isShowContent){
		Date postDate = CCDateUtil.makeDateCustom(historyModel.postDate, WelfareConst.WF_DATE_TIME);
		String postDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE, postDate);
		String to = historyModel.receiverName;
		String from = historyModel.posterName;
		TextView txtDate = (TextView)lnrFrame.findViewById(R.id.txt_tc_detail_date);
		TextView txtTo = (TextView)lnrFrame.findViewById(R.id.txt_tc_detail_to);
		TextView txtFrom = (TextView)lnrFrame.findViewById(R.id.txt_tc_detail_from);

		if(!isShowContent){
			txtDate.setVisibility(View.INVISIBLE);
			txtTo.setVisibility(View.INVISIBLE);
			txtFrom.setVisibility(View.INVISIBLE);
		}else{
			txtDate.setText(context.getResources().getString(R.string.fragment_tc_detail_date, postDateFormat));
			txtTo.setText(context.getResources().getString(R.string.fragment_tc_detail_to, to));
			txtFrom.setText(context.getResources().getString(R.string.fragment_tc_detail_from, from));
		}

		if(lnrFrame.findViewById(R.id.txt_tc_detail_message) != null){
			TextView txtMessageDetail = (TextView)lnrFrame.findViewById(R.id.txt_tc_detail_message);
			txtMessageDetail.setText(historyModel.message);
			txtMessageDetail.setMovementMethod(new ScrollingMovementMethod());
		}

		if(historyModel.template != null){
			LinearLayout lnrFrameBackground = (LinearLayout)lnrFrame.findViewById(R.id.lnr_thanks_card_frame_container);
			ImageView templateImage = (ImageView)lnrFrame.findViewById(R.id.img_card);
			templateImage.setScaleType(ImageView.ScaleType.FIT_XY);
			WfPicassoHelper.loadImage2(context, BuildConfig.HOST, templateImage, historyModel.template.templateUrl);
//			if ("PH".equals(historyModel.templateType)) {
//				PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) lnrFrameBackground.getLayoutParams();
//				params.getPercentLayoutInfo().widthPercent = 0.5f;
//				params.addRule(RelativeLayout.ALIGN_PARENT_END);
//				params.leftMargin = 10;
//				PhotoViewDetail photoViewDetail = (PhotoViewDetail) lnrFrame.findViewById(R.id.photo);
//				if(historyModel.attachment != null && historyModel.attachment.fileUrl != null){
//					photoViewDetail.restoreImageInList(historyModel.attachment.fileUrl, Float.valueOf(historyModel.photoLocationX),
//							Float.valueOf(historyModel.photoLocationY), Float.valueOf(historyModel.photoScale));
//				}
//			}
		}
	}

	@Override
	public void buildPagerHeader(){
		if(!isClickNotification){
			Bundle args = getArguments();
			defaultPos = args != null ? args.getInt(DETAIL_TC_DEFAULT_POS, 0) : 0;
			buildEnabledButtons();
			onPageHistorySelected(defaultPos);
		}
	}

	public void setLstHistory(List<HistoryModel> lstHistory){
		this.lstHistory = lstHistory;
	}

	@Override
	protected void onClickBackBtn(){
		if(isClickNotification){
			emptyBackStack();
			gotoFragment(new ReceiveTCListFragment());
		}else{
			super.onClickBackBtn();
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
