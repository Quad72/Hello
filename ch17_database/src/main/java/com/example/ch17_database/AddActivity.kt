package com.example.ch17_database

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ch17_database.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    lateinit var colors:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        colors="white"
        binding.bluebutton.setOnClickListener{
            colors="blue"
        }
        binding.redbutton.setOnClickListener{
            colors="red"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.menu_add_save ->{
            val inputdata = binding.addEditView.text.toString()
            val inputcolor=colors
            val db = DBHelper(this).writableDatabase
            db.execSQL(
                "insert into TODO_TB (todo,colors) values (?,?)",
                arrayOf<String>(inputdata,inputcolor)
            )
            db.close()
            val intent = intent
            intent.putExtra("result",inputdata)
            intent.putExtra("color",inputcolor)
            setResult(Activity.RESULT_OK,intent)
            finish()
            true
        }
        else -> true
    }
}