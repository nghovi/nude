package nguyenhoangviet.vpcorp.messenger.activities;

import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import asia.chiase.core.util.CCStringUtil;
import io.realm.Realm;
import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.services.message.MessageDetailFragment;
import nguyenhoangviet.vpcorp.messenger.services.message.MessageFragment;
import nguyenhoangviet.vpcorp.messenger.services.message.model.BoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.MessageContentModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmBoardModel;
import nguyenhoangviet.vpcorp.messenger.services.message.model.RealmMessageModel;
import nguyenhoangviet.vpcorp.messenger.services.user.MsgLoginFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.pref.PreferencesAccountUtil;

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
