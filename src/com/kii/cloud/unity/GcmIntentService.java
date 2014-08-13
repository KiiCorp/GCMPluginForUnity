package com.kii.cloud.unity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Comment lines is a example how to display message in the status bar.
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
			KiiPushUnityPlugin.getInstance().sendPushNotification(this, message);
			if (!this.isForeground()) {
				this.showNotificationArea(message);
			}
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
	private void showNotificationArea(String message) {
		NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null) {
			
			String notificationTitle = "";
			String notificationText = "";
			
			String launchClassName = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName()).getComponent().getClassName();
			ComponentName componentName = new ComponentName(this.getPackageName(), launchClassName);
			Intent notificationIntent = (new Intent()).setComponent(componentName);
			notificationIntent.putExtra("notificationData", message);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			int icon = this.getIcon();
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setContentIntent(pendingIntent)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setAutoCancel(true)
				.setWhen(System.currentTimeMillis())
				.setSmallIcon(icon)
				.setContentTitle(notificationTitle)
				.setContentText(notificationText);
			notificationManager.notify(0, notificationBuilder.build());
		}
	}
	/**
	 * Gets resource id of launcher icon.
	 * 
	 * @param context
	 * @return
	 */
	private int getIcon() {
		return this.getResources().getIdentifier("ic_launcher", "drawable", this.getPackageName());
	}
	/**
	 * Checks if the application is on foreground.
	 * 
	 * @return
	 */
	private boolean isForeground(){
		ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info : processInfoList){
			if(info.processName.equals(this.getPackageName()) && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
				return true;
			}
		}
		return false;
	}

}
