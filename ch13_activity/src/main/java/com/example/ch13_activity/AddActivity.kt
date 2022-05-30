package com.example.ch13_activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ch13_activity.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddBinding
    var color:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bluebutton.setOnClickListener{
            color="blue"
        }
        binding.redbutton.setOnClickListener{
            color="red"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean=when(item.itemId) {
        R.id.menu_add_save->{
            val intent=intent
            intent.putExtra("result",binding.addEditView.text.toString())
            if (color!=null){
                intent.putExtra("color",color)
                color=null
            } else{
                intent.putExtra("color","white")
            }
            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else->true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val intent=intent
        setResult(Activity.RESULT_OK,intent)
        finish()
        super.onBackPressed()
    }

}