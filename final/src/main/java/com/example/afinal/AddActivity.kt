package com.example.afinal

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.afinal.databinding.ActivityAddBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    var hour=0
    var minute=0
    var time:String="12 : 00"
    var icolors:Int = Color.rgb(187,134,252)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val now:Long = System.currentTimeMillis()
        val todate: Date = Date(now)
        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val getTime:String = dateFormat.format(todate)
        binding.tbtitle.text = getTime
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.colorPicker.setOnClickListener{
            val mDefaultColor:Int=R.color.purple_200
            ColorPickerDialog
                .Builder(this)        				// Pass Activity Instance
                .setTitle("Pick Theme")           	// Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   // Default ColorShape.CIRCLE
                .setDefaultColor(mDefaultColor)     // Pass Default Color
                .setColorListener { color, colorHex ->
                    // Handle Color Selection
                    icolors=color
                    binding.colorPicker.setBackgroundColor(color)
                }
                .show()
        }
        binding.time.setOnClickListener {
            TimePickerDialog(this,object: TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    Log.d("kkang","time : $p1 minute : $p2")
                    if (p2<10){
                        time="$p1 : 0$p2"
                    } else {
                        time="$p1 : $p2"
                    }
                    binding.time.text=time
                    hour=p1
                    minute=p2
                }
            },15,0,true).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.menu_add_save ->{
            val inputdata = binding.addEditView.text.toString()
            val inputcolor=icolors.toString()
            val inputtime=time
            val inputalarm=binding.alarmswitch.isChecked.toString()
            val alarmintent= Intent(this,AlarmReceiver::class.java)
            val bundle=Bundle()
            bundle.putString("todo",inputdata)
            alarmintent.putExtra("todo",bundle)
            val pintent=PendingIntent.getBroadcast(this,0,alarmintent,0)
            val calendar=Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY,hour)
            calendar.set(Calendar.MINUTE,minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val alarmManager=getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent)
            }
            val db = DBHelper(this).writableDatabase
            db.execSQL(
                "insert into TODO_TB (todo,colors,time,alarm) values (?,?,?,?)",
                arrayOf<String>(inputdata,inputcolor,inputtime,inputalarm)
            )
            db.close()
            val intent = intent
            intent.putExtra("result",inputdata)
            intent.putExtra("color",inputcolor)
            intent.putExtra("time",inputtime)
            intent.putExtra("alarm",inputalarm)
            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else -> true
    }
    override fun onBackPressed() {
        val intent=intent
        setResult(Activity.RESULT_OK,intent)
        finish()
        super.onBackPressed()
    }
}