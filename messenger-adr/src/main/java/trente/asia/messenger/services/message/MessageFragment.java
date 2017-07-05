package trente.asia.messenger.services.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import trente.asia.android.define.CsConst;
import trente.asia.android.view.model.ChiaseListItemModel;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.activities.CameraPhotoPreviewActivity;
import trente.asia.messenger.activities.FilePreviewActivity;
import trente.asia.messenger.activities.RecorderVideoActivity;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.commons.dialog.MsChiaseDialog;
import trente.asia.messenger.commons.menu.MessageMenuManager;
import trente.asia.messenger.commons.utils.MsUtils;
import trente.asia.messenger.databinding.FragmentMessageBinding;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.listener.ItemMsgClickListener;
import trente.asia.messenger.services.message.listener.OnActionClickListener;
import trente.asia.messenger.services.message.listener.OnAddCommentListener;
import trente.asia.messenger.services.message.listener.OnChangedBoardListener;
import trente.asia.messenger.services.message.listener.OnRefreshBoardListListener;
import trente.asia.messenger.services.message.listener.OnScrollToTopListener;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.messenger.services.message.model.RealmBoardModel;
import trente.asia.messenger.services.message.model.RealmMessageModel;
import trente.asia.messenger.services.message.model.RealmUserModel;
import trente.asia.messenger.services.message.model.WFMStampCategoryModel;
import trente.asia.messenger.services.message.model.WFMStampModel;
import trente.asia.messenger.services.message.view.BoardPagerAdapter;
import trente.asia.messenger.services.message.view.MemberListView;
import trente.asia.messenger.services.message.view.MembersAdapter;
import trente.asia.messenger.services.message.view.MessageAdapter;
import trente.asia.messenger.services.message.view.MessageView;
import trente.asia.messenger.services.message.view.NoteView;
import trente.asia.messenger.services.message.view.RecommendStampAdapter;
import trente.asia.messenger.services.message.view.StampAdapter;
import trente.asia.messenger.services.message.view.StampCategoryAdapter;
import trente.asia.messenger.services.user.UserListFragment;
import trente.asia.messenger.services.util.NetworkChangeReceiver;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.EmotionConst;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.define.WfErrorConst;
import trente.asia.welfare.adr.dialog.WfProfileDialog;
import trente.asia.welfare.adr.menu.OnMenuButtonsListener;
import trente.asia.welfare.adr.menu.OnMenuManageListener;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.LinearLayoutOnInterceptTouch;
import trente.asia.welfare.adr.view.MsgMultiAutoCompleteTextView;
import trente.asia.welfare.adr.view.WfSlideMenuLayout;

/**
 * MessageFragment
 *
 * @author TrungND
 */

public class MessageFragment extends AbstractMsgFragment implements View.OnClickListener,ItemMsgClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,StampCategoryAdapter.OnStampCategoryAdapterListener,StampAdapter.OnStampAdapterListener,UserListFragment.OnAddUserSuccessListener,MessageView.OnTextChangedListener,RecommendStampAdapter.OnRecommendStampAdapterListener,NetworkChangeReceiver.OnNetworkChangeListener{

	private ImageView									mImgLeftHeader;
	private WfSlideMenuLayout							mSlideMenuLayout;

	private MembersAdapter								mMembersAdapter;
	private StringBuilder								mMessageBuilder				= new StringBuilder();

	private MessageAdapter								mMsgAdapter;
	private View										mViewForMenuBehind;
	private List<RealmMessageModel>						mMsgContentList				= new ArrayList<>();
	private RealmBoardModel								activeBoard					= new RealmBoardModel();

	private BoardListFragment							boardListFragment;
	private MessageMenuManager							menuManager;
	private final int									TIME_RELOAD					= 10000;																																																				// 10
	// seconds
	private boolean										isFirstScroll2Top			= true;
	private boolean										isSuccessLoad				= false;

	private MsChiaseDialog								mDlgCheckUser;
	private MsChiaseDialog								mDlgMessageAction;
	private TextView									mTxtUnreadMessage;
	private ViewPager									mPagerBoard;
	private MessageView									messageView;
	private NoteView									noteView;
	private MemberListView								memberView;
	private FragmentMessageBinding						binding;
	private StampAdapter								stampAdapter;
	private StampCategoryAdapter						stampCategoryAdapter;
	private RecommendStampAdapter						recommendStampAdapter;
	private RealmResults<WFMStampCategoryModel>			mStampCategories;
	private NetworkChangeReceiver						networkChangeReceiver;

	private final int									REQUEST_CHECK_SETTINGS		= 31;
	private OnRefreshBoardListListener					onRefreshBoardListListener;

	private OnChangedBoardListener						onChangedBoardListener		= new OnChangedBoardListener() {

																						@Override
																						public void onChangedBoard(RealmBoardModel boardModel, boolean isLoad){
																							MessageFragment.this.onChangedBoard(boardModel, isLoad);
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
																						public void onAddCommentListener(int messageId){
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
																							CameraPhotoPreviewActivity.starCameraPhotoPreviewActivity(MessageFragment.this, activeBoardId);
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onAudioClicked(){
																						}

																						@Override
																						public void onFileClicked(){
																							FilePreviewActivity.startFilePreviewActivity(MessageFragment.this, activeBoardId);
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onVideoClicked(){
																							// alertDialog.setMessage(getString(R.string.chiase_common_disabled_function));
																							// alertDialog.show();
																							// onButtonMenuOpenedClicked();
																							RecorderVideoActivity.starVideoPreviewActivity(MessageFragment.this, activeBoardId);
																							onButtonMenuOpenedClicked();
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

																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onGalleryClicked(){
																							CameraPhotoPreviewActivity.starCameraFromGalleryPhotoPreviewActivity(MessageFragment.this, activeBoardId);
																							onButtonMenuOpenedClicked();
																						}

																						@Override
																						public void onContactClicked(){
																						}
																					};

	private OnActionClickListener						actionClickListener			= new OnActionClickListener() {

																						@Override
																						public void onActionClickListener(ChiaseListItemModel item, RealmMessageModel message){
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

	private long										pivotTime					= 0;
	private String										staticStampId;
	private String										lastMessageUpdateDate;
	private MessageDetailFragment						detailFragment;
	// private String latestBoardId;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		networkChangeReceiver = new NetworkChangeReceiver(this);
		staticStampId = preferencesAccountUtil.get(MsConst.DEF_STAMP_ID);
		getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
			mRootView = binding.getRoot();

			binding.layoutStamp.listStamps.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false));
			stampAdapter = new StampAdapter(this);
			binding.layoutStamp.listStamps.setAdapter(stampAdapter);

			binding.layoutStamp.listStampCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
			stampCategoryAdapter = new StampCategoryAdapter(this);
			binding.layoutStamp.listStampCategories.setAdapter(stampCategoryAdapter);

			binding.layoutRecommendStamp.listRecommendStamp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
			recommendStampAdapter = new RecommendStampAdapter(this);
			binding.layoutRecommendStamp.listRecommendStamp.setAdapter(recommendStampAdapter);

			binding.layoutStamp.btnCancel.setOnClickListener(this);

			mStampCategories = mRealm.where(WFMStampCategoryModel.class).findAll();
			stampCategoryAdapter.setStampCategories(mStampCategories);

			if(mStampCategories.size() > 0){
				stampAdapter.setStamps(mStampCategories.get(0).stamps);
			}
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
		messageView.setOnTextChangedListener(this);
		messageView.revMessage.listener = onScrollToTopListener;
		noteView = (NoteView)inflater.inflate(R.layout.board_pager_note, null);
		noteView.initialization();
		memberView = (MemberListView)inflater.inflate(R.layout.board_pager_member, null);
		memberView.initialization();

		initViewPager();

		mSlideMenuLayout = (WfSlideMenuLayout)getView().findViewById(R.id.drawer_layout);
		mSlideMenuLayout.setOutsideLayout((LinearLayoutOnInterceptTouch)getView().findViewById(R.id.main_layout));
		mViewForMenuBehind = getView().findViewById(R.id.viewForMenuBehind);
		loadStamps();

		menuManager = new MessageMenuManager();
		menuManager.setMenuLayout(activity, R.id.menuMain, onMenuManagerListener, onMenuButtonsListener);

		messageView.imgStamp.setOnClickListener(this);
		mImgLeftHeader.setOnClickListener(this);
		messageView.imgSend.setOnClickListener(this);
		mViewForMenuBehind.setOnClickListener(this);
		messageView.lnrLike.setOnClickListener(this);
		lnrRightHeader.setOnClickListener(this);
		noteView.btnSave.setOnClickListener(this);
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);

		mMsgAdapter = new MessageAdapter(activity, mMsgContentList, this, new OnAvatarClickListener() {

			@Override
			public void OnAvatarClick(String userName, String avatarPath){
				mDlgProfile.show(BuildConfig.HOST, userName, avatarPath);
			}
		});
		messageView.revMessage.setAdapter(mMsgAdapter);

		initDialog();

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		boardListFragment = new BoardListFragment();
		boardListFragment.setOnChangedBoardListener(onChangedBoardListener);
		if(activeBoard != null && !CCStringUtil.isEmpty(activeBoard.key)){
			boardListFragment.setActiveBoard(activeBoard);
		}
		this.onRefreshBoardListListener = boardListFragment.getOnRefreshBoardListListener();
		transaction.replace(R.id.slice_menu_board, boardListFragment).commit();

	}

	@Override
	protected void updateBoardList(List<RealmBoardModel> boards) {
		super.updateBoardList(boards);
		boardListFragment.updateBoards(boards);
	}

	private void loadStamps(){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String lastUpdateDate = preferences.getString(MsConst.MESSAGE_STAMP_LAST_UPDATE_DATE, null);
		JSONObject jsonObject = new JSONObject();
		if(lastUpdateDate != null){
			try{
				jsonObject.put("lastUpdateDate", lastUpdateDate);
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		requestLoad(MsConst.API_MESSAGE_STAMP_CATEGORY_LIST, jsonObject, true);
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
		activeBoard = new RealmBoardModel();
		activeBoard.key = Integer.parseInt(prefAccUtil.get(MsConst.PREF_ACTIVE_BOARD_ID));
		activeBoardId = activeBoard.key;
		String prefFirstTime = preferencesAccountUtil.get(MsConst.PREF_LOAD_MESSAGE_FIRST_TIME);
		if(prefFirstTime == null || "".equals(prefFirstTime)){
			loadMessageFirstTime(true);
		}else{
			loadFirstMessagesFromDB();
		}
	}

	private void loadFirstMessagesFromDB(){
		RealmResults<RealmMessageModel> messages = mRealm.where(RealmMessageModel.class).equalTo("boardId", activeBoardId).findAllSorted("key", Sort.ASCENDING);
		int startIndex = messages.size() < 10 ? 0 : messages.size() - 10;
		List<RealmMessageModel> subListMessages = messages.subList(startIndex, messages.size());
		addFirstMessages(subListMessages);
		activeBoard = mRealm.where(RealmBoardModel.class).equalTo("key", activeBoardId).findFirst();
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

	private void closeLayoutStamps(){
		mViewForMenuBehind.setVisibility(View.GONE);
		binding.layoutStamp.getRoot().setVisibility(View.GONE);
	}

	private void loadMessageFirstTime(boolean showLoading){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoard.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(MsConst.API_MESSAGE_BOARD, jsonObject, showLoading);
		preferencesAccountUtil.set(MsConst.PREF_LOAD_MESSAGE_FIRST_TIME, "done");
	}


	private void loadNoteDetail(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeBoard.key);
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestLoad(MsConst.API_MESSAGE_NOTE_DETAIL, jsonObject, true);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		try{
			if(MsConst.API_MESSAGE_BOARD.equals(url)){
				List<MessageContentModel> lstMessage = LoganSquare.parseList(response.optString("contents"), MessageContentModel.class);
				List<RealmMessageModel> lstRealmMessage = new ArrayList<>();
				for(MessageContentModel message : lstMessage){
					lstRealmMessage.add(new RealmMessageModel(message));
				}
				addFirstMessages(lstRealmMessage);
				saveMessageToDB(lstRealmMessage);
				activeBoard = mRealm.where(RealmBoardModel.class).equalTo("key", activeBoardId).findFirst();

			}else if(MsConst.API_MESSAGE_NOTE_DETAIL.equals(url)){
				BoardModel boardModel = LoganSquare.parse(response.optString("board"), BoardModel.class);
				activeBoard = new RealmBoardModel(boardModel);
				updateNoteData();
			}else if(MsConst.API_MESSAGE_STAMP_CATEGORY_LIST.equals(url)){
				saveStamps(response);
			}else{
				super.successLoad(response, url);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void addNewMessage(List<RealmMessageModel> newMessages) {
		super.addNewMessage(newMessages);
		mMsgAdapter.addMessages(newMessages);
	}

	@Override
	protected void updateMessages(List<RealmMessageModel> updateMessages) {
		super.updateMessages(updateMessages);
		mMsgAdapter.updateMessage(updateMessages);
	}

	private void addFirstMessages(List<RealmMessageModel> lstMessage){
		if(!CCCollectionUtil.isEmpty(lstMessage)){
			mMsgAdapter.addMessages(lstMessage);
			messageView.revMessage.setLastVisibleItem(mMsgAdapter.getItemCount() - 1);
			messageView.revMessage.scrollRecyclerToBottom();
			startMessageKey = lstMessage.get(0).key;
			latestMessageKey = lstMessage.get(lstMessage.size() - 1).key;
		}
	}

	private void saveStamps(JSONObject response){
		List<WFMStampCategoryModel> stampCategories = CCJsonUtil.convertToModelList(response.optString("stampCategories"), WFMStampCategoryModel.class);
		String lastUpdateDate = response.optString("lastUpdateDate");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		preferences.edit().putString(MsConst.MESSAGE_STAMP_LAST_UPDATE_DATE, lastUpdateDate).apply();

		mRealm.beginTransaction();
		for(WFMStampCategoryModel category : stampCategories){
			if(category.deleteFlag){
				WFMStampCategoryModel.deleteStampCategory(mRealm, category.key);
			}else{
				WFMStampCategoryModel wfmStampCategory = WFMStampCategoryModel.getCategory(mRealm, category.key);
				if(wfmStampCategory == null){
					mRealm.copyToRealm(category);
				}else{
					wfmStampCategory.updateStampCategory(category);
					for(WFMStampModel stamp : category.stamps){
						if(stamp.deleteFlag){
							WFMStampModel.deleteStamp(mRealm, stamp.key);
						}else{
							WFMStampModel wfmStamp = WFMStampModel.getStamp(mRealm, stamp.key);
							if(wfmStamp == null){
								wfmStampCategory.stamps.add(stamp);
							}else{
								wfmStamp.updateStamp(wfmStamp);
							}
						}
					}
				}
			}
		}
		mRealm.commitTransaction();

	}

	private void log(String msg){
		Log.e("MessageFragment", msg);
	}

	private void updateMessage(List<RealmMessageModel> lstAction){
		mMsgAdapter.updateMessage(lstAction);
	}

	@Override
	public void onResume(){
		super.onResume();
		// ((WelfareActivity)activity).setOnDeviceBackButtonClickListener(this);
		((WelfareActivity)activity).setOnActivityResultListener(onActivityResultListener);

		if(mSlideMenuLayout.isMenuShown()){
			mSlideMenuLayout.toggleMenu();
		}
		if(activeBoard != null){
			activeBoardId = activeBoard.key;
		}

	}

	private String filterToUserList(String message){
		mMessageBuilder.delete(0, mMessageBuilder.length());
		StringBuilder toUserBuilder = new StringBuilder();
		String[] words = message.split(" ");
		for(String word : words){
			if(word.startsWith("@")){
				RealmUserModel findUser = MsUtils.findUser4AccountName(activeBoard.memberList, word.substring(1, word.length()));
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
			if(WelfareConst.ITEM_TEXT_TYPE_LIKE.equals(messageType)){
				jsonObject.put("execType", WelfareConst.ITEM_TEXT_TYPE_LIKE);
				jsonObject.put("messageContent", EmotionConst.EMO_LIKE);
			}else if(WelfareConst.ITEM_TEXT_TYPE_STAMP.equals(messageType)){
				jsonObject.put("execType", WelfareConst.ITEM_TEXT_TYPE_STAMP);
				jsonObject.put("messageContent", content);
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
		requestUpdate(MsConst.API_MESSAGE_UPDATE, jsonObject, false);
	}

	private void deleteMessage(RealmMessageModel message){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", message.key + "");
			jsonObject.put("boardId", activeBoard.key + "");
		}catch(JSONException e){
			e.printStackTrace();
		}
		requestUpdate(MsConst.API_MESSAGE_DELETE, jsonObject, false);
	}

	private void editMessage(RealmMessageModel message){
		messageView.edtMessage.setText(message.messageContent);
		focusEditText(messageView.edtMessage);
		messageView.animateLikeButton(false);
	}

	private void copy2Note(RealmMessageModel message){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeBoard.key);
			jsonObject.put("targetMessageId", message.key);
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(MsConst.API_MESSAGE_NOTE_COPY, jsonObject, false);
	}

	private void updateNote(){
		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("key", activeBoard.key);
			jsonObject.put("boardNote", noteView.edtNote.getText());
		}catch(JSONException ex){
			ex.printStackTrace();
		}
		requestUpdate(MsConst.API_MESSAGE_NOTE_UPDATE, jsonObject, false);
	}

	@Override
	protected void successUpdate(JSONObject response, String url){
		if(MsConst.API_MESSAGE_UPDATE.equals(url)){
			messageView.imgSend.setEnabled(true);
			MessageContentModel contentModel = null;
			try{
				contentModel = LoganSquare.parse(response.optString("detail"), MessageContentModel.class);
				RealmMessageModel message = new RealmMessageModel(contentModel);
				if(activeBoardId == message.boardId){
					if(messageView.likeButtonType == MessageView.LikeButtonType.EDIT){
						List<RealmMessageModel> lstUpdate = new ArrayList<>();
						lstUpdate.add(message);
						mRealm.beginTransaction();
						mRealm.copyToRealmOrUpdate(message);
						mMsgAdapter.updateMessage(lstUpdate);
						mRealm.commitTransaction();
					}else{
						appendMessage(message);
						latestMessageKey = message.key;
					}
					messageView.edtMessage.setText("");
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}else if(MsConst.API_MESSAGE_LIKE.equals(url)){

		}else if(MsConst.API_MESSAGE_DELETE.equals(url)){
			int key = Integer.parseInt(response.optString("key"));
			mMsgAdapter.deleteMessage(key);
			mRealm.beginTransaction();
			mRealm.where(RealmMessageModel.class).equalTo("key", key).findFirst().deleteFromRealm();
			mRealm.commitTransaction();
		}else if(MsConst.API_MESSAGE_NOTE_UPDATE.equals(url)){
			noteView.changeMode(false);
//			loadMessageLatest();
		}else{
			super.successUpdate(response, url);
		}
	}

	private void appendMessage(RealmMessageModel messageModel){
		messageView.revMessage.isScrollToBottom();
		mMsgAdapter.addMessage(messageModel);
		messageView.revMessage.scrollRecyclerToBottom();
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.img_id_header_left_icon:
			hideKeyBoard(activity);
			mSlideMenuLayout.toggleMenu();
			break;
		case R.id.btn_id_send:
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
			closeLayoutStamps();
			break;

		case R.id.lnr_id_like:
			if(messageView.likeButtonType == MessageView.LikeButtonType.LIKE){
				sendMessage(WelfareConst.ITEM_TEXT_TYPE_STAMP, staticStampId, null, null);
			}else{
				messageView.edtMessage.setText("");
			}
			break;

		case R.id.lnr_header_right_icon:
			UserListFragment fragment = new UserListFragment();
			fragment.setOnAddUserSuccessListener(this);
			gotoFragment(fragment);
			break;

		case R.id.btn_id_save:
			updateNote();
			break;
		case R.id.btn_cancel:
			binding.layoutStamp.getRoot().setVisibility(View.GONE);
			mViewForMenuBehind.setVisibility(View.GONE);
			break;
		case R.id.btn_stamp:
			binding.layoutStamp.getRoot().setVisibility(View.VISIBLE);
			mViewForMenuBehind.setVisibility(View.VISIBLE);
			hideSoftKeyboard();
			break;
		default:
			break;
		}
	}

	private void hideSoftKeyboard(){
		View view = getActivity().getCurrentFocus();
		if(view != null){
			InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@Override
	public void onItemMsgClickListener(RealmMessageModel item){
		detailFragment = new MessageDetailFragment();
		detailFragment.setActiveMessage(item);
		detailFragment.setOnAddCommentListener(onAddCommentListener);
		gotoFragment(detailFragment);
	}

	@Override
	public void onItemCheckClickListener(RealmMessageModel item){
		if(!CCCollectionUtil.isEmpty(item.checks)){
			mDlgCheckUser.updateCheckUserList(item.getCheckList());
			mDlgCheckUser.show();
		}
	}

	@Override
	public void onItemMsgLongClickListener(RealmMessageModel item){
		List<ChiaseListItemModel> lstAction = new ArrayList<>();
		if(myself.key.equals(item.messageSender.key + "")){
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
		if(!isFirstScroll2Top){
			loadOldMessages();
		}else{
			isFirstScroll2Top = false;
		}
	}

	private void loadOldMessages(){
		RealmResults<RealmMessageModel> oldMessages = mRealm.where(RealmMessageModel.class).lessThan("key", startMessageKey).equalTo("boardId", activeBoardId).findAllSorted("key", Sort.DESCENDING);
		int endIndex = oldMessages.size() < 10 ? oldMessages.size() : 10;
		List<RealmMessageModel> subOldMessage = oldMessages.subList(0, endIndex);
		if(subOldMessage.size() > 0){
			startMessageKey = subOldMessage.get(endIndex - 1).key;
			mMsgAdapter.addMessage2Top(subOldMessage);
		}
	}

	private void onChangedBoard(final RealmBoardModel boardModel, boolean isLoad){
		if(mSlideMenuLayout.isMenuShown()){
			mSlideMenuLayout.toggleMenu();
		}
		if(activeBoard == null || boardModel.key != activeBoard.key){
			latestMessageKey = 0;
			activity.runOnUiThread(new Runnable() {

				public void run(){
					MessageFragment.super.updateHeader(boardModel.boardName);
				}
			});

			activeBoard = boardModel;
			updateNoteData();
			if(isLoad){
				activeBoardId = boardModel.key;
				mMsgAdapter.clearAll();
				messageView.edtMessage.clearFocus();
				messageView.edtMessage.setText("");
				isFirstScroll2Top = true;
				loadFirstMessagesFromDB();
			}
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
			List<RealmUserModel> userListWithoutMe = new ArrayList<>();
			for(RealmUserModel userModel : activeBoard.memberList){
				if(userModel.key != Integer.parseInt(myself.key)){
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
			BoardModel boardModel = null;
			try{
				boardModel = LoganSquare.parse(detailBoard, BoardModel.class);
				RealmBoardModel realmBoardModel = new RealmBoardModel(boardModel);
				if(boardListFragment != null) boardListFragment.onAddedContactListener(realmBoardModel);
				break;
			}catch(IOException e){
				e.printStackTrace();
			}
		default:
			break;
		}
	}

	private void appendFile(Intent returnedIntent){
		log("Append photo");
		String detailMessage = returnedIntent.getExtras().getString("detail");
		if(WelfareConst.WF_FILE_SIZE_NG.equals(detailMessage)){
			alertDialog.setMessage(getString(R.string.wf_invalid_photo_over));
			alertDialog.show();
		}else{
			MessageContentModel photoModel = null;
			// try{
			// photoModel = LoganSquare.parse(CCStringUtil.toString(detailMessage), MessageContentModel.class);
			// RealmMessageModel
			// appendMessage(photoModel);
			// }catch(IOException e){
			// e.printStackTrace();
			// }
		}
	}

	private void onAddCommentListener(int messageId){
		mMsgAdapter.addComment(messageId);
	}

	public void setActiveBoard(RealmBoardModel activeBoard){
		this.activeBoard = activeBoard;
		activeBoardId = activeBoard.key;
	}

	protected void commonNotSuccess(JSONObject response){
		String returnCd = response.optString(CsConst.RETURN_CODE_PARAM);
		if(WfErrorConst.ERR_CODE_CONNECTION_ERROR.equals(returnCd)){
			log("commonNotSuccess: get here");
		}else{
			super.commonNotSuccess(response);
		}
		messageView.imgSend.setEnabled(true);
	}

	// protected void errorRequest(VolleyError error){
	// super.errorRequest(error);
	// messageView.imgSend.setEnabled(true);
	// }

	@Override
	protected void errorNetwork(String url){
		if(MsConst.API_MESSAGE_BOARD.equals(url)){

		}
	}

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
		boardListFragment = null;
		menuManager = null;
		getActivity().unregisterReceiver(networkChangeReceiver);
		mRealm.close();
	}

	@Override
	public void onPause(){
		super.onPause();
		preferencesAccountUtil.set(MsConst.PREF_LAST_MESSAGE_KEY, latestMessageKey + "");
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

	@Override
	public void onStampCategoryClick(WFMStampCategoryModel stampCategory){
		stampAdapter.setStamps(stampCategory.stamps);
	}

	@Override
	public void onStampClick(WFMStampModel stamp){
		sendMessage(WelfareConst.ITEM_TEXT_TYPE_STAMP, stamp.key, null, null);
		binding.layoutStamp.getRoot().setVisibility(View.GONE);
		mViewForMenuBehind.setVisibility(View.GONE);
	}

	@Override
	public void onSuccess(RealmBoardModel boardModel){
		if(boardListFragment != null) boardListFragment.onAddedContactListener(boardModel);
	}

	@Override
	public void onTextChanged(CharSequence charSequence){

		if(charSequence.length() < 3){
			binding.layoutRecommendStamp.getRoot().setVisibility(View.GONE);
		}else{
			List<WFMStampModel> recommendStamps = getRecommendStamps(charSequence.toString());
			if(recommendStamps.size() == 0){
				binding.layoutRecommendStamp.getRoot().setVisibility(View.GONE);
			}else{
				binding.layoutRecommendStamp.getRoot().setVisibility(View.VISIBLE);
				recommendStampAdapter.setRecommendStamps(recommendStamps);
			}
		}
	}

	private List<WFMStampModel> getRecommendStamps(String keyword){
		List<WFMStampModel> recommendStamps = new ArrayList<>();
		for(WFMStampCategoryModel stampCategory : mStampCategories){
			for(WFMStampModel stamp : stampCategory.stamps){
				if(stamp.keyword != null && !stamp.keyword.isEmpty() && stamp.keyword.toLowerCase().contains(keyword.toLowerCase())){
					recommendStamps.add(stamp);
				}
			}
		}
		return recommendStamps;
	}

	@Override
	public void onRecommendStampAdapterClick(WFMStampModel recommendStamp){
		sendMessage(WelfareConst.ITEM_TEXT_TYPE_STAMP, recommendStamp.key, null, null);
		binding.layoutRecommendStamp.getRoot().setVisibility(View.GONE);
	}

	@Override
	protected void onClickBackBtn(){
		log("onClickBackBtn");
		if(messageView.buttonType == MessageView.ButtonType.MENU_OPENED){
			onButtonMenuOpenedClicked();
		}else{
			super.onClickBackBtn();
		}
	}

	@Override
	public void onClickDeviceBackButton(){
		log("onClickDeviceBackButton");
		binding.layoutRecommendStamp.getRoot().setVisibility(View.GONE);
		binding.layoutStamp.getRoot().setVisibility(View.GONE);
		mViewForMenuBehind.setVisibility(View.GONE);
		super.onClickDeviceBackButton();
	}

	@Override
	public void onNetworkConnectionChanged(boolean connected){
		binding.txtInternetConnection.setVisibility(connected ? View.GONE : View.VISIBLE);
		if(connected){
			startTimer();
		}else{
			stopTimer();
		}
	}
}
