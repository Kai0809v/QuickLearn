package com.example.learn;

import static java.sql.DriverManager.println;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class history extends AppCompatActivity {
    //***********    按钮声明区   *************************
    private LinearLayout fabMenu;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabChild1;
    private FloatingActionButton fabChild2;//对悬浮按钮的声明
    private FloatingActionButton fabChild3;
    private FloatingActionButton fabChild4;
    private FloatingActionButton fabChild5;

    //*********************************************
    private NotificationViewModel viewModel;
    private boolean isFabOpen = false;

    private NotificationDatabaseHelper dbHelper;

    private List<NotificationModel> notifications = new ArrayList<>();
    private NotificationAdapter adapter;
    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadNotifications();
        }
    };//不知道这一段有没有用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.page_history);
        // 初始化 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
        // 初始化数据库
       dbHelper = new NotificationDatabaseHelper(this);

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        viewModel.init(getApplicationContext());

        // 观察数据变化
        viewModel.getNotifications().observe(this, data -> {
            adapter.setNotifications(data);
            adapter.notifyDataSetChanged();
        });


        // 注册广播接收器（添加标志）：
        // 如果注册了记得取消注册接收器；相关代码在末尾
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // Android 13+ 需要显式声明导出属性
//            registerReceiver(
//                    updateReceiver,
//                    new IntentFilter("NOTIFICATION_UPDATE"),
//                    Context.RECEIVER_NOT_EXPORTED // 限制为仅本应用可接收
//            );
//        } else {
//            // 旧版本无需标志
//            registerReceiver(updateReceiver, new IntentFilter("NOTIFICATION_UPDATE"));
//        }

        // 首次加载数据
        loadNotifications();

        // 悬浮按钮对应的代码
        fabMenu = findViewById(R.id.fab_menu);
        fabMain = findViewById(R.id.fab_main);
        fabChild1 = findViewById(R.id.fab_child1);
        fabChild2 = findViewById(R.id.fab_child2);
        fabChild3 = findViewById(R.id.fab_child3);
        fabChild4 = findViewById(R.id.fab_child4);
        fabChild5 = findViewById(R.id.fab_child5);



        fabMain.setOnClickListener(v -> toggleFab());


        fabChild1.setOnClickListener(v -> {
            checkAndSendNotification();
        });
        fabChild2.setOnClickListener(v -> {
            new AlertDialog.Builder(history.this)
                    .setTitle("删除记录")
                    .setMessage("确定删除所有历史记录吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        new Thread(() -> {
                            dbHelper.deleteAllNotifications();
                            runOnUiThread(() -> {//Toast 必须在主线程（UI 线程）中显示
                                loadNotifications();//因为它涉及到与 UI 相关的操作
                                Toast.makeText(history.this, "已删除所有记录", Toast.LENGTH_SHORT).show();
                            });

                        }).start();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
        fabChild3.setOnClickListener(v -> {
            Huabing();
            //以后弄个筛选功能
        });
        fabChild4.setOnClickListener(v -> {
            Huabing();
            //以后弄个自定义
        });
        fabChild5.setOnClickListener(v -> loadNotifications());


    }
    private void loadNotifications() {
        dbHelper.getNotificationsLiveData().observe(this, data -> {
            adapter.setNotifications(data);
            adapter.notifyDataSetChanged();
        });
//        new Thread(() -> {
//            List<NotificationModel> data = dbHelper.getAllNotifications();
//            runOnUiThread(() -> {
//                adapter.setNotifications(data);
//                adapter.notifyDataSetChanged();
//            });
//        }).start();
    }

    private void Huabing() {
        Toast.makeText(history.this,"功能尚未实现", Toast.LENGTH_SHORT).show();
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
    private void openFabMenu() {//开！！！将大局逆转吧（滑稽）
        fabMenu.setVisibility(View.VISIBLE);

        // 计算子按钮总高度（包括间距）
        float totalHeight = 0;
        View child = fabMenu.getChildAt(1);
        float jianJu = ((LinearLayout.LayoutParams) child.getLayoutParams()).bottomMargin;
        totalHeight = child.getHeight()+jianJu;

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
    /**
    *判断是否有通知权限
     */
    private void checkAndSendNotification() {
        if (hasNotificationPermission()) {
            sendNotification("测试通知", "这是一则测试内容");
            loadNotifications();
            println("按钮一");
        } else {
            showRationaleDialog();
            //************
        }
    }

    // 显示解释性对话框，拒绝后的引导
    private void showRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("需要通知权限")
                .setMessage("用于发送测试通知，请允许权限")
                .setPositiveButton("继续授权", (dialog, which) -> {
                    // 再次请求权限
                    openNotificationSettings();
                })
                .setNegativeButton("取消", null)
                .show();
    }
    /**
     * 13+动态申请，低的跳转到通知设置页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openNotificationSettings() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ 可以直接请求权限
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    100
            );
        } else {
            // 低版本跳转到应用通知设置
            intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
    }

    // 显示被永久拒绝后的引导对话框
    private void showPermanentDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限被永久拒绝")
                .setMessage("您已永久拒绝通知权限，请前往设置手动开启")
                .setPositiveButton("去设置", (dialog, which) -> {
                    // 跳转到应用详情页（更精确的权限设置入口）
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .setNegativeButton("取消", null)
                .show();
    }



    /**检查通知权限状态
     */
    private boolean hasNotificationPermission() {
        // Android 13 (API 33) 及以上需要动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED;
        }
        // 低版本默认有权限（需要检查系统通知设置）
        return true;
    }


    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，发送通知
                sendNotification("标题", "内容");
            } else {
                // 展示解释性对话框

                    showPermanentDeniedDialog();

            }
        }
    }
    /**每次进入这个页面都会发送通知
    //@Override
    protected void onResume() {
        super.onResume();
        if (hasNotificationPermission()) {
            // 用户可能已手动开启权限
            sendNotification("欢迎回来", "通知权限已开启");
        }
    }
    */
    // 发送通知的方法
    private void sendNotification(String title, String content) {
        // 1. 创建通知渠道（Android 8.0+ 必需）
        String channelId = "export_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "测试通知",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("读取通知测试");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // 2. 构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.send_message) // 通知小图标
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // 点击后自动消失

        // 3. 添加点击动作（可选）
        Intent intent = new Intent(this, history.class);//点击通知后跳转的页面
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        builder.setContentIntent(pendingIntent);
        // 4. 发送通知
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());

    }

        // 销毁广播接收器
//        @Override
//        protected void onDestroy() {
//            super.onDestroy();
//            unregisterReceiver(updateReceiver);
//        }



}