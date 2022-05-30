package com.example.ch12

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.ch12.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var toggle:ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ambinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(ambinding.root)
        setSupportActionBar(ambinding.toolbar)
        val tabLayout:TabLayout=ambinding.tabs
        val tab1:TabLayout.Tab=tabLayout.newTab()
        tab1.text="Tab1"
        tabLayout.addTab(tab1)
        val tab2:TabLayout.Tab=tabLayout.newTab()
        tab2.text="Tab2"
        tabLayout.addTab(tab2)
        val tab3:TabLayout.Tab=tabLayout.newTab()
        tab3.text="Tab3"
        tabLayout.addTab(tab3)
        val firstpage=supportFragmentManager.beginTransaction()
        firstpage.replace(R.id.tabContent,OneFragment())
        firstpage.commit()
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction=supportFragmentManager.beginTransaction()
                when(tab?.text){
                    "Tab1"->transaction.replace(R.id.tabContent,OneFragment())
                    "Tab2"->transaction.replace(R.id.tabContent,TwoFragment())
                    "Tab3"->transaction.replace(R.id.tabContent,ThreeFragment())
                }
                transaction.commit()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        toggle = ActionBarDrawerToggle(this,ambinding.drawer,R.string.drawer_opened,R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
        ambinding.mainDrawerView.setNavigationItemSelectedListener {
            Toast.makeText(this,"you selected ${it.title}",Toast.LENGTH_SHORT).show()
            true
        }
        ambinding.extendedFAB.setOnClickListener{
            when(ambinding.extendedFAB.isExtended){
                true->ambinding.extendedFAB.shrink()
                false->ambinding.extendedFAB.extend()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}