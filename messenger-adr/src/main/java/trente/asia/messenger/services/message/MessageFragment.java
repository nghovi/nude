package trente.asia.messenger.services.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.model.ChiaseListItemModel;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.activities.CameraPhotoPreviewActivity;
import trente.asia.messenger.activities.FilePreviewActivity;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.commons.dialog.MsChiaseDialog;
import trente.asia.messenger.commons.menu.MessageMenuManager;
import trente.asia.messenger.commons.utils.MsUtils;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.listener.ItemMsgClickListener;
import trente.asia.messenger.services.message.listener.OnActionClickListener;
import trente.asia.messenger.services.message.listener.OnAddCommentListener;
import trente.asia.messenger.services.message.listener.OnChangedBoardListener;
import trente.asia.messenger.services.message.listener.OnRefreshBoardListListener;
import trente.asia.messenger.services.message.listener.OnScrollToTopListener;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.messenger.services.message.view.BoardPagerAdapter;
import trente.asia.messenger.services.message.view.MemberListView;
import trente.asia.messenger.services.message.view.MembersAdapter;
import trente.asia.messenger.services.message.view.MessageAdapter;
import trente.asia.messenger.services.message.view.MessageView;
import trente.asia.messenger.services.message.view.NoteView;
import trente.asia.messenger.services.user.UserListActivity;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.activity.WelfareFragment;
import trente.asia.welfare.adr.define.EmotionConst;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfProfileDialog;
import trente.asia.welfare.adr.menu.OnMenuButtonsListener;
import trente.asia.welfare.adr.menu.OnMenuManageListener;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.MsgMultiAutoCompleteTextView;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * MessageFragment
 *
 * @author TrungND
 */
public class MessageFragment extends AbstractMsgFragment implements View.OnClickListener,ItemMsgClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

	private ImageView									mImgLeftHeader;
	private WfSlideMenuLayout							mSlideMenuLayout;

	private MembersAdapter								mMembersAdapter;
	private StringBuilder								mMessageBuilder				= new StringBuilder();

	private MessageAdapter								mMsgAdapter;
	private View										mViewForMenuBehind;
	private List<MessageContentModel>					mMsgContentList				= new ArrayList<>();
	private BoardModel									activeBoard;
	public static String								activeBoardId;
	private String										autoroadCd;

	private BoardListFragment							boardListFragment;
	private MessageMenuManager							menuManager;
	private final int									TIME_RELOAD					= 10000;				// 10
																											// seconds
	private Timer										mTimer;
	private boolean										isFirstScroll2Top			= true;
	private String										latestMessageId				= "0";
	private String										startMessageId				= "0";
	private boolean										isSuccessLoad				= false;

	private MsChiaseDialog								mDlgCheckUser;
	private MsChiaseDialog								mDlgMessageAction;
	private TextView									mTxtUnreadMessage;
	private ViewPager									mPagerBoard;
	private MessageView									messageView;
	private NoteView									noteView;
	private MemberListView								memberView;

	private final int									REQUEST_CHECK_SETTINGS		= 31;
	private OnRefreshBoardListListener					onRefreshBoardListListener;

	private OnChangedBoardListener						onChangedBoardListener		= new OnChangedBoardListener() {

																						@Override
																						public void onChangedBoard(BoardModel boardModel){
																							MessageFragment.this.onChangedBoard(boardModel);
																						}

																						@Override
																						public void onRefreshUnreadMessage(Integer unreadMessage){
																							MessageFragment.this.refreshUnreadMessage(unreadMessage);
																						}
																					};

	private OnScrollToTopListener						onScrollToTopListener		= new OnScrollToTopListener() {

																						@Override
																						public void onScrollToTopListener(){
																							MessageFragment.this.onScrollToTopListener();
																						}
																					};

	private OnAddCommentListener						onAddCommentListener		= new OnAddCommentListener() {

																						@Override
																						public void onAddCommentListener(String messageId){
																							MessageFragment.this.onAddCommentListener(messageId);
																						}
																					};

	private OnMenuManageListener						onMenuManagerListener		= new OnMenuManageListener() {

																						@Override
																						public void onMenuOpened(){
																							messageView.buttonType = MessageView.ButtonType.MENU_OPENED;
																						}

																						@Override
																						public void onMenuClosed(){
																							messageView.buttonType = MessageView.ButtonType.MENU;
																							mViewForMenuBehind.setVisibility(View.GONE);
																							messageView.edtMessage.setEnabled(true);
																						}
																					};

	protected OnMenuButtonsListener						onMenuButtonsListener		= new OnMenuButtonsListener() {

																						@Override
																						public void onCameraClicked(){
																							CameraPhotoPreviewActivity.starCameraPhotoPreviewActivity(MessageFragment.this, activeBoard.key);
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onAudioClicked(){
																						}

																						@Override
																						public void onFileClicked(){
																							FilePreviewActivity.startFilePreviewActivity(MessageFragment.this, activeBoard.key);
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onVideoClicked(){
																							alertDialog.setMessage(getString(R.string.chiase_common_disabled_function));
																							alertDialog.show();
																							onButtonMenuOpenedClicked();
																							// RecorderVideoActivity.starVideoPreviewActivity(MessageFragment.this,
																							// activeBoard.key);
																							// onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onLocationClicked(){
																							// Check if google play services is up to date
																							// final int playServicesStatus =
																							// GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
																							// if(playServicesStatus != ConnectionResult.SUCCESS){
																							// // If google play services in not available show an
																							// // error dialog and return
																							// final Dialog errorDialog =
																							// GoogleApiAvailability.getInstance().getErrorDialog(activity,
																							// playServicesStatus, 0, null);
																							// errorDialog.show();
																							// onButtonMenuOpenedClicked();
																							// return;
																							// }

																							// check location setting
																							GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(activity).addApi(LocationServices.API).addConnectionCallbacks(MessageFragment.this).addOnConnectionFailedListener(MessageFragment.this).build();
																							mGoogleApiClient.connect();

																							LocationRequest locationRequestHighAccuracy = LocationRequest.create();
																							locationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
																							locationRequestHighAccuracy.setInterval(30 * 1000);
																							locationRequestHighAccuracy.setFastestInterval(5 * 1000);
																							LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequestHighAccuracy);

																							PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
																							result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

																								@Override
																								public void onResult(LocationSettingsResult result){
																									final Status status = result.getStatus();
																									// final
																									// LocationSettingsStates
																									// =
																									// result.getLocationSettingsStates();
																									switch(status.getStatusCode()){
																									case LocationSettingsStatusCodes.SUCCESS:
																										// All-location-settings-are-satisfied.
																										sendLocation();
																										break;
																									case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
																										// Location-settings-are-not-satisfied.
																										// But-could-be-fixed-by-showing-the-user-a-dialog.
																										try{
																											// Show-the-dialog-by-calling-startResolutionForResult(),
																											// and-check-the-result-in-onActivityResult().
																											status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
																										}catch(IntentSender.SendIntentException e){
																											// Ignore-the-error.
																										}
																										break;
																									case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
																										// Location-settings-are-not-satisfied.However,we-have-no-way
																										// to-fix-the-settings-so-we-won't-show-the-dialog.
																										break;
																									}
																								}
																							});

																							//
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onGalleryClicked(){
																							CameraPhotoPreviewActivity.starCameraFromGalleryPhotoPreviewActivity(MessageFragment.this, activeBoard.key);
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onContactClicked(){
																						}
																					};

	private OnActionClickListener						actionClickListener			= new OnActionClickListener() {

																						@Override
																						public void onActionClickListener(ChiaseListItemModel item, MessageContentModel message){
																							if(MsConst.MESSAGE_ACTION_EDIT.equals(item.key)){
																								editMessage(message);
																							}else if(MsConst.MESSAGE_ACTION_DELETE.equals(item.key)){
																								deleteMessage(message);
																							}else if(MsConst.MESSAGE_ACTION_COPY.equals(item.key)){
																								copy2Note(message);
																							}
																						}
																					};

	private WelfareActivity.OnActivityResultListener	onActivityResultListener	= new WelfareActivity.OnActivityResultListener() {

																						@Override
																						public void onActivityResult(int requestCode, int resultCode, Intent data){

																							if(resultCode != Activity.RESULT_OK) return;
																							switch(requestCode){

																							case REQUEST_CHECK_SETTINGS:
																								sendLocation();
																								break;

																							default:
																								break;
																							}
																						}
																					};

	private WfProfileDialog								mDlgProfile;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_message, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		mImgLeftHeader = (ImageView)getView().findViewById(R.id.img_id_header_left_icon);
		LinearLayout lnrRightHeader = (LinearLayout)getView().findViewById(R.id.lnr_header_right_icon);
		mTxtUnreadMessage = (TextView)getView().findViewById(R.id.txt_id_unread_message);

		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		messageView = (MessageView)inflater.inflate(R.layout.board_pager_message, null);
		messageView.initialization();
		messageView.revMessage.listener = onScrollToTopListener;
		noteView = (NoteView)inflater.inflate(R.layout.board_pager_note, null);
		noteView.initialization();
		memberView = (MemberListView)inflater.inflate(R.layout.board_pager_member, null);
		memberView.initialization();

		initViewPager();

		mSlideMenuLayout = (WfSlideMenuLayout)getView().findViewById(R.id.drawer_layout);
		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		boardListFragment = new BoardListFragment();
		boardListFragment.setOnChangedBoardListener(onChangedBoardListener);
		if(activeBoard != null && !CCStringUtil.isEmpty(activeBoard.key)){
			boardListFragment.setActiveBoard(activeBoard);
		}
		this.onRefreshBoardListListener = boardListFragment.getOnRefreshBoardListListener();
		transaction.replace(R.id.slice_menu_board, boardListFragment).commit();

		menuManager = new MessageMenuManager();
		menuManager.setMenuLayout(activity, R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

		mImgLeftHeader.setOnClickListener(this);
		messageView.lnrSend.setOnClickListener(this);
		mViewForMenuBehind.setOnClickListener(this);
		messageView.lnrLike.setOnClickListener(this);
		lnrRightHeader.setOnClickListener(this);
		noteView.btnSave.setOnClickListener(this);
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);

		mMsgAdapter = new MessageAdapter(activity, mMsgContentList, this, new OnAvatarClickListener() {

			@Override
			public void OnAvatarClick(String userName, String avatarPath){
				mDlgProfile.updateProfileDetail(BuildConfig.HOST, userName, avatarPath);
				mDlgProfile.show();
			}
		});
		messageView.revMessage.setAdapter(mMsgAdapter);

		initDialog();
	}

	private void initViewPager(){
		mPagerBoard = (ViewPager)getView().findViewById(R.id.view_pager_id_board);
		BoardPagerAdapter pagerAdapter = new BoardPagerAdapter(activity, messageView, noteView, memberView);
		mPagerBoard.setAdapter(pagerAdapter);

		ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0){
				switch(arg0){
				case 0:
					break;
				case 1:
					loadNoteDetail();
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2){
			}

			@Override
			public void onPageScrollStateChanged(int arg0){
			}
		};
		mPagerBoard.addOnPageChangeListener(mListener);
	}

	private void initDialog(){
		mDlgCheckUser = new MsChiaseDialog(activity);
		mDlgCheckUser.setDialogCheckUserList();

		mDlgMessageAction = new MsChiaseDialog(activity);
		mDlgMessageAction.setDialogAction(actionClickListener);
	}

	@Override
	protected void initData(){
		if(activeBoard != null){
			if(!CCStringUtil.isEmpty(activeBoard.key)){
				loadMessageList();
			}
		}
	}

	private void sendLocation(){
		Location lastKnownLocation = MsUtils.getLocation(activity);
		if(lastKnownLocation != null){
			String address = WelfareUtil.getAddress4Location(activity, lastKnownLocation);
			sendMessage(WelfareConst.ITEM_TEXT_TYPE_LOC, address, String.valueOf(lastKnownLocation.getLatitude()), String.valueOf(lastKnownLocation.getLongitude()));
		}else{
			Toast.makeText(activity, getString(R.string.chiase_common_not_working_gps), Toast.LENGTH_LONG).show();
		}
	}

	private void onButtonMenuClicked(){
		if(messageView.buttonType == MessageView.ButtonType.IN_ANIMATION){
			return;
		}

		messageView.buttonType = MessageView.ButtonType.IN_ANIMATION;
		messageView.edtMessage.setEnabled(false);
		menuManager.openMenu(messageView.imgSend);
		mViewForMenuBehind.setVisibility(View.VISIBLE);
	}

	private void onButtonMenuOpenedClicked(){
		if(messageView.buttonType == MessageView.ButtonType.IN_ANIMATION || mViewForMenuBehind.getVisibility() == View.GONE){
			return;
		}
		messageView.buttonType = MessageView.ButtonType.IN_ANIMATION;
		menuManager.closeMenu();
	}

	private void loadMessageList(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoard.key);
			if(!CCStringUtil.isEmpty(autoroadCd) && !CCConst.NONE.equals(autoroadCd)){
				jsonObject.put("autoroadCd", autoroadCd);
			}else{
				jsonObject.put("execType", "NEW");
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_MSG_0002, jsonObject, true);
	}

	private void loadMessageLatest(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoard.key);
			jsonObject.put("targetMessageId", latestMessageId);
			jsonObject.put("startMessageId", startMessageId);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_MSG_0007, jsonObject, false);
	}

	private void loadNoteDetail(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeBoard.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(WfUrlConst.WF_NOT_0003, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		if(WfUrlConst.WF_MSG_0002.equals(url)){
			List<MessageContentModel> lstMessage = CCJsonUtil.convertToModelList(response.optString("contents"), MessageContentModel.class);
			if(!CCCollectionUtil.isEmpty(lstMessage)){
				if(CCStringUtil.isEmpty(autoroadCd)){
					mMsgAdapter.addMessages(lstMessage);
					messageView.revMessage.setLastVisibleItem(mMsgAdapter.getItemCount() - 1);
					messageView.revMessage.scrollRecyclerToBottom();
					startMessageId = lstMessage.get(0).key;
					latestMessageId = lstMessage.get(lstMessage.size() - 1).key;
				}else{
					// append to top
					Collections.reverse(lstMessage);
					mMsgAdapter.addMessage2Top(lstMessage);
					startMessageId = lstMessage.get(0).key;
				}
			}

			autoroadCd = response.optString("autoroadCd");
			isSuccessLoad = true;
		}else if(WfUrlConst.WF_MSG_0007.equals(url)){
			// get latest message
			List<MessageContentModel> lstMessage = CCJsonUtil.convertToModelList(response.optString("contents"), MessageContentModel.class);
			// lastUpdateTime = response.optString("lastUpdateTime");
			if(!CCCollectionUtil.isEmpty(lstMessage)){
				messageView.revMessage.isScrollToBottom();
				mMsgAdapter.addMessages(lstMessage);
				messageView.revMessage.scrollRecyclerToBottom();
				String lastKey = lstMessage.get(lstMessage.size() - 1).key;
				if(CCNumberUtil.toInteger(latestMessageId).compareTo(CCNumberUtil.toInteger(lastKey)) < 0){
					latestMessageId = lastKey;
				}
			}

			// update board list
			List<BoardModel> lstBoard = CCJsonUtil.convertToModelList(response.optString("boards"), BoardModel.class);
			if(onRefreshBoardListListener != null) onRefreshBoardListListener.onRefreshBoardListListener(lstBoard);

			// update action list
			List<MessageContentModel> lstAction = CCJsonUtil.convertToModelList(response.optString("actions"), MessageContentModel.class);
			if(!CCCollectionUtil.isEmpty(lstAction)){
				this.updateMessage(lstAction);
			}
		}else if(WfUrlConst.WF_NOT_0003.equals(url)){
			BoardModel boardModel = CCJsonUtil.convertToModel(response.optString("board"), BoardModel.class);
			activeBoard = boardModel;
			updateNoteData();
		}else{
			super.successLoad(response, url);
		}
	}

	private void updateMessage(List<MessageContentModel> lstAction){
		mMsgAdapter.updateMessage(lstAction);
	}

	@Override
	public void onResume(){
		super.onResume();
		((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
		((WelfareActivity)activity).setOnActivityResultListener(onActivityResultListener);

		if(mSlideMenuLayout.isMenuShown()){
			mSlideMenuLayout.toggleMenu();
		}
		if(activeBoard != null){
			activeBoardId = activeBoard.key;
		}

		if(mTimer == null) mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run(){
				if(isSuccessLoad){
					loadMessageLatest();
				}
			}
		}, TIME_RELOAD, TIME_RELOAD);
	}

	private String filterToUserList(String message){
		mMessageBuilder.delete(0, mMessageBuilder.length());
		StringBuilder toUserBuilder = new StringBuilder();
		String[] words = message.split(" ");
		for(String word : words){
			if(word.startsWith("@")){
				UserModel findUser = MsUtils.findUser4AccountName(activeBoard.memberList, word.substring(1, word.length()));
				if(findUser != null){
					toUserBuilder.append(findUser.key + ",");
				}else{
					mMessageBuilder.append(word + " ");
				}
			}else{
				mMessageBuilder.append(word + " ");
			}
		}
		return toUserBuilder.toString();
	}

	private void sendMessage(String messageType, String content, String latitude, String longitude){

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoard.key);
			if(WelfareConst.ITEM_TEXT_TYPE_ICON.equals(messageType)){
				jsonObject.put("execType", WelfareConst.ITEM_TEXT_TYPE_ICON);
				jsonObject.put("messageContent", EmotionConst.EMO_LIKE);
			}else if(WelfareConst.ITEM_TEXT_TYPE_LOC.equals(messageType)){
				jsonObject.put("gpsLongtitude", longitude);
				jsonObject.put("gpsLatitude", latitude);
				jsonObject.put("messageContent", content);
			}else{
				messageView.imgSend.setEnabled(false);
				jsonObject.put("execType", WelfareConst.ITEM_TEXT_TYPE_TEXT);
				// get to user list in content
				jsonObject.put("targetListString", filterToUserList(content));
				int end = mMessageBuilder.length() >= 1 ? (mMessageBuilder.length() - 1) : 0;
				jsonObject.put("messageContent", mMessageBuilder.substring(0, end));
				if(messageView.likeButtonType == MessageView.LikeButtonType.EDIT){
					jsonObject.put("key", mDlgMessageAction.getMessage().key);
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_MSG_0004, jsonObject, false);
	}

	private void deleteMessage(MessageContentModel message){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", message.key);
			jsonObject.put("boardId", activeBoard.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_MSG_0008, jsonObject, false);
	}

	private void editMessage(MessageContentModel message){
		messageView.edtMessage.setText(message.messageContent);
		focusEditText(messageView.edtMessage);
		messageView.animateLikeButton(false);
	}

	private void copy2Note(MessageContentModel message){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeBoard.key);
			jsonObject.put("targetMessageId", message.key);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_NOT_0002, jsonObject, false);
	}

	private void updateNote(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeBoard.key);
			jsonObject.put("boardNote", noteView.edtNote.getText());
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(WfUrlConst.WF_NOT_0001, jsonObject, false);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(WfUrlConst.WF_MSG_0004.equals(url)){
			messageView.imgSend.setEnabled(true);
			MessageContentModel contentModel = CCJsonUtil.convertToModel(response.optString("detail"), MessageContentModel.class);
			if(messageView.likeButtonType == MessageView.LikeButtonType.EDIT){
				List<MessageContentModel> lstUpdate = new ArrayList<>();
				lstUpdate.add(contentModel);
				mMsgAdapter.updateMessage(lstUpdate);
			}else{
				appendMessage(contentModel);
				// latestMessageId = contentModel.key;
			}
			messageView.edtMessage.setText("");
		}else if(WfUrlConst.WF_MSG_0005.equals(url)){

		}else if(WfUrlConst.WF_MSG_0008.equals(url)){
			mMsgAdapter.deleteMessage(response.optString("key"));
		}else if(WfUrlConst.WF_NOT_0001.equals(url)){
			noteView.changeMode(false);
			loadMessageLatest();
			// mPagerBoard.setCurrentItem(0, true);
		}else{
			super.successUpdate(response, url);
		}
	}

	private void appendMessage(MessageContentModel messageModel){
		messageView.revMessage.isScrollToBottom();
		mMsgAdapter.addMessage(messageModel);
		messageView.revMessage.scrollRecyclerToBottom();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_left_icon:
			mSlideMenuLayout.toggleMenu();
			break;
		case R.id.lnr_id_send:
			if(messageView.buttonType == MessageView.ButtonType.MENU){
				onButtonMenuClicked();
			}else{
				String messageText = CCStringUtil.toString(messageView.edtMessage.getText());
				// if(!CCStringUtil.isEmpty(messageText)){
				sendMessage(WelfareConst.ITEM_TEXT_TYPE_TEXT, messageText, null, null);
				// }else{
				// alertDialog.setMessage(getString(R.string.msg_msg_invalid_empty));
				// alertDialog.show();
				// messageView.edtMessage.setText("");
				// }
			}
			break;
		case R.id.viewForMenuBehind:
			onButtonMenuOpenedClicked();
			break;

		case R.id.lnr_id_like:
			if(messageView.likeButtonType == MessageView.LikeButtonType.LIKE){
				sendMessage(WelfareConst.ITEM_TEXT_TYPE_ICON, null, null, null);
			}else{
				messageView.edtMessage.setText("");
			}
			break;

		case R.id.lnr_header_right_icon:
			Intent intent = new Intent(activity, UserListActivity.class);
			startActivityForResult(intent, MsConst.REQUEST_CODE_ADD_CONTACT);
			break;

		case R.id.btn_id_save:
			updateNote();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemMsgClickListener(MessageContentModel item){
		MessageDetailFragment fragment = new MessageDetailFragment();
		fragment.setActiveMessage(item);
		fragment.setOnAddCommentListener(onAddCommentListener);
		gotoFragment(fragment);
	}

	@Override
	public void onItemCheckClickListener(MessageContentModel item){
		if(!CCCollectionUtil.isEmpty(item.checks)){
			mDlgCheckUser.updateCheckUserList(item.getCheckList());
			mDlgCheckUser.show();
		}
	}

	@Override
	public void onItemMsgLongClickListener(MessageContentModel item){
		List<ChiaseListItemModel> lstAction = new ArrayList<>();
		if(myself.key.equals(item.messageSender.key)){
			if(WelfareConst.ITEM_TEXT_TYPE_TEXT.equals(item.messageType)){
				lstAction.add(new ChiaseListItemModel(MsConst.MESSAGE_ACTION_EDIT, getString(R.string.msg_msg_action_edit)));
			}
			lstAction.add(new ChiaseListItemModel(MsConst.MESSAGE_ACTION_DELETE, getString(R.string.msg_msg_action_delete)));
		}

		if(WelfareConst.ITEM_TEXT_TYPE_TEXT.equals(item.messageType)){
			lstAction.add(new ChiaseListItemModel(MsConst.MESSAGE_ACTION_COPY, getString(R.string.msg_msg_action_copy)));
		}
		if(!CCCollectionUtil.isEmpty(lstAction)){
			mDlgMessageAction.setMessage(item);
			mDlgMessageAction.updateDialogAction(lstAction);
		}
	}

	private void onScrollToTopListener(){
		if(!CCStringUtil.isEmpty(autoroadCd) && !CCConst.NONE.equals(autoroadCd)){
			if(!isFirstScroll2Top){
				loadMessageList();
			}else{
				isFirstScroll2Top = false;
			}
		}
	}

	private void onChangedBoard(final BoardModel boardModel){
		if(mSlideMenuLayout.isMenuShown()){
			mSlideMenuLayout.toggleMenu();
		}
		if(activeBoard == null || !boardModel.key.equals(activeBoard.key)){
			activity.runOnUiThread(new Runnable() {

				public void run(){
					MessageFragment.super.updateHeader(boardModel.boardName);
				}
			});

			activeBoard = boardModel;
			updateNoteData();
			activeBoardId = boardModel.key;
			mMsgAdapter.clearAll();
			messageView.edtMessage.clearFocus();
			messageView.edtMessage.setText("");
			autoroadCd = "";
			isFirstScroll2Top = true;
			loadMessageList();
		}else{
			if(!boardModel.boardName.equals(activeBoard.boardName)){
				activeBoard = boardModel;
				updateNoteData();
				updateHeader(boardModel.boardName);
			}
		}
	}

	private void updateNoteData(){
		noteView.edtNote.setText(CCStringUtil.toString(activeBoard.boardNote));
		noteView.changeMode(false);
		if(!CCCollectionUtil.isEmpty(activeBoard.memberList)){
			memberView.updateMemberList(activeBoard.memberList);
			List<UserModel> userListWithoutMe = new ArrayList<>();
			for(UserModel userModel : activeBoard.memberList){
				if(!userModel.key.equals(myself.key)){
					userListWithoutMe.add(userModel);
				}
			}
			mMembersAdapter = new MembersAdapter(activity, userListWithoutMe);
			messageView.edtMessage.setAdapter(mMembersAdapter);
			messageView.edtMessage.setTokenizer(new MsgMultiAutoCompleteTextView.CommaTokenizer());
		}
	}

	private void refreshUnreadMessage(Integer unreadMessage){
		if(unreadMessage != null && unreadMessage.compareTo(CCConst.ZERO) > 0){
			mTxtUnreadMessage.setVisibility(View.VISIBLE);
			mTxtUnreadMessage.setText(String.valueOf(unreadMessage));
		}else{
			mTxtUnreadMessage.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent returnedIntent){
		super.onActivityResult(requestCode, resultCode, returnedIntent);

		if(resultCode != Activity.RESULT_OK) return;
		switch(requestCode){
		case WelfareConst.RequestCode.PHOTO_CHOOSE:
			appendFile(returnedIntent);
			break;

		case WelfareConst.RequestCode.VIDEO_CHOOSE:
			appendFile(returnedIntent);
			break;

		case WelfareConst.RequestCode.PICK_FILE:
			appendFile(returnedIntent);
			break;

		case MsConst.REQUEST_CODE_ADD_CONTACT:
			String detailBoard = returnedIntent.getExtras().getString("detail");
			BoardModel boardModel = CCJsonUtil.convertToModel(detailBoard, BoardModel.class);
			if(boardListFragment != null) boardListFragment.onAddedContactListener(boardModel);
			break;

		default:
			break;
		}
	}

	private void appendFile(Intent returnedIntent){
		String detailMessage = returnedIntent.getExtras().getString("detail");
		if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
			alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
			alertDialog.show();
		}else{
			MessageContentModel photoModel = CCJsonUtil.convertToModel(CCStringUtil.toString(detailMessage), MessageContentModel.class);
			appendMessage(photoModel);
		}
	}

	private void onAddCommentListener(String messageId){
		mMsgAdapter.addComment(messageId);
	}

	public void setActiveBoard(BoardModel activeBoard){
		this.activeBoard = activeBoard;
		activeBoardId = activeBoard.key;
	}

	@Override
	protected void onClickBackBtn(){
		if(messageView.buttonType == MessageView.ButtonType.MENU_OPENED){
			onButtonMenuOpenedClicked();
		}else{
			super.onClickBackBtn();
		}
	}

	protected void commonNotSuccess(JSONObject response){
		super.commonNotSuccess(response);
		messageView.imgSend.setEnabled(true);
	}

	// protected void errorRequest(VolleyError error){
	// super.errorRequest(error);
	// messageView.imgSend.setEnabled(true);
	// }

	protected void errorRequest2(){
		super.errorRequest2();
		messageView.imgSend.setEnabled(true);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

		mImgLeftHeader = null;
		mSlideMenuLayout = null;
		mMembersAdapter = null;
		mMessageBuilder = null;
		messageView = null;
		noteView = null;
		mPagerBoard = null;

		mMsgAdapter = null;
		mViewForMenuBehind = null;
		mMsgContentList = null;
		activeBoard = null;
		autoroadCd = null;

		boardListFragment = null;
		menuManager = null;
	}

	@Override
	public void onPause(){
		super.onPause();
		activeBoardId = null;
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle){

	}

	@Override
	public void onConnectionSuspended(int i){

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult){

	}
}
