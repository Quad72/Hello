package com.example.ch11

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch11.databinding.ActivityMainBinding
import com.example.ch11.databinding.ItemMainBinding

class MainActivity : AppCompatActivity() {
    val fragmentManager:FragmentManager=supportFragmentManager
    val transaction:FragmentTransaction=fragmentManager.beginTransaction()
    val fragment=OneFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val abinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(abinding.root)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)---뒤로가기 버튼 설정
        setSupportActionBar(abinding.toolbar)
        val datas= mutableListOf<String>()
        for (i in 1..10){
            datas.add("Item $i")
        }
        class MyViewHolder(val binding: ItemMainBinding):RecyclerView.ViewHolder(binding.root)
        class MyAdapter(val binding: MutableList<String>):
                RecyclerView.Adapter<RecyclerView.ViewHolder>(){

            override fun getItemCount(): Int {
                return datas.size
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder =MyViewHolder(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                Log.d("kkang","onBindViewHolder: $position")
                val binding = (holder as MyViewHolder).binding
                binding.itemData.text=datas[position]
                binding.itemRoot.setOnClickListener{
                    Log.d("kkang","item root click:$position")
                    datas.add("newdata")
                    (abinding.recyclerView.adapter as MyAdapter).notifyDataSetChanged()
                }
            }
                }
        val layoutManager=GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        abinding.recyclerView.layoutManager=layoutManager
        abinding.recyclerView.adapter=MyAdapter(datas)
        abinding.recyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d("kkang","onSupportNavigateUp")
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_main,menu)
        val menuItem=menu?.findItem(R.id.menu_search)
        val searchView=menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("kkang","입력 중")
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("kkang","입력 완료")
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean=when(item.itemId){
        R.id.menu1->{
            Log.d("kkang","menu1 click")
            true
        }
        R.id.menu2->{
            Log.d("kkang","menu2 click")
            true
        }
        else->super.onOptionsItemSelected(item)
    }
}