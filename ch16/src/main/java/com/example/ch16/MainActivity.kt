package com.example.ch16

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent= Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662952,126.9779451"))
        startActivity(intent)
    }
}