package com.dreamct.tingfeng

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.sidesheet.SideSheetDialog
import java.io.File

class MainActivity : ComponentActivity() {
    //在 Java和kotlin中，只要方法定义在同一个类中，方法定义的位置实际上不影响程序的正常运行。

    private var count = 0

    private val mainTexts: Array<String> by lazy {
        resources.getStringArray(R.array.ciku_texts)
    }
    //kotlin的数组中的元素也是从0开始的
    private lateinit var kaiguan: MaterialSwitch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()//开启边缘到边缘Kotlin
        setContentView(R.layout.mainactivity)

        // 创建模态侧边栏
        val sideSheetDialog = SideSheetDialog(this)
        sideSheetDialog.setContentView(R.layout.side_sheet_content)
        // 设置侧边栏的边缘（可选）
        sideSheetDialog.setSheetEdge(Gravity.START) // 从左侧滑出
        //添加这行代码，防止点击外部区域关闭侧边栏
        //sideSheetDialog.setCanceledOnTouchOutside(false)

        // 获取侧边栏的根布局并设置点击事件
        sideSheetDialog.findViewById<View>(R.id.side_sheet_root)?.setOnClickListener {
            // 空实现，用于拦截点击事件
        }
        // 查找侧边栏中的视图并设置点击事件
        sideSheetDialog.findViewById<Button>(R.id.close_button)?.setOnClickListener {
            sideSheetDialog.dismiss()
//            val stitle = findViewById<TextView>(R.id.sidebar_title)
//            stitle.text = "首页"

        }
        //侧边栏按钮inf
        sideSheetDialog.findViewById<Button>(R.id.infButton)?.setOnClickListener {
            val intent = Intent(this, InfActivity::class.java)
            startActivity(intent)
        }


        //kotlin  使用 findViewById 查找按钮
        val myButton1: Button = findViewById(R.id.button1)
        //val myButton2: Button = findViewById(R.id.button2)
        val myButton3: Button = findViewById(R.id.button3)
        val myButton4: Button = findViewById(R.id.button4)
        //val topAppBar.subtitle = findViewById<TextView>(R.id.textView)


        //val kaiguan: SwitchMaterial = findViewById(R.id.ListenSwitch)
        kaiguan = findViewById(R.id.ListenSwitch)
        // 检查开关状态并启动服务
        if (kaiguan.isChecked && isNotificationServiceEnabled()) {
            val intent = Intent(this, NotificationMonitor::class.java)
            startService(intent)

        }



        updateSwitchState()
        val topAppBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        topAppBar.subtitle ="---------"
        // 检查开关状态并启动服务
        /**顶部导航栏,通过点击AppBar的导航图标来切换侧边栏*/
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            Toast.makeText(this, "Navigation touched", Toast.LENGTH_SHORT).show()

            sideSheetDialog.show()
        }


        myButton1.setOnClickListener {
            val intent = Intent(this,history::class.java)
            startActivity(intent)
        }
//        myButton2.setOnClickListener {
//            // 按钮被点击时执行的代码
//            val intent = Intent(this, InfActivity::class.java)
//            startActivity(intent)
//        }
        myButton3.setOnClickListener {
            if (count < 7) {
                topAppBar.subtitle = mainTexts[count]
                //count++
                if (count == 5 || count == 6){
                    //topAppBar.subtitle = 20F
                }else{
                    //topAppBar.subtitle.textSize = 40f
                }
                count++
            }else{
                topAppBar.subtitle = "初学阶段"
                //topAppBar.subtitle.textSize = 40f
                count = 0
            }
        }
        myButton4.setOnClickListener {
            Toast.makeText(this, "此功能还未开发", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,InfActivity::class.java)
            startActivity(intent)
        }
        val fuChen: Button = findViewById(R.id.fuchen)

        fuChen.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("删除数据库")
                .setMessage("在数据库异常时才建议这么做，确定删除数据库吗？")
                .setPositiveButton("确定") { _, _ ->
                    // 执行删除操作
                    //此用于获取files目录下的文件
                    // val path = "${filesDir.absolutePath}/notifications.db"
                    val path = getDatabasePath("notifications.db").absolutePath
                    val file = File(path)
                    if (file.exists()) {
                        if (file.delete()) {
                            Toast.makeText(this, "数据库已删除", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "删除数据库失败", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "数据库文件不存在${path}", Toast.LENGTH_LONG).show()
                    }
//                    val path = "${filesDir}/notifications.db"
//                    val file = File(path)
//                    file.delete()
                    //这是Java的语法 path = filesDir() + "/" + "notifications.db"
                    //Toast.makeText(this, "其实还没做出来", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("取消", null)
                .show()
        }




        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.topbar1 -> {
                    Toast.makeText(this, "此功能还未开发", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.topbar2 -> {
                    Toast.makeText(this, "此功能还未开发", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        /**通知记录服务开关*/
        kaiguan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!isNotificationServiceEnabled()) {
                    // 调用引导用户授权的方法
                    requestNotificationAccess()
                    updateSwitchState()
                }else {
                    // 启动服务
//                    val intent = Intent(this, NotificationMonitor::class.java)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        startForegroundService(intent)
//                    } else {
//                        startService(intent)
//                    }
                    Toast.makeText(this, "通知访问权限已开启", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 停止服务（需要自定义逻辑）
                //stopService(Intent(this, NotificationMonitor::class.java))
            }
        }

    }

    /**添加创建通知渠道的方法*/
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "notification_monitor_channel",
//                "通知监听服务",
//                NotificationManager.IMPORTANCE_LOW
//            ).apply {
//                description = "用于持续监听系统通知"
//            }
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }


    override fun onResume() {
        super.onResume()
        // 每次返回界面时更新开关状态
        updateSwitchState()

        // 添加服务启动检查
//        if (kaiguan.isChecked && isNotificationServiceEnabled()) {
//            val intent = Intent(this, NotificationMonitor::class.java)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startForegroundService(intent)
//            } else {
//                startService(intent)
//            }
//        }
    }



    /**更新开关状态*/
    private fun updateSwitchState() {
        kaiguan.isChecked = isNotificationServiceEnabled()
    }
    /**删除数据库*/


    /**停止服务的方法（需要服务支持）*/
//    override fun stopService(intent: Intent): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            //super.stopForegroundService(intent)
//            applicationContext.stopService(intent)
//        } else {
//            super.stopService(intent)
//        }
//    }

    /**引导用户授权通知访问权限*/
    private fun requestNotificationAccess() {
        // 检查权限是否已开启
        if (!isNotificationServiceEnabled()) {
            // 如果未开启，弹出对话框提示用户去设置页面开启
            MaterialAlertDialogBuilder(this)
                .setTitle("通知访问权限")
                .setMessage("为了使用通知记录功能，请开启通知访问权限。")
                .setPositiveButton("去开启") { _, _ ->
                    // 跳转到通知访问权限设置页面
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    startActivity(intent)
                }
                .setNegativeButton("取消", null)
                .show()
        }
//        else
//        {
//            Toast.makeText(this, "通知访问权限已开启", Toast.LENGTH_SHORT).show()
//        }
    }

    /**检查通知访问权限是否已开启*/
    private fun isNotificationServiceEnabled(): Boolean {
//        val packageName = packageName
//        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
//        return flat != null && flat.contains(packageName)
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(this)
        return enabledListeners.contains(packageName)
    }


}
