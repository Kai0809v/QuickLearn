package com.example.learn;

import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class KaiNotificationListenerService extends NotificationListenerService {

    private static final String TAG = "NotificationService";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        // 解析通知数据
        Notification notification = parseNotification(sbn);
        if (notification != null) {
            // 将通知数据传递给 Activity（这里需要使用适当的通信方式，例如广播）
            sendNotificationData(notification);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(TAG, "Notification removed: " + sbn.getPackageName());
    }

    private Notification parseNotification(StatusBarNotification sbn) {
        try {
            String appName = sbn.getPackageName();
            String content = sbn.getNotification().extras.getString("android.text");
            String time = String.valueOf((System.currentTimeMillis())); // 当前时间戳

            // 如果没有内容，则尝试获取标题
            if (content == null) {
                content = sbn.getNotification().extras.getString("android.title");
            }

            return new Notification(appName, content, time);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendNotificationData(Notification notification) {
        // 这里可以通过广播或其他方式将通知数据发送到 Activity
        // 例如：发送广播
        Intent intent = new Intent("com.example.learn.NOTIFICATION_RECEIVED");
        intent.putExtra("notification", (CharSequence) notification);
        sendBroadcast(intent);
    }
}