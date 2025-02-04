package com.example.learn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// NotificationDatabaseHelper.java
public class NotificationDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notifications.db";
    private static final int DB_VERSION = 2;// 数据库版本号

    // 创建表语句
    private static final String CREATE_TABLE =
            "CREATE TABLE notifications (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "package_name TEXT NOT NULL," +
                    "app_name TEXT NOT NULL," +
                    "title TEXT," +
                    "content TEXT," +
                    "timestamp LONG NOT NULL);";

    public NotificationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);// 创建表
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // 执行从版本1到版本2的迁移操作（如新增字段）
            db.execSQL("ALTER TABLE notifications ADD COLUMN new_column TEXT");
        }
        if (oldVersion < 3) {
            // 版本2到版本3的迁移
        }
    }

    public long insertNotification(NotificationModel notification) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("package_name", notification.getPackageName());
        values.put("app_name", notification.getAppName());
        values.put("title",notification.getTitle());
        values.put("content", notification.getContent());
        values.put("timestamp", notification.getTimestamp());
        return db.insert("notifications", null, values);
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
        return list;
    }
}