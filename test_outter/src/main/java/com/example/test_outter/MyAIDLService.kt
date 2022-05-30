package com.example.test_outter

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast

class MyAIDLService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return object : MyAIDLInterface.Stub(){
            override fun funA(data: String?) {
                Toast.makeText(applicationContext,"$data",Toast.LENGTH_SHORT).show()
                Log.d("kkang","test")
            }
            override fun funB(): Int {
                return 10
            }
        }
    }
}