package nguyenhoangviet.vpcorp.messenger.services.message;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
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

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import asia.chiase.core.util.CCDateUtil;
import asia.chiase.core.util.CCFormatUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import nguyenhoangviet.vpcorp.android.util.AndroidUtil;
import nguyenhoangviet.vpcorp.android.util.DownloadFileManager;
import nguyenhoangviet.vpcorp.android.util.OpenDownloadedFile;
import nguyenhoangviet.vpcorp.android.view.ChiaseDownloadFileDialog;
import nguyenhoangviet.vpcorp.android.view.ChiaseEditText;
import nguyenhoangviet.vpcorp.android.view.ChiaseImageViewRatioWidth;
import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.commons.defines.MsConst;
import nguyenhoangviet.vpcorp.messenger.commons.dialog.MsChiaseDialog;
import nguyenhoangviet.vpcorp.messenger.commons.utils.MsUtils;
import nguyenhoangviet.vpcorp.messenger.fragment.AbstractMsgFragment;
import nguyenhoangviet.vpcorp.messenger.services.message.listener.OnAddCommentListener;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmBoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmCommentModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmMessageModel;
import nguyenhoangviet.vpcorp.messenger.services.util.NetworkChangeReceiver;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.EmotionConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.dialog.WfDialog;
import nguyenhoangviet.vpcorp.welfare.adr.dialog.WfProfileDialog;
import nguyenhoangviet.vpcorp.welfare.adr.models.CommentModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WfPicassoHelper;

/**
 * MessageDetailFragment
 *
 * @author HuyNQ
 */
public class MessageDetailFragment extends AbstractMsgFragment implements View.OnClickListener,NetworkChangeReceiver.OnNetworkChangeListener{

	private LinearLayout				mLnrRightHeader;
	private ChiaseImageViewRatioWidth	mImgThumbnail;
	private VideoView					mVideoView;
	private ImageView					imgPlay;

	private ImageView					mBtnComment;
	private LinearLayout				mLnrSend;
	private ChiaseEditText				mEdtComment;
	public static int					activeMessageId;
	private RealmMessageModel			activeMessage;

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
	private TextView					mTxtInternetConnection;

	private Integer						latestCommentId	= 0;
	private Timer						mTimer;
	private boolean						isSuccessLoad	= false;
	private final int					TIME_RELOAD		= 10000;
	private List<Integer>				mLstCommentId	= new ArrayList<>();
	private WfProfileDialog				mDlgProfile;
	private NetworkChangeReceiver		networkChangeReceiver;

	public void setActiveMessage(RealmMessageModel activeMessage){
		this.activeMessage = activeMessage;
		activeBoardId = activeMessage.boardId;
	}

	public void setOnAddCommentListener(OnAddCommentListener onAddCommentListener){
		this.onAddCommentListener = onAddCommentListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
		networkChangeReceiver = new NetworkChangeReceiver(this);
		getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
		mTxtInternetConnection = (TextView)getView().findViewById(R.id.txt_internet_connection);

		mLnrRightHeader.setOnClickListener(this);
		mLnrSend.setOnClickListener(this);
		mLnrLike.setOnClickListener(this);

		mDlgCheckUser = new MsChiaseDialog(activity);
		mDlgCheckUser.setDialogCheckUserList();
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);

		Picasso.with(getContext()).load(BuildConfig.HOST + preferencesAccountUtil.get(MsConst.DEF_STAMP_PATH))
				.fit().into(mBtnLikeCmt);
	}

	@Override
	protected void initData(){
		loadMessageDetail();
	}

	@Override
	protected void updateDetailMessage() {
		super.updateDetailMessage();
		refreshMessage();
	}

	@Override
	public void onResume(){
		super.onResume();
		activeMessageId = activeMessage.key;
		startTimer();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopTimer();
	}

	private void loadMessageDetail(){
		if(activeMessage.comments == null){
			return;
		}
		lnrCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mDlgCheckUser.updateCheckUserList(activeMessage.checks);
				mDlgCheckUser.show();
			}
		});
		mTxtComment.setText(String.valueOf(activeMessage.comments.size()));
		mTxtCheck.setText(String.valueOf(activeMessage.checks.size()));
		if(!CCStringUtil.isEmpty(activeMessage.messageSender.avatarPath)){
			WfPicassoHelper.loadImage(activity, host + activeMessage.messageSender.avatarPath, mImgAvatar, null);
		}
		mImgAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				mDlgProfile.show(BuildConfig.HOST, activeMessage.messageSender.userName, activeMessage.messageSender.avatarPath);
			}
		});

		Date messageDate = CCDateUtil.makeDateCustom(activeMessage.messageDate, WelfareConst.WF_DATE_TIME);
		String messageDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HH_MM, messageDate);
		mTxtUserDetail.setText(activeMessage.messageSender.userName);
		mTxtDetailDate.setText(messageDateFormat);
		if(!CCStringUtil.isEmpty(activeMessage.messageSender.avatarPath)){
			WfPicassoHelper.loadImage(activity, BuildConfig.HOST + activeMessage.messageSender.avatarPath, mImgAvatar, null);
		}

		if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(activeMessage.messageType)){
			mRltMedia.setVisibility(View.VISIBLE);
			mLnrFile.setVisibility(View.GONE);
			// get image from google service
			int imageWidth = AndroidUtil.getWidthScreen(activity);
			int imageHeight = imageWidth * 6 / 9;
			mLocationUrl = WelfareUtil.getGoogleUrl(activeMessage.gpsLatitude, activeMessage.gpsLongtitude, imageWidth, imageHeight);
			WfPicassoHelper.loadImage(activity, mLocationUrl, mImgThumbnail, null);

			mImgThumbnail.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					Intent intentMapApp = new Intent(android.content.Intent.ACTION_VIEW);
					intentMapApp.setData(Uri.parse("http://maps.google.com/maps??hl=en&saddr=" + activeMessage.messageContent + "&daddr=" + activeMessage.gpsLatitude + "," + activeMessage.gpsLongtitude));
					activity.startActivity(intentMapApp);
				}
			});

			// show address
			TextView txtAddress = (TextView)getView().findViewById(R.id.txt_id_address);
			txtAddress.setText(activeMessage.messageContent);
			txtAddress.setVisibility(View.VISIBLE);
		}else if(WelfareConst.ITEM_FILE_TYPE_FILE.equals(activeMessage.messageType)){
			mRltMedia.setVisibility(View.GONE);
			mLnrFile.setVisibility(View.VISIBLE);
			TextView txtFileName = (TextView)getView().findViewById(R.id.txt_id_file_name);
			txtFileName.setText(activeMessage.attachment.fileName);
		}else{
			mRltMedia.setVisibility(View.VISIBLE);
			mLnrFile.setVisibility(View.GONE);

			if(activeMessage.thumbnailAttachment != null && !CCStringUtil.isEmpty(activeMessage.thumbnailAttachment.fileUrl)){
				WfPicassoHelper.loadImage(activity, host + activeMessage.thumbnailAttachment.fileUrl, mImgThumbnail, null);
			}

			if(WelfareConst.ITEM_FILE_TYPE_MOVIE.equals(activeMessage.messageType)){
				imgPlay = (ImageView)getView().findViewById(R.id.img_id_play);
				imgPlay.setVisibility(View.VISIBLE);
				mImgThumbnail.setOnClickListener(this);
			}else{
				mDlgPhotoDetail = new WfDialog(activity);
				mDlgPhotoDetail.setDialogPhotoDetail(BuildConfig.HOST + activeMessage.thumbnailAttachment.fileUrl);
				mImgThumbnail.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v){
						mDlgPhotoDetail.show();
					}
				});
			}
		}
		for(RealmCommentModel comment : activeMessage.comments){
			addComment(comment);
		}
	}

	public void refreshMessage(){
		activeMessage = mRealm.where(RealmMessageModel.class).equalTo("key", activeMessageId).findFirst();
		for(RealmCommentModel comment : activeMessage.comments){
			addComment(comment);
		}
		mTxtComment.setText(String.valueOf(activeMessage.comments.size()));
		mTxtCheck.setText(String.valueOf(activeMessage.checks.size()));
	}

	private void addComment(final RealmCommentModel commentModel){
		if(!mLstCommentId.contains(commentModel.key)){
			LayoutInflater mInflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View commentView = mInflater.inflate(R.layout.item_comment_list, null);
			ImageView imgAvatar = (ImageView)commentView.findViewById(R.id.img_avatar_cmt);
			TextView txtUserNameCmt = (TextView)commentView.findViewById(R.id.txt_userName_cmt);
			TextView txtDateCmt = (TextView)commentView.findViewById(R.id.txt_date_cmt);
			TextView txtContentCmt = (TextView)commentView.findViewById(R.id.txt_contentCmt);
			ImageView imgEmotion = (ImageView)commentView.findViewById(R.id.img_id_emotion);
			Picasso.with(getContext()).load(BuildConfig.HOST + preferencesAccountUtil.get(MsConst.DEF_STAMP_PATH))
					.fit().into(imgEmotion);

			Date commentDate = CCDateUtil.makeDateCustom(commentModel.commentDate, WelfareConst.WF_DATE_TIME);
			String commentDateFormat = CCFormatUtil.formatDateCustom(WelfareConst.WF_DATE_TIME_DATE_HH_MM, commentDate);
			txtUserNameCmt.setText(commentModel.commentUser.userName);
			txtDateCmt.setText(commentDateFormat);
			if(!CCStringUtil.isEmpty(commentModel.commentUser.avatarPath)){
				WfPicassoHelper.loadImage(activity, BuildConfig.HOST + commentModel.commentUser.avatarPath, imgAvatar, null);
			}
			imgAvatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					mDlgProfile.show(BuildConfig.HOST, commentModel.commentUser.userName, commentModel.commentUser.avatarPath);
				}
			});

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
			if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(activeMessage.messageType)){
				long date = System.currentTimeMillis();
				filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
				fileUrl = mLocationUrl;
			}else{
				filename = activeMessage.attachment.fileName;
				fileUrl = host + activeMessage.attachment.fileUrl;
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
		if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(activeMessage.messageType)){
			long date = System.currentTimeMillis();
			filename = WelfareConst.FilesName.CAMERA_TEMP_FILE_NAME + String.valueOf(date) + WelfareConst.FilesName.CAMERA_TEMP_FILE_EXT;
			fileUrl = mLocationUrl;
		}else{
			filename = activeMessage.attachment.fileName;
			fileUrl = host + activeMessage.attachment.fileUrl;
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
			jsonObject.put("boardId", activeMessage.boardId + "");
			jsonObject.put("messageId", activeMessage.key + "");
			jsonObject.put("commentContent", content);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(MsConst.API_MESSAGE_COMMENT, jsonObject, false);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(MsConst.API_MESSAGE_COMMENT.equals(url)){
			CommentModel commentModel = null;
			try{
				commentModel = LoganSquare.parse(response.optString("detail"), CommentModel.class);
				RealmCommentModel comment = new RealmCommentModel(commentModel);
				Realm realm = Realm.getDefaultInstance();
				realm.beginTransaction();
				activeMessage.comments.add(comment);
				realm.commitTransaction();
				addComment(comment);
				mTxtComment.setText(String.valueOf(mLstCommentId.size()));
				scroll2Bottom();
				if(onAddCommentListener != null){
					onAddCommentListener.onAddCommentListener(activeMessage.key);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
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
	protected void onClickBackBtn(){
		if(isClickNotification){
			RealmBoardModel boardModel = new RealmBoardModel();
			boardModel.key = activeMessage.boardId;
			MessageFragment messageFragment = new MessageFragment();
			messageFragment.setActiveBoard(boardModel);
			emptyBackStack();
			gotoFragment(messageFragment);
		}else{
			super.onClickBackBtn();
		}
	}

	@Override
	public void onNetworkConnectionChanged(boolean connected){
		mTxtInternetConnection.setVisibility(connected ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		mLnrRightHeader = null;
		mImgThumbnail = null;
		activeMessage = null;
		mBtnComment = null;
		mEdtComment = null;

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

		getActivity().unregisterReceiver(networkChangeReceiver);
	}
}
