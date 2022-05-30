package com.example.ch13

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intent=intent
        intent.putExtra("data1","hello")
        intent.putExtra("data2",10)
        intent.putExtra("result","world")
        //finish()
    }


}