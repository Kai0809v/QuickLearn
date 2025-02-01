package com.example.learn

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private var count = 0
    private val texts = arrayOf("你好", "世界", "你好世界")
    //kotlin的数组中的元素也是从0开始的


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)

        //kotlin  使用 findViewById 查找按钮
        val myButton1: Button = findViewById(R.id.button1)
        val myButton2: Button = findViewById(R.id.button2)
        val myButton3: Button = findViewById(R.id.button3)
        val wenben = findViewById<TextView>(R.id.textView)


        /******
        // 现在你可以对按钮进行操作，例如设置点击事件监听器
        myButton1.setOnClickListener {
            // 创建一个AlertDialog.Builder实例
            val builder = AlertDialog.Builder(this)

            // 设置对话框的标题和消息
            builder.setTitle("提示")
            builder.setMessage("还没有开发出来")

            // 设置对话框的按钮
            builder.setPositiveButton("确定") { dialog, which ->
                // 用户点击了确定按钮
                // 在这里处理用户的响应
                Toast.makeText(this,"马上了",Toast.LENGTH_SHORT).show()
            }

            // 创建并显示对话框
            val dialog: AlertDialog = builder.create()
            dialog.show()

            // 按钮被点击时执行的代码
            //val intent = Intent(this, 待定活动页::class.java)
            //startActivity(intent)
        }
        ******/
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
            if (count < 3) {
                wenben.text = texts[count]
                count++
            }else{
                wenben.text = "初学阶段"
                count = 0
            }
            println("按钮3被点击了")

        }
    }


}
