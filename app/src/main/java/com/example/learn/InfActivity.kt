package com.example.learn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.Theme_QuickLearn)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.infpage)
//使用了View类，还是要声明的
        val iButton1 :Button = findViewById(R.id.inf_b1)
        val iButton2 :Button = findViewById(R.id.inf_b2)


        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.inf_b1 -> {
                    // 处理按钮1的点击事件
                    val url = "https://github.com/Kai0809v/QuickLearn" // 替换为你要跳转的网址
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
                    startActivity(intent)
                    println("Button 1 clicked")
                }
                R.id.inf_b2 -> {
                    // 处理按钮2的点击事件
                    Toast.makeText(this,"等小星星够多的时候再创建",Toast.LENGTH_SHORT).show()
                    println("Button 2 clicked")
                }
            }
        }

        iButton1.setOnClickListener(clickListener)
        iButton2.setOnClickListener(clickListener)

    }
}