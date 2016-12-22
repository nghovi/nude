package trente.asia.messenger.services.message;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseCheckableImageView;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.listener.OnChangedBoardListener;
import trente.asia.messenger.services.message.listener.OnRefreshBoardListListener;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.view.BoardAdapter;
import trente.asia.messenger.services.user.MsgSettingFragment;
import trente.asia.messenger.services.user.listener.OnAddedContactListener;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WfUrlConst;
import trente.asia.welfare.adr.dialog.WfProfileDialog;
import trente.asia.welfare.adr.utils.WfPicassoHelper;

/**
 * BoardListFragment
 *
 * @author TrungND
 */
public class BoardListFragment extends AbstractMsgFragment implements View.OnClickListener,OnAddedContactListener{

	private ListView					lsvBoard;
	private BoardAdapter				mAdapter;
	private BoardModel					activeBoard;

	private ImageView					imgUserAvatar;
	private TextView					txtUserName;
	private TextView					txtUserMail;
	private ChiaseCheckableImageView	btnSetting;

	private String						mAvatarPath					= "";
	private OnChangedBoardListener		onChangedBoardListener;
	private OnRefreshBoardListListener	onRefreshBoardListListener	= new OnRefreshBoardListListener() {

																		@Override
																		public void onRefreshBoardListListener(List<BoardModel> lstBoard){
																			BoardListFragment.this.refreshBoardList(lstBoard);
																		}
																	};
	private WfProfileDialog				mDlgProfile;

	public void setOnChangedBoardListener(OnChangedBoardListener onChangedBoardListener){
		this.onChangedBoardListener = onChangedBoardListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		WelfareActivity.OnDeviceBackButtonClickListener listener = ((WelfareActivity)getActivity()).getOnDeviceBackButtonClickListener();
		super.onCreate(savedInstanceState);
		if(listener != null){
			((WelfareActivity)getActivity()).setOnDeviceBackButtonClickListener(listener);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_board_list, container, false);
		}
		return mRootView;
	}

	@Override
	protected void initView(){
		super.initView();

		lsvBoard = (ListView)getView().findViewById(R.id.lsv_id_board);
		lsvBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				BoardModel boardModel = (BoardModel)parent.getItemAtPosition(position);
				activeBoard = boardModel;
				lsvBoard.setItemChecked(position, true);
				if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(boardModel);
			}
		});

		imgUserAvatar = (ImageView)getView().findViewById(R.id.img_id_myAvatar);
		txtUserName = (TextView)getView().findViewById(R.id.txt_loginUserName);
		txtUserMail = (TextView)getView().findViewById(R.id.txt_loginUserMail);
		btnSetting = (ChiaseCheckableImageView)getView().findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(this);
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(95, 95);
	}

	@Override
	protected void initData(){
		super.initData();
		loadBoardList();
	}

	private void loadBoardList(){
		JSONObject jsonObject = new JSONObject();
		requestLoad(WfUrlConst.WF_MSG_0001, jsonObject, false);
	}

	@Override
	protected void successLoad(JSONObject response, String url){
		System.out.println("===========================================");
		System.out.println(isDestroy);

		txtUserName.setText(myself.userName);
		txtUserMail.setText(myself.userMail);
		if(!CCStringUtil.isEmpty(myself.avatarPath)){
			mAvatarPath = myself.avatarPath;
			WfPicassoHelper.loadImage(activity, host + myself.avatarPath, imgUserAvatar, null);
			imgUserAvatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v){
					mDlgProfile.updateProfileDetail(BuildConfig.HOST, myself.userName, myself.avatarPath);
					mDlgProfile.show();
				}
			});
		}

		List<BoardModel> boardList = CCJsonUtil.convertToModelList(response.optString("boards"), BoardModel.class);
		if(!CCCollectionUtil.isEmpty(boardList)){
			mAdapter = new BoardAdapter(activity, boardList, new OnAvatarClickListener() {

				@Override
				public void OnAvatarClick(String userName, String avatarPath){
					mDlgProfile.updateProfileDetail(BuildConfig.HOST, userName, avatarPath);
					mDlgProfile.show();
				}
			});
			lsvBoard.setAdapter(mAdapter);

			// set active board
			if(activeBoard != null && !CCStringUtil.isEmpty(activeBoard.key)){
				for(int i = 0; i < boardList.size(); i++){
					BoardModel boardModel = boardList.get(i);
					if(activeBoard.key.equals(boardModel.key)){
						activeBoard = boardModel;
						lsvBoard.setItemChecked(i, true);
						if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(activeBoard);
						break;
					}
				}
			}else{
				lsvBoard.setItemChecked(0, true);
				activeBoard = boardList.get(0);
				if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(boardList.get(0));
			}

			checkUnreadMessage(boardList);
		}
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_setting:
			gotoFragment(new MsgSettingFragment());
			break;
		default:
			break;
		}
	}

	public void onAddedContactListener(BoardModel boardModel){
		// int checkedPosition = lsvBoard.getCheckedItemPosition();
		activeBoard = boardModel;
		mAdapter.add(boardModel, 0);
		lsvBoard.setItemChecked(0, true);
		if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(boardModel);
	}

	// public void onReceiveMessage(MessageContentModel messageModel){
	// mAdapter.addUnreadMessage(messageModel);
	// }

	private void refreshBoardList(List<BoardModel> lstBoard){
		// check avatar path
		myself = prefAccUtil.getUserPref();
		if(!mAvatarPath.equals(CCStringUtil.toString(myself.avatarPath))){
			mAvatarPath = CCStringUtil.toString(myself.avatarPath);
			if(!CCStringUtil.isEmpty(myself.avatarPath)){
				WfPicassoHelper.loadImage(activity, host + myself.avatarPath, imgUserAvatar, null);
			}else{
				imgUserAvatar.setImageResource(R.drawable.wf_profile);
			}
		}

		if(!CCCollectionUtil.isEmpty(lstBoard)){
			mAdapter = new BoardAdapter(activity, lstBoard, new OnAvatarClickListener() {

				@Override
				public void OnAvatarClick(String userName, String avatarPath){
					mDlgProfile.updateProfileDetail(BuildConfig.HOST, userName, avatarPath);
					mDlgProfile.show();
				}
			});
			lsvBoard.setAdapter(mAdapter);

			boolean isActive = false;
			for(int i = 0; i < lstBoard.size(); i++){
				BoardModel boardModel = lstBoard.get(i);
				if(boardModel.key.equals(activeBoard.key)){
					lsvBoard.setItemChecked(i, true);
					isActive = true;
					break;
				}
			}

			if(!isActive){
				lsvBoard.setItemChecked(0, true);
				if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(lstBoard.get(0));
			}

			checkUnreadMessage(lstBoard);
		}
	}

	private void checkUnreadMessage(List<BoardModel> lstBoard){
		Integer unreadMessage = 0;
		for(BoardModel boardModel : lstBoard){
			unreadMessage += CCNumberUtil.checkNull(boardModel.boardUnread);
		}
		if(onChangedBoardListener != null) onChangedBoardListener.onRefreshUnreadMessage(unreadMessage);
	}

	public OnRefreshBoardListListener getOnRefreshBoardListListener(){
		return onRefreshBoardListListener;
	}

	public void setActiveBoard(BoardModel activeBoard){
		this.activeBoard = activeBoard;
	}

	@Override
	protected void onClickBackBtn(){
		super.onClickBackBtn();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		lsvBoard = null;
		mAdapter = null;
		activeBoard = null;

		imgUserAvatar = null;
		txtUserName = null;
		txtUserMail = null;
		btnSetting = null;

		onChangedBoardListener = null;
		onRefreshBoardListListener = null;
	}
}
