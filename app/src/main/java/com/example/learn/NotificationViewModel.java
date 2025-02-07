package com.example.learn;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NotificationViewModel extends ViewModel {
    private NotificationDatabaseHelper dbHelper;
    private LiveData<List<NotificationModel>> notifications;

    public void init(Context context) {
        dbHelper = new NotificationDatabaseHelper(context);
        notifications = dbHelper.getNotificationsLiveData();
    }

    public LiveData<List<NotificationModel>> getNotifications() {
        return notifications;
    }

    public void deleteAllNotifications() {
        new Thread(() -> {
            dbHelper.deleteAllNotifications();
            dbHelper.notifyDataChanged(); // 触发数据更新
        }).start();
    }
}