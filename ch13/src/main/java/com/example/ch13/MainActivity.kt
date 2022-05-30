package com.example.ch13

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ch13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var integer=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //액티비티 화면 되돌리면서 requestLauncher로 사후처리하기
        /*val requestLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val resultData=it.data?.getStringExtra("result")
                    binding.mainResultView.text="result: $resultData"
        }*/
        val intent:Intent= Intent(this,DetailActivity::class.java)
        val intent2:Intent=Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.7749,127.4194"))
        //인텐트 찾아보고 없으면 없다고 토스트 메시지 출력
        try {
            startActivity(intent2)
        }catch (e:Exception){
            Toast.makeText(this,"no app...",Toast.LENGTH_SHORT).show()
        }
        //requestLauncher.launch(intent)
        val data2=savedInstanceState?.getInt("data2")
        if(data2==null){
        }else{
            val Iinteger=data2
            binding.leveltext.text="레벨: $Iinteger"
        }
        binding.levelbutton.setOnClickListener{
            integer=integer+1
            binding.leveltext.text="레벨: ${integer}"
        }
        //---소프트 키보드 제어하기
        //소프트 키보드 올리고 내리기
        val manager=getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.showInputButton.setOnClickListener{
            binding.editView?.requestFocus()
            manager.showSoftInput(binding.editView,InputMethodManager.SHOW_IMPLICIT)
        }
        binding.hideInputButton.setOnClickListener{
            manager.hideSoftInputFromWindow(currentFocus?.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
        }
        //소프트 키보드 보이면 안보이게, 안보이면 보이게
        binding.togglekeyboard.setOnClickListener{
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        }
        //전체화면 출력 코드--API30레벨 이후
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            window.setDecorFitsSystemWindows(false)
            val controller=window.insetsController
            if (controller!=null){
                controller.hide(WindowInsets.Type.statusBars() or
                WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior=
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        //---API 레벨 30 이전
        //전체화면 설정
        /*window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)*/
        //전체화면 해제
        /*window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)*/

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val data1=savedInstanceState.getString("data1")
        val data2=savedInstanceState.getInt("data2")
        integer=data2
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("data1","hello")
        outState.putInt("data2",integer)
    }
}