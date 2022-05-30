package com.example.afinal

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.afinal.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var datas: MutableList<String>? = null
    var times: MutableList<String>? = null
    var colors: MutableList<Int>? = null
    var adcolors:MutableList<Int>?=null
    var alarms: MutableList<Boolean>? = null
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val now:Long = System.currentTimeMillis()
        val todate: Date = Date(now)
        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val getTime:String = dateFormat.format(todate);
        binding.abtitle.title = getTime
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.google.android.material.R.drawable.material_ic_calendar_black_24dp)
        val manager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelId="one-channel"
            val channelName="My Channel one"
            val channel= NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )

            channel.description="My Channel One Description"
            channel.setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes= AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor= Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern= longArrayOf(100,200,100,200)
            manager.createNotificationChannel(channel)
        }


        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            it.data!!.getStringExtra("result")?.let {
                datas?.add(it)
            }
            it.data!!.getStringExtra("color")?.let {
                colors?.add(it.toInt())
            }
            it.data!!.getStringExtra("time")?.let {
                times?.add(it)
            }
            it.data!!.getStringExtra("alarm")?.let {
                alarms?.add(it.toBoolean())
            }
            adapter.notifyDataSetChanged()
        }
        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            requestLauncher.launch(intent)
        }

        datas= mutableListOf<String>()
        times= mutableListOf<String>()
        colors= mutableListOf<Int>()
        alarms= mutableListOf<Boolean>()

        //add......................
        val db = DBHelper(this).readableDatabase
        val cursor = db.rawQuery("select * from TODO_TB",null)
        cursor.run {
            while (moveToNext()){
                datas?.add(cursor.getString(1))
                colors?.add(cursor.getString(2).toInt())
                times?.add(cursor.getString(3))
                alarms?.add(cursor.getString(4).toBoolean())
            }
        }
        db.close()

        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager=layoutManager
        adapter=MyAdapter(datas,colors,times,alarms)
        binding.mainRecyclerView.adapter=adapter
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_main_setting){
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        if (item.itemId == android.R.id.home){
            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    Log.d("kkang", "year : $p1, month : $p2, dayOfMonth : $p3")
                }
            }, 2022, 3,21).show()
        }
        return super.onOptionsItemSelected(item)
    }

}
