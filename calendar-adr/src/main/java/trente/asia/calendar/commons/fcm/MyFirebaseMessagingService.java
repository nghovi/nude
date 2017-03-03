package trente.asia.calendar.commons.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import asia.chiase.core.util.CCJsonUtil;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.util.CsMsgUtil;
import trente.asia.calendar.R;
import trente.asia.calendar.commons.activites.MainClActivity;
import trente.asia.welfare.adr.define.WelfareConst;
import trente.asia.welfare.adr.models.FcmNotificationModel;

/**
 * MyFirebaseMessagingService
 *
 * @author TrungND
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService{

	private final String	TAG	= "MessagingService";
	private String			mNoticeType;
	private String			mKey;
	private String			mParentKey;

	public void onMessageReceived(RemoteMessage remoteMessage){

		Log.i(TAG, "============Start notification=============");

		// Check if message contains a data payload.
		if(remoteMessage.getData().size() > 0){
			/* Get notification data from FCM */
			String notification = remoteMessage.getData().get("bodyData");
			mNoticeType = CCStringUtil.toString(remoteMessage.getData().get("type"));
			mParentKey = CCStringUtil.toString(remoteMessage.getData().get("parentKey"));
			mKey = CCStringUtil.toString(remoteMessage.getData().get("key"));
			if(!CCStringUtil.isEmpty(notification)){
				sendNotification(notification, mNoticeType, mKey, mParentKey);
			}
		}
	}

	private void sendNotification(String notification, String noticeType, String key, String parentKey){

		FcmNotificationModel model = CCJsonUtil.convertToModel(notification, FcmNotificationModel.class);

		Intent intent = new Intent(this, MainClActivity.class);
		intent.putExtra(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE, noticeType);
		intent.putExtra(WelfareConst.NotificationReceived.USER_INFO_NOTI_PARENT_KEY, parentKey);
		intent.putExtra(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY, key);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		int requestID = (int)System.currentTimeMillis();
		PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		String content = "";
		if(!CCStringUtil.isEmpty(model.body_loc_key)){
			content = CsMsgUtil.message(this, model.body_loc_key, model.body_loc_args);
		}
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.app_name)).setContentText(content).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(WelfareConst.NOTIFICATION_ID, notificationBuilder.build());
	}

}
