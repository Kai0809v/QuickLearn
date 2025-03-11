package com.dreamct.tingfeng

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.sidesheet.SideSheetDialog

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

        // 查找侧边栏中的视图并设置点击事件
        sideSheetDialog.findViewById<Button>(R.id.close_button)?.setOnClickListener {
            sideSheetDialog.dismiss()
            val stitle = findViewById<TextView>(R.id.sidebar_title)
        }




        //kotlin  使用 findViewById 查找按钮
        val myButton1: Button = findViewById(R.id.button1)
        val myButton2: Button = findViewById(R.id.button2)
        val myButton3: Button = findViewById(R.id.button3)
        val myButton4: Button = findViewById(R.id.button4)
        val wenben = findViewById<TextView>(R.id.textView)


        //val kaiguan: SwitchMaterial = findViewById(R.id.ListenSwitch)
        kaiguan = findViewById(R.id.ListenSwitch)
        updateSwitchState()
        val topAppBar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)

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
        myButton2.setOnClickListener {
            // 按钮被点击时执行的代码
            val intent = Intent(this, InfActivity::class.java)
            startActivity(intent)
        }
        myButton3.setOnClickListener {
            if (count < 7) {
                wenben.text = mainTexts[count]
                //count++
                if (count == 5 || count == 6){
                    wenben.textSize = 20F
                }else{
                    wenben.textSize = 40f
                }
                count++
            }else{
                wenben.text = "初学阶段"
                wenben.textSize = 40f
                count = 0
            }
        }
        myButton4.setOnClickListener {
            Toast.makeText(this, "此功能还未开发", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,InfActivity::class.java)
            startActivity(intent)
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
                    val intent = Intent(this, NotificationMonitor::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent)
                    } else {
                        startService(intent)
                    }
                    Toast.makeText(this, "通知访问权限已开启", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 停止服务（需要自定义逻辑）
                stopService(Intent(this, NotificationMonitor::class.java))
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // 每次返回界面时更新开关状态
        updateSwitchState()
    }



    // 更新开关状态
    private fun updateSwitchState() {
        kaiguan.isChecked = isNotificationServiceEnabled()
    }

    // 停止服务的方法（需要服务支持）
    override fun stopService(intent: Intent): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //super.stopForegroundService(intent)
            applicationContext.stopService(intent)
        } else {
            super.stopService(intent)
        }
    }

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
