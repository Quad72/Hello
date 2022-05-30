package com.example.ch9

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.ch9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            val windowMetrics:WindowMetrics=windowManager.currentWindowMetrics
            binding.textView.text="width : ${windowMetrics.bounds.width()}, height : ${windowMetrics.bounds.height()}"
        }else{
            val display = windowManager.defaultDisplay
            val displayMetrics =DisplayMetrics()
            display?.getRealMetrics(displayMetrics)
            binding.textView.text="width : ${displayMetrics.widthPixels}, height : ${displayMetrics.heightPixels}"
        }
    }

}