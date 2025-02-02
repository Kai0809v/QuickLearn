package com.example.learn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    private LinearLayout fabMenu;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabChild1;
    private FloatingActionButton fabChild2;//对悬浮按钮的声明
    private boolean isFabOpen = false;

    private List<Notification> notifications = new ArrayList<>();
    private NotificationAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notification notification = intent.getParcelableExtra("notification");
            if (notification != null) {
                notifications.add(notification);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);

        // 注册广播接收器
        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver, new IntentFilter("com.example.learn.RECEIVE_NOTIFICATION"));

        // 悬浮按钮对应的代码
        //FloatingActionButton fabMain = findViewById(R.id.fab_main);
        fabMenu = findViewById(R.id.fab_menu);
        fabMain = findViewById(R.id.fab_main);
        fabChild1 = findViewById(R.id.fab_child1);
        fabChild2 = findViewById(R.id.fab_child2);


        fabMain.setOnClickListener(v -> toggleFab());
        fabChild1.setOnClickListener(v -> Toast.makeText(history.this, "Child 1 clicked", Toast.LENGTH_SHORT).show());

        fabChild2.setOnClickListener(v -> Toast.makeText(history.this, "Child 2 clicked", Toast.LENGTH_SHORT).show());

    }

    //触发状态转换
    private void toggleFab() {
        if (isFabOpen) {
            closeFabMenu();
        } else {
            openFabMenu();
        }
        isFabOpen = !isFabOpen;
    }
    //
    private void openFabMenu() {//开！！！将大局逆转吧（滑稽）
        fabMenu.setVisibility(View.VISIBLE);

        // 计算子按钮总高度（包括间距）
        float totalHeight = 0;
        for (int i = 0; i < fabMenu.getChildCount(); i++) {
            View child = fabMenu.getChildAt(i);
            totalHeight += child.getHeight() + ((LinearLayout.LayoutParams) child.getLayoutParams()).bottomMargin;
        }

        // 子按钮向上展开动画
        fabMenu.animate()
                .translationY(-totalHeight)//设置动画方向
                .alpha(1f)//透明度渐变：配合 alpha 实现渐入渐出效果
                .setDuration(300)//设置动画时间
                .setInterpolator(new AccelerateDecelerateInterpolator());//设置动画插值器

        // 主按钮旋转动画
        fabMain.animate()
                .rotationBy(180f)//设置旋转角度
                .setDuration(300);
    }

    private void closeFabMenu() {
        // 子按钮向下收回动画
        fabMenu.animate()
                .translationY(0)
                .alpha(0f)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(() -> fabMenu.setVisibility(View.INVISIBLE));

        // 主按钮旋转还原
        fabMain.animate()
                .rotationBy(180f)
                .setDuration(300);
    }


    // 销毁广播接收器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }



}