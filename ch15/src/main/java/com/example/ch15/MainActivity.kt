package com.example.ch15

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.example.ch15.databinding.ActivityMainBinding
import com.example.test_outter.MyAIDLInterface

class MainActivity : AppCompatActivity() {
    lateinit var messenger: Messenger
    lateinit var aidlService: MyAIDLInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //val intent= Intent(this,MyService::class.java)
        val exintent=Intent("ACTION_AIDL_SERVICE")
        exintent.setPackage("com.example.test_outter")
        bindService(exintent, connection, Context.BIND_AUTO_CREATE)
        //외부앱과 소통하기 위해 번들 만들기
        //val bundle = Bundle()
        //bundle.putString("data1","hello")
        //bundle.putInt("data2",10)
        binding.button.setOnClickListener{
            //val msg= Message()
            //msg.what =10
            //msg.obj="hello"
            //외부 앱이랑 메시지 주고받으려면 번들을 이용해야함
            //msg.obj=bundle
            //messenger.send(msg)
            aidlService.funA("hello")
            Toast.makeText(this,"${aidlService.funB()}",Toast.LENGTH_SHORT).show()
        }
        //잡인포랑 잡 스케쥴러로 잡 서비스 설정
        var jobScheduler: JobScheduler? = getSystemService<JobScheduler>()
        val extras=PersistableBundle()
        extras.putString("extra_data","hello kkang")
        val builder=JobInfo.Builder(1,ComponentName(this,MyJobService::class.java))
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        builder.setRequiresCharging(true)
        builder.setExtras(extras)
        val jobInfo=builder.build()
        jobScheduler!!.schedule(jobInfo)

    }
    val connection: ServiceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            aidlService= MyAIDLInterface.Stub.asInterface(service)
            Log.d("kkang","onServiceconneted...")
            //messenger = Messenger(service)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("kkang","onServiceDisconneted...")
        }
    }
}