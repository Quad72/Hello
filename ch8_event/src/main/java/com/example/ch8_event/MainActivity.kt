package com.example.ch8_event

import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.ch8_event.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var initTime=0L
    var pauseTime=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startbutton.setOnClickListener {
            binding.chronometer.base=SystemClock.elapsedRealtime()+pauseTime
            binding.chronometer.start()
            binding.stopbutton.isEnabled= true
            binding.resetbutton.isEnabled=true
            binding.startbutton.isEnabled=false
        }
        binding.stopbutton.setOnClickListener {
            pauseTime=binding.chronometer.base-SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.stopbutton.isEnabled=false
            binding.startbutton.isEnabled=true
            binding.resetbutton.isEnabled=true
        }
        binding.resetbutton.setOnClickListener {
            pauseTime=0L
            binding.chronometer.base=SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.stopbutton.isEnabled= false
            binding.resetbutton.isEnabled=false
            binding.startbutton.isEnabled=true
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-initTime>3000){
                android.widget.Toast.makeText(this,"종료하려면 한번 더 누르세요!!",android.widget.Toast.LENGTH_SHORT).show()
                initTime=System.currentTimeMillis()
                return true
            }
            }
        return super.onKeyDown(keyCode, event)
    }
}