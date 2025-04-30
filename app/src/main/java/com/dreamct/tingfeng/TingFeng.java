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
    public void initViewModelIfNeeded() {
        if (sharedViewModel == null) {
            sharedViewModel = new NotificationViewModel();
        }
    }
    public static boolean SwitchState = false;
    public static boolean devMode = false;
}
