package trente.asia.messenger.services.message;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.AndroidUtil;
import trente.asia.android.util.DownloadFileManager;
import trente.asia.android.util.OpenDownloadedFile;
import trente.asia.android.view.ChiaseDownloadFileDialog;
import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.ChiaseImageViewRatioWidth;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.dialog.MsChiaseDialog;
import trente.asia.messenger.commons.utils.MsUtils;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.listener.OnAddCommentListener;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.EmotionConst;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfDialog;
import trente.asia.welfare.adr.models.CommentModel;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * MessageDetailFragment
 *
 * @author HuyNQ
 */
public class MessageDetailFragment extends AbstractMsgFragment implements View.OnClickListener{

	private LinearLayout				mLnrRightHeader;
	private ChiaseImageViewRatioWidth	mImgThumbnail;
	private VideoView					mVideoView;
	private ImageView					imgPlay;

	private MessageContentModel			messageModel;
	private ImageView					mBtnComment;
	private LinearLayout				mLnrSend;
	private ChiaseEditText				mEdtComment;
	public static String				activeMessageId;
	private MessageContentModel			activeMessage;

	private TextView					mTxtUserDetail;
	private TextView					mTxtDetailDate;
	private ImageView					mImgAvatar;
	private ImageButton					mBtnLikeCmt;
	private LinearLayout				mLnrLike;
	private String						mLocationUrl;
	private WfDialog					mDlgPhotoDetail;

	private LinearLayout				mLnrTarget;
	private LinearLayout				mLnrComment;
	private ScrollView					mScrDetail;
	private OnAddCommentListener		onAddCommentListener;

	private RelativeLayout				mRltMedia;
	private LinearLayout				mLnrFile;
	private MsChiaseDialog				mDlgCheckUser;
	public LinearLayout					lnrCheck;
	public TextView						mTxtCheck;
	public TextView						mTxtComment;

	private Integer						latestCommentId	= 0;
	private Timer						mTimer;
	private boolean						isSuccessLoad	= false;
	private final int					TIME_RELOAD		= 10000;
	private List<String>				mLstCommentId	= new ArrayList<>();

	public void setActiveMessage(MessageContentModel activeMessage){
		this.activeMessage = activeMessage;
	}

	public void setOnAddCommentListener(OnAddCommentListener onAddCommentListener){
		this.onAddCommentListener = onAddCommentListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_message_detail, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();
		initHeader(R.drawable.wf_back_white, getString(R.string.msg_message_detail_title), R.drawable.wf_downloadwhite);

		mLnrRightHeader = (LinearLayout)getView().findViewById(R.id.lnr_header_right_icon);
		mTxtUserDetail = (TextView)getView().findViewById(R.id.txt_detail_userName);
		mTxtDetailDate = (TextView)getView().findViewById(R.id.txt_detail_date);
		lnrCheck = (LinearLayout)getView().findViewById(R.id.lnr_id_check);
		mBtnComment = (ImageView)getView().findViewById(R.id.btn_send_cmt);
		mLnrSend = (LinearLayout)getView().findViewById(R.id.lnr_id_send);
		mTxtCheck = (TextView)getView().findViewById(R.id.txt_numb_check);
		mTxtComment = (TextView)getView().findViewById(R.id.txt_id_comment_numb);

		mEdtComment = (ChiaseEditText)getView().findViewById(R.id.edt_comment);
		addTextWatcher(mBtnComment, Arrays.asList((View)mEdtComment));
        addTextWatcher(mLnrSend, Arrays.asList((View)mEdtComment));

		mBtnLikeCmt = (ImageButton)getView().findViewById(R.id.btn_like_cmt);
		mLnrLike = (LinearLayout)getView().findViewById(R.id.lnr_id_like);
		mImgThumbnail = (ChiaseImageViewRatioWidth)getView().findViewById(R.id.img_message_thumbnail);
		mImgAvatar = (ImageView)getView().findViewById(R.id.img_id_avatar);
		mVideoView = (VideoView)getView().findViewById(R.id.video_id_view);

		mLnrTarget = (LinearLayout)getView().findViewById(R.id.lnr_id_target_user);
		mLnrComment = (LinearLayout)getView().findViewById(R.id.lnr_id_comment);
		mScrDetail = (ScrollView)getView().findViewById(R.id.scr_id_detail);
		mRltMedia = (RelativeLayout)getView().findViewById(R.id.rlt_id_media);
		mLnrFile = (LinearLayout)getView().findViewById(R.id.lnr_id_file);

		mLnrRightHeader.setOnClickListener(this);
		mLnrSend.setOnClickListener(this);
		mLnrLike.setOnClickListener(this);

		mDlgCheckUser = new MsChiaseDialog(activity);
		mDlgCheckUser.setDialogCheckUserList();
	}

	@Override
	protected void initData(){
		loadMessageDetail();
	}

	@Override
	public void onResume(){
		super.onResume();
		activeMessageId = activeMessage.key;

		if(mTimer == null) mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run(){
				if(isSuccessLoad){
					loadCommentLatest();
				}
			}
		}, TIME_RELOAD, TIME_RELOAD);
	}

	private void loadMessageDetail(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeMessage.boardId);
			jsonObject.put("targetMessageId", activeMessage.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_MSG_0003, jsonObject, true);
	}

	private void loadCommentLatest(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeMessage.boardId);
			jsonObject.put("targetMessageId", activeMessage.key);
			jsonObject.put("lastId", this.latestCommentId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_CMT_0002, jsonObject, false);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_MSG_0003.equals(url)){
			messageModel = CCJsonUtil.convertToModel(response.optString("detail"), MessageContentModel.class);
			// prefAccUtil.set(WelfareConst.ACTIVE_BOARD_ID, messageModel.boardId);
			if(!CCStringUtil.isEmpty(messageModel.messageSender.avatarPath)){
				WfPicassoHelper.loadImage(activity, host + messageModel.messageSender.avatarPath, mImgAvatar, null);
			}

			Date messageDate = CCDateUtil.makeDateCustom(messageModel.messageDate, WelfareConst.WL_DATE_TIME_1);
			String messageDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_2, messageDate);
			mTxtUserDetail.setText(messageModel.messageSender.userName);
			mTxtDetailDate.setText(messageDateFormat);
			if(!CCStringUtil.isEmpty(messageModel.messageSender.avatarPath)){
				WfPicassoHelper.loadImage(activity, BuildConfig.HOST + messageModel.messageSender.avatarPath, mImgAvatar, null);
			}
			if(!CCCollectionUtil.isEmpty(messageModel.targets)){
				mLnrTarget.setVisibility(View.VISIBLE);
				LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				for(UserModel userModel : messageModel.targets){
					View toUserView = mInflater.inflate(R.layout.item_to_user_list, null);
					ImageView imgToUserAvatar = (ImageView)toUserView.findViewById(R.id.img_id_to_user_avatar);
					if(!CCStringUtil.isEmpty(userModel.avatarPath)){
						WfPicassoHelper.loadImage(activity, BuildConfig.HOST + userModel.avatarPath, imgToUserAvatar, null);
					}
					mLnrTarget.addView(toUserView);
				}
			}

			if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(messageModel.messageType)){
				mRltMedia.setVisibility(View.VISIBLE);
				mLnrFile.setVisibility(View.GONE);
				// get image from google service
				int imageWidth = AndroidUtil.getWidthScreen(activity);
				int imageHeight = imageWidth * 6 / 9;
				mLocationUrl = WelfareUtil.getGoogleUrl(messageModel.gpsLatitude, messageModel.gpsLongtitude, imageWidth, imageHeight);
				WfPicassoHelper.loadImage(activity, mLocationUrl, mImgThumbnail, null);

				mImgThumbnail.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						Intent intentMapApp = new Intent(android.content.Intent.ACTION_VIEW);
						intentMapApp.setData(Uri.parse("http://maps.google.com/maps??hl=en&saddr=" + messageModel.messageContent + "&daddr=" + messageModel.gpsLatitude + "," + messageModel.gpsLongtitude));
						activity.startActivity(intentMapApp);
					}
				});

				// show address
				TextView txtAddress = (TextView)getView().findViewById(R.id.txt_id_address);
				txtAddress.setText(messageModel.messageContent);
				txtAddress.setVisibility(View.VISIBLE);
			}else if(WelfareConst.ITEM_FILE_TYPE_FILE.equals(messageModel.messageType)){
				mRltMedia.setVisibility(View.GONE);
				mLnrFile.setVisibility(View.VISIBLE);
				TextView txtFileName = (TextView)getView().findViewById(R.id.txt_id_file_name);
				txtFileName.setText(messageModel.attachment.fileName);
			}else{
				if(WelfareConst.ITEM_FILE_TYPE_PHOTO.equals(messageModel.messageType) && CCNumberUtil.checkNull(messageModel.attachment.fileSize) > WelfareConst.MAX_FILE_SIZE_2MB){
					mRltMedia.setVisibility(View.GONE);
					mLnrFile.setVisibility(View.VISIBLE);
					TextView txtFileName = (TextView)getView().findViewById(R.id.txt_id_file_name);
					txtFileName.setText(messageModel.attachment.fileName);
				}else{
					mRltMedia.setVisibility(View.VISIBLE);
					mLnrFile.setVisibility(View.GONE);

					if(messageModel.thumbnailAttachment != null && !CCStringUtil.isEmpty(messageModel.thumbnailAttachment.fileUrl)){
						WfPicassoHelper.loadImage(activity, host + messageModel.thumbnailAttachment.fileUrl, mImgThumbnail, null);
					}

					if(WelfareConst.ITEM_FILE_TYPE_MOVIE.equals(messageModel.messageType)){
						imgPlay = (ImageView)getView().findViewById(R.id.img_id_play);
						imgPlay.setVisibility(View.VISIBLE);
						mImgThumbnail.setOnClickListener(this);
					}else{
						mDlgPhotoDetail = new WfDialog(activity);
						mDlgPhotoDetail.setDialogPhotoDetail(BuildConfig.HOST + messageModel.attachment.fileUrl);
						mImgThumbnail.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v){
								mDlgPhotoDetail.show();
							}
						});
					}
				}
			}

			if(!CCCollectionUtil.isEmpty(messageModel.comments)){
				latestCommentId = CCNumberUtil.toInteger(messageModel.comments.get(messageModel.comments.size() - 1).key);
				for(CommentModel model : messageModel.comments){
					addComment(model);
				}
				mTxtComment.setText(String.valueOf(mLstCommentId.size()));
			}

			if(!CCCollectionUtil.isEmpty(messageModel.checks)){
				lnrCheck.setOnClickListener(this);
				mTxtCheck.setText(String.valueOf(WelfareUtil.size(messageModel.checks)));
				mDlgCheckUser.updateCheckUserList(messageModel.checks);
			}

			isSuccessLoad = true;
		}else if(WfUrlConst.WF_CMT_0002.equals(url)){
			List<CommentModel> lstComment = CCJsonUtil.convertToModelList(response.optString("comments"), CommentModel.class);
			if(!CCCollectionUtil.isEmpty(lstComment)){
				CommentModel lastItem = lstComment.get(lstComment.size() - 1);
				if(latestCommentId.compareTo(CCNumberUtil.toInteger(lastItem.key)) < 0){
					latestCommentId = CCNumberUtil.toInteger(lastItem.key);
				}
				for(CommentModel comment : lstComment){
					addComment(comment);
				}
				mTxtComment.setText(String.valueOf(mLstCommentId.size()));
				scroll2Bottom();
			}
		}else{
			super.successLoad(response, url);
		}
	}

	private void addComment(CommentModel commentModel){
		if(!mLstCommentId.contains(commentModel.key)){
			LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View commentView = mInflater.inflate(R.layout.item_comment_list, null);
			ImageView imgAvatar = (ImageView)commentView.findViewById(R.id.img_avatar_cmt);
			TextView txtUserNameCmt = (TextView)commentView.findViewById(R.id.txt_userName_cmt);
			TextView txtDateCmt = (TextView)commentView.findViewById(R.id.txt_date_cmt);
			TextView txtContentCmt = (TextView)commentView.findViewById(R.id.txt_contentCmt);
			ImageView imgEmotion = (ImageView)commentView.findViewById(R.id.img_id_emotion);

			Date commentDate = CCDateUtil.makeDateCustom(commentModel.commentDate, WelfareConst.WL_DATE_TIME_1);
			String commentDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WL_DATE_TIME_2, commentDate);
			txtUserNameCmt.setText(commentModel.commentUser.userName);
			txtDateCmt.setText(commentDateFormat);
			if(!CCStringUtil.isEmpty(commentModel.commentUser.avatarPath)){
				WfPicassoHelper.loadImage(activity, BuildConfig.HOST + commentModel.commentUser.avatarPath, imgAvatar, null);
			}

			if(!commentModel.commentContent.equals(EmotionConst.EMO_LIKE)){
				txtContentCmt.setText(commentModel.commentContent);
			}else{
				imgEmotion.setVisibility(View.VISIBLE);
				txtContentCmt.setVisibility(View.GONE);
			}

			mLnrComment.addView(commentView);
			mLstCommentId.add(commentModel.key);
		}
	}

	private void downloadFile(){
		if(AndroidUtil.verifyStoragePermissions(activity)){
			String fileUrl = null;
			String filename = null;
			if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(messageModel.messageType)){
				long date = System.currentTimeMillis();
				filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
				fileUrl = mLocationUrl;
			}else{
				filename = messageModel.attachment.fileName;
				fileUrl = host + messageModel.attachment.fileUrl;
			}

			String filePath = MsUtils.getFilesFolderPath() + "/" + filename;
			File file = new File(filePath);

			if(file.exists()){
				OpenDownloadedFile.downloadedFileDialog(file, activity);
			}else{
				final ChiaseDownloadFileDialog dialog = ChiaseDownloadFileDialog.startDialog(activity);
				DownloadFileManager.downloadFile(activity, fileUrl, file, new DownloadFileManager.OnDownloadListener() {

					@Override
					public void onStart(){
						Log.d("LOG", "START UPLOADING");
					}

					@Override
					public void onSetMax(final int max){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								dialog.setMax(max);
							}
						});
					}

					@Override
					public void onProgress(final int current){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								dialog.setCurrent(current);
							}
						});
					}

					@Override
					public void onFinishDownload(){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								// dialog.fileDownloaded();
							}
						});
					}

					@Override
					public void onResponse(boolean isSuccess, final String path){
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run(){
								dialog.dismiss();
								OpenDownloadedFile.downloadedFileDialog(new File(path), activity);
							}
						});
					}
				});
			}
		}
	}

	private void playVideo(){

		String fileUrl = null;
		String filename = null;
		if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(messageModel.messageType)){
			long date = System.currentTimeMillis();
			filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
			fileUrl = mLocationUrl;
		}else{
			filename = messageModel.attachment.fileName;
			fileUrl = host + messageModel.attachment.fileUrl;
		}

		String filePath = MsUtils.getFilesFolderPath() + "/" + filename;
		File file = new File(filePath);
		if(file.exists()){
			startPlay(filePath);
		}else{
			final ChiaseDownloadFileDialog dialog = ChiaseDownloadFileDialog.startDialog(activity);
			DownloadFileManager.downloadFile(activity, fileUrl, file, new DownloadFileManager.OnDownloadListener() {

				@Override
				public void onStart(){
					Log.d("LOG", "START UPLOADING");
				}

				@Override
				public void onSetMax(final int max){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){
							dialog.setMax(max);
						}
					});
				}

				@Override
				public void onProgress(final int current){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){
							dialog.setCurrent(current);
						}
					});
				}

				@Override
				public void onFinishDownload(){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){
							// dialog.fileDownloaded();
						}
					});
				}

				@Override
				public void onResponse(boolean isSuccess, final String path){
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run(){
							dialog.dismiss();
							// play video
							startPlay(path);
						}
					});
				}
			});
		}

		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.setDataAndType(Uri.parse(host + messageModel.attachment.fileUrl), "video/*");
		// // intent.setDataAndType(Uri.parse("http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4"), "video/*");
		// startActivityForResult(intent, WelfareConst.RequestCode.PLAY_MOVIE);

	}

	private void startPlay(String videoPath){
		mVideoView.setVideoPath(videoPath);
		mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp){
				mImgThumbnail.setVisibility(View.VISIBLE);
				imgPlay.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.GONE);
			}
		});

		mImgThumbnail.setVisibility(View.GONE);
		imgPlay.setVisibility(View.GONE);
		mVideoView.setVisibility(View.VISIBLE);
		mVideoView.start();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.lnr_header_right_icon:
			downloadFile();
			break;
		case R.id.img_message_thumbnail:
			playVideo();
			break;
		case R.id.lnr_id_send:
			String commentText = mEdtComment.getText().toString();
			// if(!CCStringUtil.isEmpty(commentText)){
			sendComment(commentText);
			// }else{
			// alertDialog.setMessage(getString(R.string.msg_comment_invalid_empty));
			// alertDialog.show();
			// mEdtComment.setText("");
			// }
			break;
		case R.id.lnr_id_like:
			sendComment(EmotionConst.EMO_LIKE);
			break;
		case R.id.lnr_id_check:
			mDlgCheckUser.show();
			break;
		default:
			break;
		}
	}

	private void sendComment(String content){
		mEdtComment.setText("");
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", messageModel.boardId);
			jsonObject.put("messageId", messageModel.key);
			jsonObject.put("commentContent", content);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_CMT_0001, jsonObject, false);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_CMT_0001.equals(url)){
			CommentModel commentModel = CCJsonUtil.convertToModel(response.optString("detail"), CommentModel.class);
			addComment(commentModel);
			mTxtComment.setText(String.valueOf(mLstCommentId.size()));
			// if(latestCommentId.compareTo(CCNumberUtil.toInteger(commentModel.key)) < 0){
			// latestCommentId = CCNumberUtil.toInteger(commentModel.key);
			// }
			scroll2Bottom();

			if(onAddCommentListener != null) onAddCommentListener.onAddCommentListener(activeMessage.key);
		}else{
			super.successUpdate(response, url);
		}
	}

	private void scroll2Bottom(){
		mScrDetail.postDelayed(new Runnable() {

			@Override
			public void run(){
				mScrDetail.fullScroll(ScrollView.FOCUS_DOWN);
			}
		}, 500);
	}

	@Override
	public void onPause(){
		super.onPause();
		activeMessageId = null;

		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}
	}

	@Override
	protected void onClickBackBtn(){
		if(isClickNotification){
			BoardModel boardModel = new BoardModel();
			boardModel.key = messageModel.boardId;
			MessageFragment messageFragment = new MessageFragment();
			messageFragment.setActiveBoard(boardModel);
			emptyBackStack();
			gotoFragment(messageFragment);
		}else{
			super.onClickBackBtn();
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		mLnrRightHeader = null;
		mImgThumbnail = null;
		activeMessage = null;
		messageModel = null;
		mBtnComment = null;
		mEdtComment = null;
		activeMessageId = null;

		mTxtUserDetail = null;
		mImgAvatar = null;
		mBtnLikeCmt = null;
		mLocationUrl = null;

		mLnrTarget = null;
		mLnrComment = null;
		mScrDetail = null;
		onAddCommentListener = null;

		mRltMedia = null;
		mLnrFile = null;
		mDlgCheckUser = null;
		lnrCheck = null;
		mTxtCheck = null;
	}
}
