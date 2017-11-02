package nguyenhoangviet.vpcorp.calendar.commons.fcm;

import java.io.IOException;

import com.bluelinelabs.logansquare.LoganSquare;
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
import android.widget.RemoteViews;

import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.util.CsMsgUtil;
import nguyenhoangviet.vpcorp.calendar.R;
import nguyenhoangviet.vpcorp.calendar.commons.activites.MainClActivity;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.FcmNotificationModel;

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

		FcmNotificationModel model = null;
		try{
			model = LoganSquare.parse(notification, FcmNotificationModel.class);
			Intent intent = new Intent(this, MainClActivity.class);
			intent.putExtra(WelfareConst.NotificationReceived.USER_INFO_NOTI_TYPE, noticeType);
			intent.putExtra(WelfareConst.NotificationReceived.USER_INFO_NOTI_PARENT_KEY, parentKey);
			intent.putExtra(WelfareConst.NotificationReceived.USER_INFO_NOTI_KEY, key);

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			int requestID = (int)System.currentTimeMillis();
			PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

			String content = "";
			String keyString = model.body_loc_key;
			String[] args = model.body_loc_args;
			if(!CCStringUtil.isEmpty(keyString)){
				// if("FM_CL_003".equals(keyString) || "FM_CL_004".equals(keyString)){
				// int scheduleNum = Integer.parseInt(args[0]);
				// int todoNum = Integer.parseInt(args[1]);
				// if(scheduleNum == 0){
				// keyString = keyString + "2";
				// args = new String[]{args[1]};
				// }
				// if(todoNum == 0){
				// keyString = keyString + "1";
				// args = new String[]{args[0]};
				// }
				//
				// }
				content = CsMsgUtil.message(this, keyString, args);
			}

			RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.item_push_notification);
			contentView.setImageViewResource(R.id.image, R.drawable.pn_icon);
			contentView.setTextViewText(R.id.title, getString(R.string.app_name));
			contentView.setTextViewText(R.id.text, content);

			Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)new NotificationCompat.Builder(this).setSmallIcon(R.drawable.pn_icon).setContent(contentView).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);

			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(WelfareConst.NOTIFICATION_ID, notificationBuilder.build());
		}catch(IOException e){
			e.printStackTrace();
		}

	}

}
