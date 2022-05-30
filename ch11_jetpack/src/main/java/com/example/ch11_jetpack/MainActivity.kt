package com.example.ch11_jetpack

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ch11_jetpack.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ambinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(ambinding.root)
        setSupportActionBar(ambinding.toolbar)
        toggle= ActionBarDrawerToggle(this,ambinding.drawer,R.string.drawer_opened,R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        val adapter=MyFragmentPagerAdapter(this)
        ambinding.viewpager.adapter=adapter
    }
    class MyFragmentPagerAdapter(activity: FragmentActivity):FragmentStateAdapter(activity){
        val fragments:List<Fragment>
        init {
            fragments= listOf(OneFragment(),TwoFragment(),ThreeFragment())
        }

        override fun getItemCount(): Int =fragments.size
        override fun createFragment(position: Int): Fragment=fragments[position]
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_main,menu)
        val menuItem=menu?.findItem(R.id.menu_search)
        val searchView=menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
