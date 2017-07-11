package trente.asia.thankscard.services.common;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
		buildLayoutSender(currentHistory);
	}

	public void buildBodyLayout(){
		super.buildBodyLayout();
	}

	private void buildLayoutSender(final HistoryModel historyModel){
		TextView txtSend = (TextView)getView().findViewById(R.id.txt_fragment_tc_detail_send);
		TextView txtSenderName = (TextView)getView().findViewById(R.id.txt_sender_name);
		ImageView senderAvatar = (ImageView)getView().findViewById(R.id.img_sender_avatar);
		ImageView imgSeal = (ImageView) getView().findViewById(R.id.img_seal);

		if(myself.key.equals(historyModel.receiverId) && getTitle() == R.string.fragment_tc_detail_title_receive){
			txtSend.setVisibility(View.VISIBLE);
			txtSend.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					gotoPostedEditFragment(historyModel);
				}
			});
		}else{
			txtSend.setVisibility(View.INVISIBLE);
		}

		if (historyModel.isSecret) {
			if (myself.key.equals(historyModel.receiverId) ||
					myself.key.equals(historyModel.posterId)) {
				adapter.showMessage();
				imgSeal.setVisibility(View.VISIBLE);
			} else {
				adapter.hideMessage();
				imgSeal.setVisibility(View.INVISIBLE);
			}
		} else {
			adapter.showMessage();
			imgSeal.setVisibility(View.INVISIBLE);
		}
		log("buildLayoutSender");
		txtSenderName.setText(historyModel.posterName);
		Picasso.with(getContext()).load(BuildConfig.HOST + historyModel.posterAvatarPath).fit().into(senderAvatar);
	}

	private void log(String msg) {
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
