package com.example.learn

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.Theme_QuickLearn)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.infpage)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}