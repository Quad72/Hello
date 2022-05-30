package com.example.ch14

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //브로드캐스트 리시버 동적 등록
        val reciever=object :BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                when(intent?.action){
                    Intent.ACTION_SCREEN_ON -> Log.d("kkang","screen on")
                    Intent.ACTION_SCREEN_OFF -> Log.d("kkang","screen off")
                    Intent.ACTION_BATTERY_OKAY -> Log.d("kkang","ACTION_BATTERY_OKAY")
                    Intent.ACTION_BATTERY_LOW -> Log.d("kkang","ACTION_BATTERY_LOW")
                    Intent.ACTION_BATTERY_CHANGED -> Log.d("kkang","ACTION_BATTERY_CHANGED")
                    Intent.ACTION_POWER_CONNECTED -> Log.d("kkang","ACTION_POWER_CONNECTED")
                    Intent.ACTION_POWER_DISCONNECTED -> Log.d("kkang","ACTION_POWER_DISCONNECTED")
                }
            }
        }
        val afilter=IntentFilter("ACTION_SCREEN_OFF")
        //apply를 이용해서 여러번 호출해야하는 변수를 한번에 처리함
        //apply를 이용하면 apply를 붙인거를 apply안에서 this로 계속 사용가능
        val filter=IntentFilter("ACTION_SCREEN_ON").apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(reciever,filter)
        //브로드캐스트 리시버 해제 함수
        //unregisterReceiver(reciever)
        //브로드캐스트 리시버 실햄 함수
        /*val intent=Intent(this,MyReceiver::class.java)
        sendBroadcast(intent)*/
        //시스템 인텐트 없이 배터리 상태 파악하기
        val intentFilter=IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus=registerReceiver(null,intentFilter)

        val status=batteryStatus!!.getIntExtra(BatteryManager.EXTRA_STATUS,-1)
        if (status==BatteryManager.BATTERY_STATUS_CHARGING){
            //전원이 공급되고 있다면
            val chargePlug=batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1)
            when(chargePlug){
                BatteryManager.BATTERY_PLUGGED_USB -> Log.d("kkang","usb charge")
                BatteryManager.BATTERY_PLUGGED_AC -> Log.d("kkang","ac charge")
            }
        }else{
            Log.d("kkang","not charging")
        }
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1)
        val batteryPct=level/scale.toFloat()*100
        Log.d("kkang","batteryPct : $batteryPct")
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}