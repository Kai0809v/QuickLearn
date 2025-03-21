package com.dreamct.tingfeng;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationMonitor extends NotificationListenerService {
    private static final String TAG = "NotificationMonitor";
    //private NotificationDatabaseHelper dbHelper;
    private NotificationViewModel viewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        //dbHelper = new NotificationDatabaseHelper(this);
        TingFeng app = (TingFeng) getApplication();

        // 确保每次服务创建时都获取最新的 ViewModel 实例
        //app.initViewModelIfNeeded();

        viewModel = app.getSharedViewModel();
        viewModel.init(this);
        System.out.println("服务已创建，ViewModel 初始化完成");
    }



    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {

            // 获取包名
            String packageName = sbn.getPackageName();

            // 获取应用名称
            String appName = getApplicationName(packageName);

            //获取通知标题
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString(android.app.Notification.EXTRA_TITLE);
            if (title == null) {title = "无标题";}

            // 获取通知内容
            String content = extractNotificationContent(sbn.getNotification());
            if (content == null) {content = "无内容";}
            // 获取时间戳
            long timestamp = sbn.getPostTime();

            // 存入数据库
            NotificationModel notification = new NotificationModel();
            notification.setAppName(appName);
            notification.setPackageName(packageName);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setTimestamp(timestamp);

            // 通知界面更新（通过广播）
            //sendBroadcast(new Intent("NOTIFICATION_UPDATE"));

            //dbHelper.insertNotification(notification);//将数据存入数据库
            viewModel.insertNotification(notification); // 使用 ViewModel 插入数据,触发数据更新
            System.out.println("monitor:存入数据库");
        } catch (Exception e) {
            Log.e(TAG, "处理通知错误: " + e.getMessage());
        }
    }

    private String getApplicationName(String packageName) {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return pm.getApplicationLabel(ai).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName; // 无法获取时返回包名
        }
    }

    private String extractNotificationContent(android.app.Notification notification) {
        Bundle extras = notification.extras;
        if (extras != null) {
            // 优先获取长文本
            CharSequence text = extras.getCharSequence(android.app.Notification.EXTRA_TEXT);
            if (text != null) return text.toString();

            // 没有长文本时获取标题
            text = extras.getCharSequence(android.app.Notification.EXTRA_TITLE);
            if (text != null) return text.toString();
        }
        return "无文本内容";
    }

    @Override
    public void onListenerConnected() {
        // 服务连接时触发
    }

    @Override
    public void onListenerDisconnected() {
        // 服务断开时触发
    }
}