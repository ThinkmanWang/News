package com.thinkman.chinabestnews;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.thinkman.chinabestnews.models.NewsListModel;
import com.thinkman.chinabestnews.models.NewsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "HEHE";

	Context mContext = null;
	@Override
	public void onReceive(Context context, Intent intent) {

		mContext = context;

        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			Gson gson = new Gson();
			final NewsModel news = gson.fromJson(bundle.getString(JPushInterface.EXTRA_MESSAGE), NewsModel.class);
			addNotification1(news);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private static int requestCode = 1;
	private void addNotification1(NewsModel news) {
		NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification myNotify = new Notification();
		myNotify.icon = R.mipmap.ic_launcher;
		myNotify.tickerText = news.getTitle();
		myNotify.when = System.currentTimeMillis();
		myNotify.flags = Notification.FLAG_AUTO_CANCEL;

		RemoteViews rv = new RemoteViews(mContext.getPackageName(),
				R.layout.notification_layout);
		rv.setTextViewText(R.id.text_content, news.getTitle());
		//rv.setTextViewText(R.id.text_content_time, StringUtils.longTime2String(System.currentTimeMillis() / 1000, StringUtils.dateFormat3));
		myNotify.contentView = rv;

		manager.notify(requestCode, myNotify);
	}

	public static final int NOTIFICATION_ID = 1;
	private void addNotification(NewsModel news) {
		// Use NotificationCompat.Builder to set up our notification.
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

		//icon appears in device notification bar and right hand corner of notification
		//builder.setSmallIcon(R.drawable.ic_stat_notification);

		// This intent is fired when notification is clicked
		//Intent intent = new Intent(MyReceiver.this, );
		//PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// Set the intent that will fire when the user taps the notification.
		//builder.setContentIntent(pendingIntent);

		// Large icon appears on the left of the notification
		//builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

		// Content title, which appears in large type at the top of the notification
		builder.setContentTitle("中国最大的新闻客户端");

		// Content text, which appears in smaller text below the title
		builder.setContentText(news.getTitle());

		// The subtext, which appears under the text on newer devices.
		// This will show-up in the devices with Android 4.2 and above only
		builder.setSubText("");

		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

		// Will display the notification in the notification bar
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}

}
