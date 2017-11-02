package nguyenhoangviet.vpcorp.messenger.fragment;

import android.os.Bundle;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import nguyenhoangviet.vpcorp.android.define.CsConst;
import nguyenhoangviet.vpcorp.messenger.BuildConfig;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.commons.defines.MsConst;
import nguyenhoangviet.vpcorp.messenger.services.message.MessageFragment;
import nguyenhoangviet.vpcorp.messenger.services.message.model.BoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.MessageContentModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmBoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmMessageModel;
import nguyenhoangviet.vpcorp.messenger.services.user.MsgLoginFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareFragment;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.define.WfErrorConst;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

/**
 * AbstractMsgFragment
 *
 * @author TrungND
 */
public class AbstractMsgFragment extends WelfareFragment{

	public PreferencesAccountUtil	preferencesAccountUtil;
	public static String			lastMessageUpdateDate;
	public static int				activeBoardId;
	public int						latestMessageKey	= 0;
	public int						startMessageKey		= 0;
	private static Timer			mTimer;
	public static Realm				mRealm;
	private final int				TIME_RELOAD			= 10000;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		preferencesAccountUtil = new PreferencesAccountUtil(getContext());
		host = BuildConfig.HOST;
		if(mRealm == null){
			mRealm = Realm.getDefaultInstance();
		}
	}

	@Override
	protected void gotoSignIn(){
		super.gotoSignIn();
		gotoFragment(new MsgLoginFragment());
	}

	@Override
	protected void commonNotSuccess(JSONObject response, String url){
		String returnCd = response.optString(CsConst.RETURN_CODE_PARAM);
		if(WfErrorConst.MS_ERR_CODE_DEPT_CHANGED.equals(returnCd) || WfErrorConst.MS_ERR_CODE_CONTENT_DELETED.equals(returnCd)){
			if(WfErrorConst.MS_ERR_CODE_CONTENT_DELETED.equals(returnCd)){
				alertDialog.setMessage(getString(R.string.msg_common_error_ms002));
				alertDialog.show();
			}
			emptyBackStack();
			gotoFragment(new MessageFragment());
		}else{
			super.commonNotSuccess(response, url);
		}
	}

	@Override
	protected String getServiceCd(){
		return WelfareConst.SERVICE_CD_MS;
	}

	public void loadMessageLatest(){

		JSONObject jsonObject = new JSONObject();
		try{
			jsonObject.put("boardId", activeBoardId);
			if(lastMessageUpdateDate != null && !lastMessageUpdateDate.isEmpty()){
				jsonObject.put("lastMessageUpdateDate", lastMessageUpdateDate);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}

		requestLoad(MsConst.API_MESSAGE_LATEST, jsonObject, false);
	}

	@Override
	protected void successLoad(JSONObject response, String url){

		if(MsConst.API_MESSAGE_LATEST.equals(url)){
			try{
				lastMessageUpdateDate = response.optString("lastMessageUpdateDate");

				List<MessageContentModel> lstMessage = LoganSquare.parseList(response.optString("contents"), MessageContentModel.class);
				if(lstMessage.size() > 0){

					List<RealmMessageModel> messages = new ArrayList<>();
					for(MessageContentModel message : lstMessage){
						messages.add(new RealmMessageModel(message));
					}
					saveMessageToDB(messages);

					List<RealmMessageModel> newMessages = new ArrayList<>();
					List<RealmMessageModel> updateMessages = new ArrayList<>();

					for(RealmMessageModel message : messages){
						if(message.key <= latestMessageKey){
							if(message.key >= startMessageKey){
								updateMessages.add(message);
							}
						}else{
							newMessages.add(message);
							latestMessageKey = message.key;
						}
					}

					if(updateMessages.size() > 0){
						updateMessages(updateMessages);
						updateDetailMessage();
					}

					if(newMessages.size() > 0){
						addNewMessage(newMessages);
					}

				}

				List<BoardModel> listBoard = LoganSquare.parseList(response.optString("boards"), BoardModel.class);

				mRealm.beginTransaction();
				List<RealmBoardModel> realmBoards = new ArrayList<>();
				for(BoardModel boardModel : listBoard){
					RealmBoardModel realmBoard = mRealm.where(RealmBoardModel.class).equalTo("key", boardModel.key).findFirst();
					if(realmBoard != null){
						realmBoard.update(boardModel);
					}else{
						realmBoard = new RealmBoardModel(boardModel);
					}

					if(activeBoardId == boardModel.key && !"".equals(lastMessageUpdateDate)){
						realmBoard.lastMessageUpdateDate = lastMessageUpdateDate;
					}
					mRealm.copyToRealmOrUpdate(realmBoard);

					realmBoards.add(realmBoard);
				}
				mRealm.commitTransaction();

				updateBoardList(realmBoards);

				RealmResults<RealmBoardModel> localBoards = mRealm.where(RealmBoardModel.class).findAll();
				for(RealmBoardModel localBoard : localBoards){
					boolean isDeleted = true;
					for(RealmBoardModel realmBoard : realmBoards){
						if(realmBoard.key == localBoard.key){
							isDeleted = false;
							break;
						}
					}
					if(isDeleted){
						mRealm.beginTransaction();
						localBoard.deleteFromRealm();
						mRealm.commitTransaction();
					}
				}

			}catch(IOException e){
				e.printStackTrace();
			}

		}else{
			super.successLoad(response, url);
		}
	}

	public void saveMessageToDB(List<RealmMessageModel> lstMessage){
		mRealm.beginTransaction();
		for(RealmMessageModel message : lstMessage){
			mRealm.copyToRealmOrUpdate(message);
		}
		mRealm.commitTransaction();
	}

	protected void updateBoardList(List<RealmBoardModel> boards){
	}

	protected void updateMessages(List<RealmMessageModel> updateMessages){
	}

	protected void updateDetailMessage(){
	}

	protected void addNewMessage(List<RealmMessageModel> newMessages){
	}

	private void log(String msg){
		Log.e("AbstractMsg", msg);
	}

	public void startTimer(){
		loadMessageLatest();
		if(mTimer == null){
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {

				@Override
				public void run(){
					loadMessageLatest();
				}
			}, TIME_RELOAD, TIME_RELOAD);
		}
	}

	public void stopTimer(){
		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
		}
	}
}
