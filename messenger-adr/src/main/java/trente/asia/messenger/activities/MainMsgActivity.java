package trente.asia.messenger.activities;

import android.os.Bundle;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.messenger.R;
import trente.asia.messenger.services.message.MessageDetailFragment;
import trente.asia.messenger.services.message.MessageFragment;
import trente.asia.messenger.services.message.model.BoardModel;
import trente.asia.messenger.services.message.model.MessageContentModel;
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
				addFragment(new MessageFragment());
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
			BoardModel boardModel = new BoardModel(key);
			messageFragment.setActiveBoard(boardModel);
			addFragment(messageFragment);
		}else if(WelfareConst.NotificationType.MS_NOTI_NEW_COMMENT.equals(serviceCode)){
			MessageDetailFragment messageFragment = new MessageDetailFragment();
			RealmMessageModel activeMessage = new RealmMessageModel();
			activeMessage.key = key;
			activeMessage.boardId = parentKey;
			messageFragment.setActiveMessage(activeMessage);
			messageFragment.isClickNotification = true;
			addFragment(messageFragment);
		}
	}
}
