package com.kii.cloud.unity;

//import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
//import android.app.NotificationManager;
//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Comment lines is a sample how to display message in the status bar.
 * 
 * @author noriyoshi.fukuzaki@kii.com
 */
public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super("KiiGcmIntentService");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("GcmIntentService", "#####onHandleIntent");
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		Log.d("GcmIntentService", "#####messageType=" + messageType);
		if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
			Bundle extras = intent.getExtras();
			String message = this.toJson(extras).toString();
			KiiPushUnityPlugin.getInstance().sendPushNotification(message);
//			if (!this.isForeground()) {
//				this.showNotificationArea(message);
//			}
		}
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}
	private JSONObject toJson(Bundle bundle) {
		JSONObject json = new JSONObject();
		for (String key : bundle.keySet()) {
			try {
				json.put(key, bundle.get(key));
			} catch (JSONException ignore) {
			}
		}
		return json;
	}
	
//	/**
//	 * Shows a received message in the notification area.
//	 * 
//	 * @param json
//	 */
//	private void showNotificationArea(String json) {
//		NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
//		if (notificationManager != null) {
//			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//			.setSmallIcon(android.R.drawable.ic_menu_info_details)
//			.setContentTitle("New message received!!")
//			.setContentText(json);
//			notificationManager.notify(0, notificationBuilder.build());
//		}
//	}
//	/**
//	 * Checks if the KiiChat is on foreground.
//	 * 
//	 * @return
//	 */
//	private boolean isForeground(){
//		ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//		List<RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
//		for(RunningAppProcessInfo info : processInfoList){
//			if(info.processName.equals(this.getPackageName()) && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
//				return true;
//			}
//		}
//		return false;
//	}

}
