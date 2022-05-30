package com.example.ch10

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ch10.databinding.ActivityMainBinding
import com.example.ch10.databinding.DialogInputBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val status = ContextCompat.checkSelfPermission(this,"android.permission.ACCESS_FINE_LOCATION")
        if(status==PackageManager.PERMISSION_GRANTED){
            Log.d("kkang", "permission granted")
        } else{
            Log.d("kkang", "permission denied")
        }
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted->
            if(isGranted){
                Log.d("kkang", "callback, granted")
            } else{
                Log.d("kkang", "callback, denied")
            }
        }
        requestPermissionLauncher.launch("android.permission.ACCESS_FINE_LOCATION")
        val toast = Toast.makeText(this, "종료하려면 한 번 더 누르세요", Toast.LENGTH_SHORT)
        toast.show()
        binding.button2.setOnClickListener {
            TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    Log.d("kkang", "time : $p1, minute : $p2")
                    binding.timetext.text="$p1 시 $p2 분"
                }
            }, 15,0,true).show()

        }
        binding.button1.setOnClickListener {
            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    Log.d("kkang", "year : $p1, month : $p2, dayOfMonth : $p3")
                    binding.textView1.text="year : $p1, month : $p2, dayOfMonth : $p3"
                }
            }, 2020, 8,21).show()

        }
        val items= arrayOf<String>("사과", "복숭아", "수박", "딸기")
        binding.button3.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("items test")
                setIcon(android.R.drawable.ic_dialog_info)
                setItems(items, object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Log.d("kkang", "선택한 과일:${items[p1]}")
                        binding.fruittext.text="선택한 과일은 : ${items[p1]} 입니다"
                    }
                })
                setPositiveButton("닫기", null)
                show()
            }
        }
        var selecteditems = Array<String>(5){i->""}
        var siba:String=""
        binding.button4.setOnClickListener {
            AlertDialog.Builder(this).run {
                setTitle("items test")
                setIcon(android.R.drawable.ic_dialog_info)
                setMultiChoiceItems(
                    items,
                    booleanArrayOf(false, false, false, false),
                    object : DialogInterface.OnMultiChoiceClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean) {
                            var mstring:String=siba
                            if (p2){
                                mstring=mstring.plus(" ${items[p1]}")
                                siba=mstring
                            } else{}
                        }
                    })
                val eventHandler= object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        if (p1==DialogInterface.BUTTON_POSITIVE){
                            binding.multifruittext.text="선택한 과일은 ${siba} 입니다."
                            siba=""
                        } else{
                        }
                    }
                }
                setPositiveButton("닫기", eventHandler)
                show()
            }
        }
        val dialogBinding=DialogInputBinding.inflate(layoutInflater)
        AlertDialog.Builder(this).run{
            setTitle("Input")
            setView(dialogBinding.root)
            setPositiveButton("닫기",null)
            show()
        }
        val vibrator = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator;
        }else{
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        binding.vibebutton.setOnClickListener {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                vibrator.vibrate(
                    VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE)
            )
            }else{
                vibrator.vibrate(500)
            }
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(500,1000,500,2000),
                    intArrayOf(0,50,0,200),-1))
            } else{
                vibrator.vibrate(longArrayOf(500,1000,500,2000),-1)
            }
        }
    }



}
