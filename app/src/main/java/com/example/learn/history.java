package com.example.learn;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 准备数据
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("通知应用 1", "这是一条通知内容。", "2024-10-01 10:00"));
        notifications.add(new Notification("通知应用 2", "这是另一条通知内容。", "2024-10-02 11:00"));
        notifications.add(new Notification("通知应用 3", "这是第三条通知内容。", "2024-10-03 12:00"));

        // 设置适配器
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
    }
}