package com.example.afinal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.databinding.ItemRecyclerviewBinding
import java.util.*


class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>?,val colors: MutableList<Int>?,val times: MutableList<String>?,val alarms: MutableList<Boolean>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    fun onClick(view: View, i:Int) {
        Log.d("kkang","$i 클릭")
        Toast.makeText(view.context, "${datas!![i]} 삭제!", Toast.LENGTH_LONG).show()
        val db = DBHelper(view.context).writableDatabase
        db.execSQL("delete from TODO_TB where todo='${datas!![i]}'")
        datas.removeAt(i)
        colors?.removeAt(i)
        times?.removeAt(i)
        alarms?.removeAt(i)
        notifyDataSetChanged()

    }
    fun toggleOn(view: View, i: Int){
        var intmin=0
        val token = times!![i].split(' ')
        val inthour=token[0].toInt()
        val middle=Character.getNumericValue(token[2][0])
        if (middle==0){
            intmin=Character.getNumericValue(token[2][1])
        } else {
            intmin=token[2].toInt()
        }
        Log.d("kkang","토글 $inthour,$intmin")
        val alarmManager=view.context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val calendar=Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,inthour)
        calendar.set(Calendar.MINUTE,intmin)
        if (calendar.timeInMillis<System.currentTimeMillis()){
            calendar.add(Calendar.DATE,1)
        }
        val intent = Intent(view.context, AlarmReceiver::class.java)
        val bundle= Bundle()
        bundle.putString("todo",datas!![i])
        intent.putExtra("todo",bundle)
        val pintent = PendingIntent.getBroadcast(view.context, i, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pintent)
        }
        val db = DBHelper(view.context).writableDatabase
        db.execSQL("update TODO_TB set alarm='true' where todo='${datas!![i]}'")
        alarms!![i]=true
    }
    fun toggleOff(view: View, i: Int){
        val alarmManager=view.context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(view.context, AlarmReceiver::class.java)
        val pintent = PendingIntent.getBroadcast(view.context, i, intent, 0)
        alarmManager.cancel(pintent)
        val db = DBHelper(view.context).writableDatabase
        db.execSQL("update TODO_TB set alarm='false' where todo='${datas!![i]}'")
        alarms!![i]=false
    }

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        val activity=MainActivity()
        binding.itemData.text= times!![position]
        binding.itemDesc.text= datas!![position]
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(colors!![position])
        shape.setCornerRadius(15f)
        holder.binding.itemRoot.setBackground(shape)
        holder.binding.alarmswitch.setOnCheckedChangeListener(null)
        binding.alarmswitch.isChecked=alarms!![position]
        holder.binding.itemDelete.setOnClickListener{
            onClick(it,position)
        }
        holder.binding.alarmswitch.setOnCheckedChangeListener{ SwitchView, isChecked ->
            if (isChecked==true){
                toggleOn(SwitchView,position)
                Log.d("kkang","$position 변경")
            } else {
                toggleOff(SwitchView,position)
                Log.d("kkang","$position 변경")
            }
        }
    }
}