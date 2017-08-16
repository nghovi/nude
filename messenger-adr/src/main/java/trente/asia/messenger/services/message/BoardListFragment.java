package trente.asia.messenger.services.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asia.chiase.core.util.CCCollectionUtil;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import io.realm.RealmResults;
import trente.asia.android.view.ChiaseCheckableImageView;
import trente.asia.messenger.BuildConfig;
import trente.asia.messenger.R;
import trente.asia.messenger.commons.defines.MsConst;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.listener.OnChangedBoardListener;
import trente.asia.messenger.services.message.listener.OnRefreshBoardListListener;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.RealmBoardModel;
import trente.asia.messenger.services.message.view.BoardAdapter;
import trente.asia.messenger.services.user.MsgSettingFragment;
import trente.asia.messenger.services.user.listener.OnAddedContactListener;
import trente.asia.welfare.adr.activity.WelfareActivity;
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

	private ImageView					imgUserAvatar;
	private TextView					txtUserName;
	private TextView					txtUserMail;
	private ChiaseCheckableImageView	btnSetting;

	private String						mAvatarPath					= "";
	private OnChangedBoardListener		onChangedBoardListener;
	private OnRefreshBoardListListener	onRefreshBoardListListener	= new OnRefreshBoardListListener() {

																		@Override
																		public void onRefreshBoardListListener(List<RealmBoardModel> lstBoard){
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
				RealmBoardModel boardModel = mAdapter.getItem(position);
				activeBoardId = boardModel.key;
				prefAccUtil.set(MsConst.PREF_ACTIVE_BOARD_ID, activeBoardId + "");
				lsvBoard.setItemChecked(position, true);
				if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(boardModel, true);
			}
		});

		activeBoardId = Integer.parseInt(prefAccUtil.get(MsConst.PREF_ACTIVE_BOARD_ID));

		imgUserAvatar = (ImageView)getView().findViewById(R.id.img_id_myAvatar);
		txtUserName = (TextView)getView().findViewById(R.id.txt_loginUserName);
		txtUserMail = (TextView)getView().findViewById(R.id.txt_loginUserMail);
		btnSetting = (ChiaseCheckableImageView)getView().findViewById(R.id.btn_setting);
		btnSetting.setOnClickListener(this);
		mDlgProfile = new WfProfileDialog(activity);
		mDlgProfile.setDialogProfileDetail(50, 50);

		txtUserName.setText(myself.userName);
		txtUserMail.setText(myself.userMail);
		if(!CCStringUtil.isEmpty(myself.avatarPath)){
			mAvatarPath = myself.avatarPath;
			WfPicassoHelper.loadImage(activity, host + myself.avatarPath, imgUserAvatar, null);
		}
		imgUserAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				mDlgProfile.show(BuildConfig.HOST, myself.userName, myself.avatarPath);
			}
		});
	}

	private void log(String msg) {
        Log.e("BoardList", msg);
    }

	@Override
	protected void initData(){
		super.initData();
		loadBoardList();
	}

	private void loadBoardList(){
		RealmResults<RealmBoardModel> boardList = Realm.getDefaultInstance().where(RealmBoardModel.class).findAll();
		List<RealmBoardModel> boards = new ArrayList<>();
		for(RealmBoardModel board : boardList){
			boards.add(board);
		}
		showBoards(boards);
	}

	private void showBoards(List<RealmBoardModel> boards){
		if(!CCCollectionUtil.isEmpty(boards)){
			mAdapter = new BoardAdapter(activity, boards, new OnAvatarClickListener() {

				@Override
				public void OnAvatarClick(String userName, String avatarPath){
					mDlgProfile.show(BuildConfig.HOST, userName, avatarPath);
				}
			});
			lsvBoard.setAdapter(mAdapter);

			// set active board
			if(activeBoardId != -1){
				for(int i = 0; i < boards.size(); i++){
					RealmBoardModel boardModel = boards.get(i);
					if(activeBoardId == boardModel.key){
						lsvBoard.setItemChecked(i, true);
						break;
					}
				}
			} else {
				lsvBoard.setItemChecked(0, true);
				activeBoardId = boards.get(0).key;
				prefAccUtil.set(MsConst.PREF_ACTIVE_BOARD_ID, activeBoardId + "");
				if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(boards.get(0), false);
			}
			checkUnreadMessage(boards);
		}
	}

	public void updateBoards(List<RealmBoardModel> boards){
		if(mAdapter == null){
			showBoards(boards);
		}else{
			mAdapter.setBoardList(boards);
			for(int i = 0; i < boards.size(); i++){
				RealmBoardModel boardModel = boards.get(i);
				if(activeBoardId == boardModel.key){
					lsvBoard.setItemChecked(i, true);
					break;
				}
			}
			checkUnreadMessage(boards);
		}
	}

	@Override
	protected void successLoad(JSONObject response, String url){

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

	public void onAddedContactListener(RealmBoardModel boardModel){
		if (mAdapter != null) {
			mAdapter.add(boardModel, 0);
		}
		lsvBoard.setItemChecked(0, true);
		activeBoardId = boardModel.key;
		preferencesAccountUtil.set(MsConst.PREF_ACTIVE_BOARD_ID, activeBoardId + "");
	}

	private void refreshBoardList(List<RealmBoardModel> lstBoard){
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
					mDlgProfile.show(BuildConfig.HOST, userName, avatarPath);
				}
			});
			lsvBoard.setAdapter(mAdapter);

			boolean isActive = false;
			for(int i = 0; i < lstBoard.size(); i++){
				RealmBoardModel boardModel = lstBoard.get(i);
				if(boardModel.key == activeBoardId){
					lsvBoard.setItemChecked(i, true);
					isActive = true;
					break;
				}
			}

			if(!isActive){
				lsvBoard.setItemChecked(0, true);
				if(onChangedBoardListener != null) onChangedBoardListener.onChangedBoard(lstBoard.get(0), true);
			}

			checkUnreadMessage(lstBoard);
		}
	}

	private void checkUnreadMessage(List<RealmBoardModel> lstBoard){
		Integer unreadMessage = 0;
		for(RealmBoardModel boardModel : lstBoard){
			unreadMessage += CCNumberUtil.checkNull(boardModel.boardUnread);
		}
		if(onChangedBoardListener != null) onChangedBoardListener.onRefreshUnreadMessage(unreadMessage);
	}

	public OnRefreshBoardListListener getOnRefreshBoardListListener(){
		return onRefreshBoardListListener;
	}

	public void setActiveBoard(int activeBoardId){
		this.activeBoardId = activeBoardId;
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

		imgUserAvatar = null;
		txtUserName = null;
		txtUserMail = null;
		btnSetting = null;

		onChangedBoardListener = null;
		onRefreshBoardListListener = null;
	}
}
