package com.example.ch14_reciever

import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ch14_reciever.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            when(getIntExtra(BatteryManager.EXTRA_STATUS,-1)){
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when (getIntExtra(BatteryManager.EXTRA_PLUGGED,-1)){
                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargeResultView.text="AC Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources,R.drawable.ac)
                            )
                        }
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            binding.chargeResultView.text="USB Plugged"
                            binding.chargingImageView.setImageBitmap(
                                BitmapFactory.decodeResource(resources,R.drawable.usb)
                            )
                        }
                    }
                }else -> {
                binding.chargeResultView.text="Not Plugged"
            }
            }
            //휴대폰의 배터리 레벨 가져오기
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE,-1)
            val batteryPct=level/scale.toFloat()*100
            binding.percentResultView.text="$batteryPct %"
        }
        binding.button.setOnClickListener{
            //리시버와 연결된 인텐트 생성해서 가짜로 브로드캐스트 함
            val intent=Intent(this,MyReceiver::class.java)
            sendBroadcast(intent)
        }

    }
}