package com.example.ch13_coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ch13_coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        binding.button1.setOnClickListener {
            val channel= Channel<Long>()
            val backgroundScope= CoroutineScope(Dispatchers.Default+ Job())
            backgroundScope.launch {
                var sum  =0L
                var time= measureTimeMillis {
                    for(i in 1..2_000_000_000){
                        sum+=i
                    }
                }
                Log.d("kkang","time: $time")
                channel.send(sum)
            }
            val mainScope=GlobalScope.launch (Dispatchers.Main){
                channel.consumeEach {
                    binding.resultView.text="sum : $it"
                }
            }
        }

        setContentView(binding.root)
    }
}