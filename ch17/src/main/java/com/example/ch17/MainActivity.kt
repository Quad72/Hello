package com.example.ch17

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //테이블 생성
        val db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE,null)
        /*db.execSQL("create table USER_TB ("+
                "_id integer primary key autoincrement,"+
                "name not null, "+
                "phone)")*/
        //데이터 삽입
        db.execSQL("insert into USER_TB (name, phone) values (?,?)",
        arrayOf<String>("kkang","0101111"))
        //데이터 조회
        val cursor=db.rawQuery("select * from USER_TB",null)
        //선택한 행의 값 가져오기
        while (cursor.moveToNext()){
            val name = cursor.getString(0)
            val phone = cursor.getString(1)
        }
        //insert()함수 사용
        val values = ContentValues()
        values.put("name","kkang")
        values.put("phone","010112")
        db.insert("USER_TB",null,values)
        //query()함수 사용
        val cursor2=db.query("USER_TB", arrayOf<String>("name","phone"),"phone=?", arrayOf<String>("010112"),null,null,null)
        //SQLiteOpenHelper 이용하기
        class DBHelper(context: Context):SQLiteOpenHelper(context,"testdb2",null,1){
            //onCreate는 앱이 설치된 직후 딱 한번 호출됨
            override fun onCreate(p0: SQLiteDatabase?) {
            }
            //onUpgrade는 DB변경될때마다 호출됨
            override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            }
        }
        //SQLiteOpenHelper 이용해서 데이터베이스 객체 생성하기
        val db2:SQLiteDatabase=DBHelper(this).writableDatabase
        //여기서부터 코드로 파일 만드는 방법
        //파일 객체 생성 후 데이터 쓰기
        val file = File(filesDir,"test.txt")
        val writeStream:OutputStreamWriter = file.writer()
        writeStream.write("hello world")
        writeStream.flush()
        //파일의 데이터 읽기
        val readStream:BufferedReader = file.reader().buffered()
        readStream.forEachLine {
            Log.d("kkang","$it")
        }
        //Context객체의 함수 사용
        openFileOutput("test2.txt",Context.MODE_PRIVATE).use {
            it.write("hello world!!".toByteArray())
        }
        openFileInput("test2.txt").bufferedReader().forEachLine {
            Log.d("kkang","$it")
        }
        //외장메모리 사용 가능 여부 판단
        if (Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED){
            Log.d("kkang","ExternalStorageState Mounted")
        } else {
            Log.d("kkang","ExternalStorageState UnMounted")
        }
        //앱별 저장소에 접근
        /*val file2:File? = getExternalFilesDir(null)
        Log.d("kkang","${file?.absolutePath}")
        //앱별 저장소에 파일 쓰기
        val file3:File = File(getExternalFilesDir(null),"test3.txt")
        val writeStream2:OutputStreamWriter = file3.writer()
        writeStream2.write("hello world")
        writeStream2.flush()
        //앱별 저장소에 파일 읽기
        val readStream2:BufferedReader = file3.reader().buffered()
        readStream2.forEachLine {
            Log.d("kkang","$it")
        }*/
        //파일 프로바이더로 외부 공유(카메라 앱 이용)
        /*val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            //이미지 로딩
            var bitmap = BitmapFactory.decodeFile(filePath)
        }
        val timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file4 = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
        filePath = file.absolutePath
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.ch16_provider.fileprovider", file
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        requestCameraFileLauncher.launch(intent)*/
        //공용 저장소에 접근
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val cursor3 = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor3?.let {
            while (cursor3.moveToNext()){
                Log.d("kkang","_id:${cursor3.getLong(0)}, name:${cursor3.getString(1)}")
            }
        }
        //액티비티의 데이터 저장
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        //앱 전체의 데이터 저장
        val sharedPref2 = getSharedPreferences("my_prefs",Context.MODE_PRIVATE)
        //프리퍼런스에 데이터 저장
        sharedPref.edit().run {
            putString("data1","hello")
            putInt("data2",10)
            commit()
        }
        //프리퍼런스의 데이터 가져오기
        val data1=sharedPref.getString("data1","world")
        val data2=sharedPref.getInt("data2",10)
        //설정값 가져오기
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val id = sharedPreferences.getString("id","")
    }
}
class MySettingFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting,rootKey)
        //설정값을 코드에서 바꾸기
        val idPreference: EditTextPreference? = findPreference("id")
        val colorPreference: ListPreference? = findPreference("color")
        idPreference?.isVisible=true
        idPreference?.summary="code summary"
        idPreference?.title="code title"
        //설정값 자동적용
        idPreference?.summaryProvider=EditTextPreference.SimpleSummaryProvider.getInstance()
        colorPreference?.summaryProvider=ListPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider=Preference.SummaryProvider<EditTextPreference>{ preference ->
            val text = preference.text
            if (TextUtils.isEmpty(text)){
                "설정이 되지 않았습니다."
            } else {
                "설정된 ID 값은 : $text 입니다"
            }
        }
        //프리퍼런스에 이벤트 핸들러 지정
        idPreference?.setOnPreferenceClickListener{ preference ->
            Log.d("kkang","preference key : ${preference.key}")
            true
        }
        //프리퍼런스를 이용한 이벤트 처리
        idPreference?.setOnPreferenceChangeListener{ preference, newValue ->
            Log.d("kkang","preference key : ${preference.key}, newValue : $newValue")
            true
        }
    }
}
class CSettingFragment : PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting,rootKey)
    }
}