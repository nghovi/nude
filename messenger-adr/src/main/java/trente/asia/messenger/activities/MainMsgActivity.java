package trente.asia.messenger.activities;

import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import trente.asia.messenger.R;
import trente.asia.messenger.services.message.MessageDetailFragment;
import trente.asia.messenger.services.message.MessageFragment;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
import trente.asia.messenger.services.message.model.RealmBoardModel;
import trente.asia.messenger.services.message.model.RealmMessageModel;
import trente.asia.messenger.services.user.MsgLoginFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.UserModel;
import trente.asia.welfare.adr.pref.PreferencesAccountUtil;

public class MainMsgActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PreferencesAccountUtil prefAccUtil = new PreferencesAccountUtil(this);
		UserModel userModel = prefAccUtil.getUserPref();

		Bundle mExtras = getIntent().getExtras();

		if(!CCStringUtil.isEmpty(userModel.key)){
			if(mExtras != null){
				showFragment(mExtras);
			}else{
				MessageFragment messageFragment = new MessageFragment();
				addFragment(messageFragment);
			}
		}else{
			addFragment(new MsgLoginFragment());
		}
	}

	private void showFragment(Bundle mExtras){
		String serviceCode = mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE);
		int key = Integer.parseInt(mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY));
		int parentKey = Integer.parseInt(mExtras.getString(WelfareConst.NotificationReceived.USER_INFO_NOTI_PARENT_KEY));
		if(WelfareConst.NotificationType.MS_NOTI_NEW_MESSAGE.equals(serviceCode)){
			MessageFragment messageFragment = new MessageFragment();
			RealmBoardModel boardModel = new RealmBoardModel();
			boardModel.key = key;
			messageFragment.setActiveBoard(boardModel);
			addFragment(messageFragment);
		}else if(WelfareConst.NotificationType.MS_NOTI_NEW_COMMENT.equals(serviceCode)){
			MessageDetailFragment messageDetail = new MessageDetailFragment();
			RealmMessageModel activeMessage = Realm.getDefaultInstance().where(RealmMessageModel.class).equalTo("key", key).findFirst();
			messageDetail.setActiveMessage(activeMessage);
			messageDetail.isClickNotification = true;
			addFragment(messageDetail);
		}
	}
}
