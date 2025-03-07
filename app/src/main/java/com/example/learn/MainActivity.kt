package com.example.learn

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : ComponentActivity() {
    //在 Java和kotlin中，只要方法定义在同一个类中，方法定义的位置实际上不影响程序的正常运行。

    private var count = 0
    //private val texts = arrayOf("你好世界","1","2")
    private val mainTexts: Array<String> by lazy {
        resources.getStringArray(R.array.ciku_texts)
    }
    //kotlin的数组中的元素也是从0开始的


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()//开启边缘到边缘Kotlin
        setContentView(R.layout.mainactivity)
        // 调用引导用户授权的方法
        requestNotificationAccess()


        //kotlin  使用 findViewById 查找按钮
        val myButton1: Button = findViewById(R.id.button1)
        val myButton2: Button = findViewById(R.id.button2)
        val myButton3: Button = findViewById(R.id.button3)
        val myButton4: Button = findViewById(R.id.button4)
        val wenben = findViewById<TextView>(R.id.textView)


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
            println("按钮3被点击了")

        }
        myButton4.setOnClickListener {
            Toast.makeText(this, "此功能还未开发", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,InfActivity::class.java)
            startActivity(intent)
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
        }else{
            Toast.makeText(this, "通知访问权限已开启", Toast.LENGTH_SHORT).show()
        }
    }

    /**检查通知访问权限是否已开启*/
    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(packageName)
    }


}
