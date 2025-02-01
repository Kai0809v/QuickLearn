package com.example.learn;

public class Notification {
    private String appName;
    private String content;
    private String time;

    public Notification(String appName, String content, String time) {
        this.appName = appName;
        this.content = content;
        this.time = time;
    }

    public String getAppName() {
        return appName;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}