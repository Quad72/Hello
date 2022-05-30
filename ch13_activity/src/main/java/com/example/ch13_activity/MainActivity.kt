package com.example.ch13_activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch13_activity.databinding.ActivityMainBinding
import com.example.ch13_activity.databinding.ItemRecyclerviewBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var datas:MutableList<String>? = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var colors:MutableList<Int>? = mutableListOf()
        val ambinding=ActivityMainBinding.inflate(layoutInflater)
        val drawable:GradientDrawable=getDrawable(R.drawable.dungle) as GradientDrawable
        setContentView(ambinding.root)
        //시간가져오기
        val ac:ActionBar?=supportActionBar
        val now:Long = System.currentTimeMillis()
        val todate:Date = Date(now)
        val dateFormat:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val getTime:String = dateFormat.format(todate);
        ac?.setTitle(getTime)
        class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
        class MyAdapter(val datas: MutableList<String>?):
            RecyclerView.Adapter<RecyclerView.ViewHolder>(){


            fun onClick(view: View, i:Int) {
                Toast.makeText(view.context, "${datas!![i]} 완료!", Toast.LENGTH_LONG).show()
                datas.removeAt(i)
                colors?.removeAt(i)
            }

            override fun getItemCount(): Int {
                return datas?.size?:0
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder =MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val binding = (holder as MyViewHolder).binding
                binding.itemData.text=datas!![position]
                //drawable.setColor(colors!![position])
                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.setColor(colors!![position])
                shape.setCornerRadius(15f)
                holder.binding.itemRoot.setBackground(shape)
                holder.binding.itemData.setOnClickListener(){
                    onClick(binding.itemData,position)
                    (ambinding.mainRecyclerView.adapter as MyAdapter).notifyDataSetChanged()
                }
            }
        }
        lateinit var adapter: MyAdapter
        val requestLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it.data!!.getStringExtra("result")?.let{
                datas?.add(it)
                adapter.notifyDataSetChanged()
            }
            it.data!!.getStringExtra("color")?.let{
                if (it=="blue"){
                    colors?.add(Color.BLUE)
                } else if (it=="red"){
                    colors?.add(Color.RED)
                } else if (it=="white"){
                    colors?.add(Color.BLACK)
                }
                adapter.notifyDataSetChanged()
            }
            if (it.resultCode== Activity.RESULT_OK){
                Toast.makeText(this,"sibla",Toast.LENGTH_SHORT).show()
            }
            /*val resultData=it.data?.getStringExtra("result").toString()
            datas?.add(resultData)
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"${datas}",Toast.LENGTH_SHORT).show()*/
        }
        ambinding.mainFab.setOnClickListener{
            val intent=Intent(this,AddActivity::class.java)
            requestLauncher.launch(intent)
        }
        datas=savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList()
        }?:let {
            mutableListOf<String>()
        }
        ambinding.mainRecyclerView.layoutManager= LinearLayoutManager(this)
        adapter=MyAdapter(datas)
        ambinding.mainRecyclerView.adapter=adapter
        ambinding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("datas", ArrayList(datas))
    }
}