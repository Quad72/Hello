package com.example.ch11_viewpager

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ch11_viewpager.databinding.ActivityMainBinding
import com.example.ch11_viewpager.databinding.ItemPagerBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val abinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(abinding.root)

        class MyPagerViewHolder(val binding: ItemPagerBinding):
            RecyclerView.ViewHolder(binding.root)
        class MyPagerAdapter(val datas: MutableList<String>):
            RecyclerView.Adapter<RecyclerView.ViewHolder>(){

            override fun getItemCount(): Int {
                return datas.size
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder =MyPagerViewHolder(ItemPagerBinding.inflate(LayoutInflater.from(parent.context),parent,false))

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val binding = (holder as MyPagerViewHolder).binding
                binding.itemPagerTextView.text=datas[position]
                when(position % 3){
                    0->binding.itemPagerTextView.setTextColor(Color.RED)
                    1->binding.itemPagerTextView.setTextColor(Color.GREEN)
                    2->binding.itemPagerTextView.setTextColor(Color.BLUE)
                }
            }
        }
        val datas= mutableListOf<String>()
        for (i in 1..3){
            datas.add("Item $i")
        }
        abinding.viewpager.adapter=MyPagerAdapter(datas)
        abinding.viewpager.orientation= ViewPager2.ORIENTATION_HORIZONTAL
    }
}