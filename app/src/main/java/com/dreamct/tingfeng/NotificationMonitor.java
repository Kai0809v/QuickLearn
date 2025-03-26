package com.dreamct.tingfeng;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationMonitor extends NotificationListenerService {
    private static final String TAG = "NotificationMonitor";
    //private NotificationDatabaseHelper dbHelper;
    private static final String CHANNEL_ID = "monitor";
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

            //dbHelper.insertNotification(notification);//将数据存入数据库
            viewModel.insertNotification(notification); // 使用 ViewModel 插入数据,触发数据更新
            System.out.println("monitor:存入数据库");
            Toast.makeText(this, "测试：已存入数据库", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "处理通知错误: " + e.getMessage());
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "com.dreamct.tingfeng.ACTION_DB_TEST".equals(intent.getAction())) {
            dbTest();
        }
        return START_STICKY;
    }

    public void dbTest(){
        //NotificationDatabaseHelper dbHelper = new NotificationDatabaseHelper(this);
        String appName = "听风";
        String packageName = "com.dreamct.tingfeng";
        String title ="数据库测试";
        String content = "数据库测试";
        long timestamp = System.currentTimeMillis();
        NotificationModel testNotification = new NotificationModel();
        testNotification.setAppName(appName);
        testNotification.setPackageName(packageName);
        testNotification.setTitle(title);
        testNotification.setContent(content);
        testNotification.setTimestamp(timestamp);
        // 在后台线程插入数据
        new Thread(() -> {
            try {
                viewModel.insertNotification(testNotification);
                //dbHelper.insertNotification(testNotification);
                System.out.println("测试数据已插入");
            } catch (Exception e) {
                Log.e(TAG, "插入测试数据失败: " + e.getMessage());
            }
        }).start();

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
        super.onListenerConnected();
        // 服务连接时触发
        System.out.println("通知监听服务已连接");
        // 可以在这里添加初始化逻辑或通知界面更新
        Toast.makeText(this, "通知监听服务已连接", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        // 服务断开时触发
        System.out.println("通知监听服务已断开");
        // 可以在这里添加重连逻辑或提示用户
        Toast.makeText(this, "通知监听服务已断开", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "通知监听服务",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("用于持续监听系统通知");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /**创建通知*/
    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("通知监听服务")
                .setContentText("正在监听系统通知")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // 替换为你的通知图标
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

}