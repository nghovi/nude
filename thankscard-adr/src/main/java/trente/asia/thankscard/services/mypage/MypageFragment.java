package trente.asia.thankscard.services.mypage;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.thankscard.R;
import trente.asia.thankscard.activities.MainActivity;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.thankscard.fragments.AbstractTCFragment;
import trente.asia.thankscard.fragments.dialogs.RankStageDialog;
import trente.asia.thankscard.services.mypage.model.MypageModel;
import trente.asia.thankscard.services.mypage.model.NoticeModel;
import trente.asia.thankscard.services.mypage.view.NoticeListAdapter;
import trente.asia.thankscard.services.posted.ThanksCardEditFragment;
import trente.asia.thankscard.services.rank.model.RankStage;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * Created by viet on 2/15/2016.
 */
public class MypageFragment extends AbstractTCFragment{

	private TextView			txtPostRank;
	private TextView			txtReceiveRank;
	private MypageModel			mypageModel;
	private ListView			lstNotice;
	private Button				btnPost;
	private ImageView			imgAvatar;
	private ImageView			imgRankStage;

	private NoticeListAdapter	adapter;
	private RankStageDialog		rankStageDialog;
	private List<RankStage>		rankStages;
	private TextView			txtPostRankNext;
	private TextView			txtReceiveRankNext;

	public boolean hasBackBtn(){
		return false;
	}

	public boolean hasSettingBtn(){
		return true;
	}

	@Override
	public int getFragmentLayoutId(){
		return R.layout.fragment_mypage;
	}

	@Override
	public int getFooterItemId(){
		return R.id.lnr_view_common_footer_mypage;
	}

	@Override
	public int getTitle(){
		return R.string.fragment_mypage_title;
	}

	@Override
	public void buildBodyLayout(){
		imgAvatar = (ImageView)getView().findViewById(R.id.img_fragment_mypage_avatar);
		txtPostRank = (TextView)getView().findViewById(R.id.txt_fragment_mypage_rank_post);
		txtPostRankNext = (TextView)getView().findViewById(R.id.txt_fragment_mypage_rank_post2);
		txtReceiveRank = (TextView)getView().findViewById(R.id.txt_fragment_mypage_rank_receive);
		txtReceiveRankNext = (TextView)getView().findViewById(R.id.txt_fragment_mypage_rank_receive2);
		imgRankStage = (ImageView)getView().findViewById(R.id.img_fragment_mypage_rank_stage);

		buildUserInfoLayout();
		builNoticeList();
	}

	@Override
	protected void initData(){

		requestMypageInfo();
	}

	private void buildUserInfoLayout(){
		ImageView imgAvatar = (ImageView)getView().findViewById(R.id.img_fragment_mypage_avatar);
		WfPicassoHelper.loadImage2(activity, host, imgAvatar, myself.avatarPath);

		btnPost = (Button)getView().findViewById(R.id.btn_fragment_mypage_post);
		btnPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				gotoPostEdit(null);
			}
		});

		// Button btnRank = (Button)getView().findViewById(R.id.btn_fragment_mypage_rank);
		// btnRank.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v){
		// if(!CCCollectionUtil.isEmpty(rankStages)){
		// showRankStageDialog();
		// }else{
		// requestRankStageInfo();
		// }
		// }
		// });
	}

	private void showRankStageDialog(){
		if(rankStageDialog == null){
			rankStageDialog = new RankStageDialog();
			rankStageDialog.setRankStagesInfo(rankStages, mypageModel.pointTotal);
		}
		rankStageDialog.show(getFragmentManager(), null);
	}

	private void builNoticeList(){
		lstNotice = (ListView)getView().findViewById(R.id.lst_fragment_mypage_notice);
		lstNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				NoticeModel notice = adapter.getItem(position);
				onNoticeClicked(notice);
			}
		});
	}

	private void onNoticeClicked(final NoticeModel notice){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which){
				gotoPostEdit(notice);
			}
		};
		showAlertDialogWithOption(getString(R.string.fragment_mypage_alert_title), notice.noticeMessage, getString(R.string.fragment_mypage_alert_post), getString(android.R.string.cancel), listener, null);
	}

	private void gotoPostEdit(NoticeModel notice){
		ThanksCardEditFragment thanksCardEditFragment = new ThanksCardEditFragment();
		thanksCardEditFragment.setTemplate(null);
		thanksCardEditFragment.setNoticeModel(notice);
		Bundle args = new Bundle();
		args.putInt(TcConst.ACTIVE_FOOTER_ITEM_ID, getFooterItemId());
		thanksCardEditFragment.setArguments(args);
		((MainActivity)getActivity()).addFragment(thanksCardEditFragment);
	}

	private void requestMypageInfo(){
		JSONObject param = new JSONObject();
		try{
			param.put("targetMonth", CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_YYYY_MM, new Date()));
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(TcConst.API_GET_MYPAGE_INFO, param, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(TcConst.API_GET_MYPAGE_INFO.equals(url)){
			requestMypageInfoSuccess(response);

			// control post button
			List<DeptModel> lstDept = CCJsonUtil.convertToModelList(response.optString("depts"), DeptModel.class);
			if(WelfareUtil.size(lstDept) == 1 && WelfareUtil.size(lstDept.get(0).members) == 1){
				btnPost.setEnabled(false);
			}
		}else if(TcConst.API_GET_RANK_STAGE_INFO.equals(url)){
			rankStages = CCJsonUtil.convertToModelList(response.optString("rankingStageList"), RankStage.class);
			if(!CCCollectionUtil.isEmpty(rankStages)){
				Collections.reverse(rankStages);
				showRankStageDialog();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void requestRankStageInfo(){
		requestLoad(TcConst.API_GET_RANK_STAGE_INFO, new JSONObject(), false);
	}

	private void requestMypageInfoSuccess(JSONObject response){
		mypageModel = CCJsonUtil.convertToModel(response.toString(), MypageModel.class);
		String postRank = getRank(mypageModel.pointPost, CCStringUtil.toString(mypageModel.seqPost));
		String nextPostRank = String.valueOf(Integer.valueOf(mypageModel.seqPost) - 1);
		txtPostRank.setText(getString(R.string.fragment_mypage_rank_post, postRank));
		txtPostRankNext.setText(getString(R.string.fragment_mypage_rank_next_post, nextPostRank, String.valueOf(Math.abs(mypageModel.archivePost))));

		String receiveRank = getRank(mypageModel.pointReceive, CCStringUtil.toString(mypageModel.seqRecieve));
		String nextReceiveRank = String.valueOf(Integer.valueOf(mypageModel.seqRecieve) - 1);
		txtReceiveRank.setText(getString(R.string.fragment_mypage_rank_receive, receiveRank));
		txtReceiveRankNext.setText(getString(R.string.fragment_mypage_rank_next_receive, nextReceiveRank, String.valueOf(Math.abs(mypageModel.archiveReceive))));

		WfPicassoHelper.loadImageWithDefaultIcon(activity, host, imgAvatar, myself.avatarPath, R.drawable.wf_profile);
		setRankStage(imgRankStage, mypageModel.stageNo);

		adapter = new NoticeListAdapter(getContext(), R.layout.item_notice, mypageModel.notices);
		lstNotice.setAdapter(adapter);
	}

	private void setRankStage(ImageView imageView, int rankStage){

		switch(rankStage){
		case 1:
			imageView.setImageResource(R.drawable.tc_rank_regular);
			break;
		case 2:
			imageView.setImageResource(R.drawable.tc_rank_bronze);
			break;
		case 3:
			imageView.setImageResource(R.drawable.tc_rank_silver);
			break;
		case 4:
			imageView.setImageResource(R.drawable.tc_rank_gold);
			break;
		}

	}

	public static String getRank(Integer point, String rank){
		if(CCConst.ZERO.equals(CCNumberUtil.checkNull(point))){
			return "--";
		}
		switch(rank){
		case "0":
			return "--";
		case "1":
			return rank + "st";
		case "2":
			return rank + "nd";
		case "3":
			return rank + "rd";
		default:
			return rank + "th";
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		txtPostRank = null;
		txtPostRankNext = null;
		txtReceiveRank = null;
		txtReceiveRankNext = null;
		lstNotice = null;
	}

}
