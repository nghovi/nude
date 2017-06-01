package trente.asia.thankscard.services.common;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.services.common.model.HistoryModel;
import trente.asia.thankscard.services.posted.ThanksCardEditFragment;
import trente.asia.thankscard.services.received.ReceiveTCListFragment;
import trente.asia.thankscard.utils.TCUtil;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.LikeModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public class TCDetailFragment extends AbstractPagerFragment{

	public static final String	DETAIL_TC_TITLE			= "DETAIL_TC_TITLE";
	public static final String	DETAIL_TC_DEFAULT_POS	= "DETAIL_TC_DEFAULT_POS";
	private TextView			txtLikeCount;
	private TextView			txtLikeText;
	private ImageView			imgLike;
	private LinearLayout		lnrLike;

	private List<HistoryModel>	lstHistory;
	private HistoryModel		currentHistory;
	private List<DeptModel>		depts;
	private int					defaultPos				= 0;

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
			jsonObject.put("categoryId", currentHistory.categoryId);
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
			loadPagerLayout(lstHistory, defaultPos, true);
		}else{
			super.successLoad(response, url);
		}
	}

	@Override
	protected void onPageHistorySelected(int position){
		currentHistory = this.lstHistory.get(position);
		buildLikeButton(currentHistory);
	}

	public void buildBodyLayout(){
		super.buildBodyLayout();
	}

	private void buildLikeButton(final HistoryModel historyModel){
		txtLikeCount = (TextView)getView().findViewById(R.id.txt_fragment_tc_detail_like_count);
		txtLikeText = (TextView)getView().findViewById(R.id.txt_fragment_tc_detail_like);
		imgLike = (ImageView)getView().findViewById(R.id.img_pate_tc_detail_like);
		lnrLike = (LinearLayout)getView().findViewById(R.id.lnr_fragment_tc_detail_like);
		TextView txtSend = (TextView)getView().findViewById(R.id.txt_fragment_tc_detail_send);
		if(!myself.key.equals(historyModel.receiverId) && !myself.key.equals(historyModel.posterId)){
			txtSend.setVisibility(View.INVISIBLE);
		}else{
			txtSend.setVisibility(View.VISIBLE);
			txtSend.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					gotoPostedEditFragment(historyModel);
				}
			});
		}

		updateLikeLayout();
	}

	private void gotoPostedEditFragment(HistoryModel historyModel){
		ThanksCardEditFragment thanksCardEditFragment = new ThanksCardEditFragment();
		Bundle args = new Bundle();
		args.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, getFooterItemId());
		String toUserId = myself.key.equals(historyModel.receiverId) ? historyModel.posterId : historyModel.receiverId;
		args.putString(ThanksCardEditFragment.MESSAGE, historyModel.message);//

		thanksCardEditFragment.setSelectedDept(WelfareUtil.getDept4UserId(depts, toUserId));
		thanksCardEditFragment.setSelectedUser(new UserModel(toUserId));

		thanksCardEditFragment.setArguments(args);
		gotoFragment(thanksCardEditFragment);
	}

	private void updateLikeLayout(){
		boolean alreadyLiked = false;
		if(!CCCollectionUtil.isEmpty(currentHistory.likeInfo)){
			for(LikeModel likeModel : currentHistory.likeInfo){
				UserModel liker = likeModel.likeUser;
				if(liker.key.equals(myself.key)){
					alreadyLiked = true;
					break;
				}
			}
		}

		txtLikeCount.setText(String.valueOf(WelfareUtil.size(currentHistory.likeInfo)));
		if(alreadyLiked){
			lnrLike.setOnClickListener(null);
			imgLike.setImageResource(R.drawable.tc_like_off);
			txtLikeText.setText(getString(R.string.fragment_tc_detail_liked));
			txtLikeText.setTextColor(Color.BLACK);
		}else{
			lnrLike.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view){
					sendLike(currentHistory);
				}
			});
			imgLike.setImageResource(R.drawable.tc_like_on);
			txtLikeText.setText(getString(R.string.fragment_tc_detail_like));
			txtLikeText.setTextColor(ContextCompat.getColor(getContext(), R.color.tc_like_text_disabled));
		}

		LinearLayout lnrFollowerContainer = (LinearLayout)getView().findViewById(R.id.lnr_follower_list);
		lnrFollowerContainer.removeAllViews();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		for(LikeModel likeModel : currentHistory.likeInfo){
			UserModel liker = likeModel.likeUser;
			LinearLayout lnrFollower = (LinearLayout)inflater.inflate(R.layout.item_follower, null);
			ImageView imgAvatar = (ImageView)lnrFollower.findViewById(R.id.img_item_follower_avatar);
			WfPicassoHelper.loadImage2(activity, host, imgAvatar, liker.avatarPath);
			TextView txtFollower = (TextView)lnrFollower.findViewById(R.id.txt_item_follower_name);
			txtFollower.setText(liker.userName);
			lnrFollowerContainer.addView(lnrFollower);
		}

	}

	private void sendLike(HistoryModel historyModel){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("postId", historyModel.key);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(TcConst.API_POST_LIKE, jsonObject, true);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		currentHistory = CCJsonUtil.convertToModel(response.optString("history"), HistoryModel.class);
		int currentItem = viewPager.getCurrentItem();
		lstHistory.set(currentItem, currentHistory);
		// adapter.rebuildData(wrapToPageModels(createPageModels()));
		updateLikeLayout();
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
			// TCUtil.picassaLoadBackgroundImage(context, lnrFrameBackground,
			// thanksCard.getTemplate().url);
			ImageView templateImage = (ImageView)lnrFrame.findViewById(R.id.img_item_thanks_card_frame);
			templateImage.setScaleType(ImageView.ScaleType.FIT_XY);
			WfPicassoHelper.loadImage2(context, BuildConfig.HOST, templateImage, historyModel.template.templateUrl);

		}
	}

	@Override
	public void buildPagerHeader(){
		if(!isClickNotification){
			Bundle args = getArguments();
			defaultPos = args != null ? args.getInt(DETAIL_TC_DEFAULT_POS, 0) : 0;
			loadPagerLayout(lstHistory, defaultPos, true);
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
		imgLike = null;
		txtLikeText = null;
		txtLikeCount = null;
		lnrLike = null;
	}

}
