package com.example.ch17_database

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ch17_database.databinding.ItemRecyclerviewBinding


class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyAdapter(val datas: MutableList<String>?,val colors: MutableList<Int>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    fun onClick(view: View, i:Int) {
        Toast.makeText(view.context, "${datas!![i]} 완료!", Toast.LENGTH_LONG).show()
        Log.d("kkang","$i 클릭")
        datas.removeAt(i)
        colors?.removeAt(i)
    }

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        val activity=MainActivity()
        binding.itemData.text= datas!![position]
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(colors!![position])
        shape.setCornerRadius(15f)
        holder.binding.itemRoot.setBackground(shape)
        holder.binding.itemData.setOnClickListener(){
            onClick(binding.itemData,position)
        }
    }
}