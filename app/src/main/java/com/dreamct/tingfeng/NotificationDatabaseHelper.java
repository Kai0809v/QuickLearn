package com.dreamct.tingfeng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notifications.db";
    private static final int DB_VERSION = 2;// 数据库版本号
    //private final MutableLiveData<List<NotificationModel>> notificationsLiveData = new MutableLiveData<>();

    // 创建表语句
    //卡片对应信息的更改代表着对应数据库的更改，所以改完信息后，数据库也要更新，要么将软件数据清除重新生成数据库
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS notifications (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "package_name TEXT NOT NULL," +
                    "app_name TEXT NOT NULL," +
                    "title TEXT," +
                    "content TEXT," +
                    "timestamp LONG NOT NULL);";

    public NotificationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);// 数据库名称和版本号
    }

    /**删除旧数据**/
    public void deleteAllNotifications() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("notifications", null, null); // 删除所有数据
        //loadNotificationsAsync();
        db.close();// 关闭数据库连接
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);// 创建表
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库
    }

    public long insertNotification(NotificationModel notification) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", notification.getPackageName());
        values.put("app_name", notification.getAppName());
        values.put("title",notification.getTitle());
        values.put("content", notification.getContent());
        values.put("timestamp", notification.getTimestamp());
        long result = db.insert("notifications", null, values);
        System.out.println("db:数据库插入");
        db.close();
        return result;
    }

    public List<NotificationModel> getAllNotifications() {
        List<NotificationModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("notifications",
                new String[]{"app_name","title","content", "timestamp"},
                null, null, null, null, "timestamp DESC");

        while (cursor.moveToNext()) {
            NotificationModel item = new NotificationModel();
            item.setAppName(cursor.getString(0));
            item.setTitle(cursor.getString(1));
            item.setContent(cursor.getString(2));
            item.setTimestamp(cursor.getLong(3));
            list.add(item);
        }
        cursor.close();
        db.close();
        return list;
    }

    /*
     * 筛选（待完成*/
    //SELECT * FROM notifications WHERE app_name = '微信' AND content LIKE '%红包%';
}
