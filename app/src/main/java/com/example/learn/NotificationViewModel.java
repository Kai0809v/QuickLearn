package com.example.learn;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NotificationViewModel extends ViewModel {
    private NotificationDatabaseHelper dbHelper;
    private MutableLiveData<List<NotificationModel>> notifications = new MutableLiveData<>();

    public void init(Context context) {
        dbHelper = new NotificationDatabaseHelper(context);
        // 初始加载数据
        System.out.println("viewmodel:初始化数据");
        loadNotifications();
    }

    public LiveData<List<NotificationModel>> getNotifications() {

        return notifications;
    }
    public void loadNotifications() {
        new Thread(() -> {
            List<NotificationModel> data = dbHelper.getAllNotifications();
            notifications.postValue(data);
            System.out.println("viewmodel:更新livedata");
        }).start();
    }
    public void insertNotification(NotificationModel notification) {
        new Thread(() -> {
            dbHelper.insertNotification(notification);
            loadNotifications(); // 插入后重新加载数据
        }).start();
    }
    public void deleteAllNotifications() {
        new Thread(() -> {
            dbHelper.deleteAllNotifications();
            loadNotifications(); // 删除后重新加载数据
        }).start();
    }
}