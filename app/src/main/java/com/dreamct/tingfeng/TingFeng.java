package com.dreamct.tingfeng;

import android.app.Application;

public class TingFeng extends Application {
    private NotificationViewModel sharedViewModel;
    public NotificationViewModel getSharedViewModel() {
        if (sharedViewModel == null) {
            sharedViewModel = new NotificationViewModel();
        }
        return sharedViewModel;
    }
}
