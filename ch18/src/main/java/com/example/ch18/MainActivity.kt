package com.example.ch18

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.Call
import android.telephony.PhoneStateListener
import android.telephony.ServiceState
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.ch18.MainActivity.retrofitobj.networkService
import com.google.gson.annotations.SerializedName
import okhttp3.Callback
import okhttp3.ResponseBody
import okhttp3.internal.platform.Platform.get
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.io.StringReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //전화 상태 변화 감지-PhoneStateListener 사용
        val phoneStateListener = object : PhoneStateListener(){
            //비행모드같이 서비스 상태가 바뀌는 순간에 호출
            override fun onServiceStateChanged(serviceState: ServiceState?) {
                super.onServiceStateChanged(serviceState)
                when(serviceState?.state) {
                    ServiceState.STATE_EMERGENCY_ONLY -> Log.d("kkang","EMERGENCY_ONLY....")
                    ServiceState.STATE_OUT_OF_SERVICE -> Log.d("kkang","OUT_OF_SERVICE...")
                    ServiceState.STATE_POWER_OFF -> Log.d("kkang","POWER_OFF...")
                    ServiceState.STATE_IN_SERVICE -> Log.d("kkang","IN_SERVICE...")
                }
            }
            //전화가 걸려오는 상태 감지
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)
                when(state){
                    //통화 대기 상태
                    TelephonyManager.CALL_STATE_IDLE -> Log.d("kkang","IDLE...")
                    //벨이 울리는 상태
                    TelephonyManager.CALL_STATE_RINGING -> Log.d("kkang","RINGING...")
                    //통화 중인 상태
                    TelephonyManager.CALL_STATE_OFFHOOK -> Log.d("kkang","OFFHOOK...")
                }
            }
        }
        //전화 매니저 얻기
        val manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager.listen(phoneStateListener,PhoneStateListener.LISTEN_SERVICE_STATE)
        //여러 상태 감지
        manager.listen(phoneStateListener,PhoneStateListener.LISTEN_SERVICE_STATE or PhoneStateListener.LISTEN_CALL_STATE)
        //상태 변화 감지 해제
        manager.listen(phoneStateListener,PhoneStateListener.LISTEN_NONE)

        //전화 상태 변호 감지하기-TelephonyCallback 사용
        //TelephonyCallback
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            telephonyManager.registerTelephonyCallback(
                mainExecutor,
                object  : TelephonyCallback(),TelephonyCallback.CallStateListener{
                    override fun onCallStateChanged(state: Int) {
                        when (state) {
                            TelephonyManager.CALL_STATE_IDLE -> {
                                Log.d("kkang","IDLE")
                            }
                            TelephonyManager.CALL_STATE_OFFHOOK -> {
                                Log.d("kkang","IDLE")
                            }
                            TelephonyManager.CALL_STATE_RINGING -> {
                                Log.d("kkang","IDLE")
                            }
                        }
                    }
                }
            )
        }
        //네트워크 국가, 사업자, 전화번호 얻기
        val countryIso = telephonyManager.networkCountryIso
        val operatorName = telephonyManager.networkOperatorName
        //val phoneNumber = telephonyManager.line1Number
        //Volley 라이브러리- 문자열 데이터 요청하기-StringRequest
        //문자열 요청 정의
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String>{
                Log.d("kkang","server data : $it")
            },
            Response.ErrorListener { error ->
                Log.d("kkang","error.......$error")
            })
        //서버에 요청하기
        val queue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
        //POST방식으로 데이터 전송
        val stringRequest2 = object : StringRequest (
            Request.Method.POST,
            url,
            Response.Listener<String>{
                Log.d("kkang","server data : $it")
            },
            Response.ErrorListener { error ->
                Log.d("kkang","error.......$error")
            }){
            override fun getParams(): MutableMap<String, String>? {
                return mutableMapOf<String,String>("one" to "hello","two" to "world")
            }
        }
        //이미지 데이터 요청하기
        //이미지 요청 정의
        val imageRequest = ImageRequest(
            url,
            Response.Listener {response -> binding.imageview.setImageBitmap(response)},
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            null,
            Response.ErrorListener { error ->
                Log.d("kkang","error......$error")
            })
        val queue2=Volley.newRequestQueue(this)
        queue2.add(imageRequest)
        //setImageUrl() 함수로 요청하기
        val queue3=Volley.newRequestQueue(this)
        val imgMap = HashMap<String,Bitmap>()
        val imageLoader = ImageLoader(queue3,object : ImageLoader.ImageCache{
            override fun getBitmap(url: String?): Bitmap? {
                return imgMap[url]
            }

            override fun putBitmap(url: String?, bitmap: Bitmap?) {
                imgMap[url] = bitmap
            }
        })
        binding.networkImagView.setImageUrl(url, imageLoader)
        //JSON 데이터 요청하기-JsonObjectRequest
        //JSON 데이터 요청하기
        val jsonRequest =
            JsonObjectRequest(
                Request.Method.GET,
                null,
                null,
                Response.Listener<JSONObject>{ response ->
                    val title = response.getString("title")
                    val date = response.getString("date")
                    Log.d("kkang","$title,$date")
                },
                Response.ErrorListener { error -> Log.d("kkang","error......$error") }
            )
        val queue4 = Volley.newRequestQueue(this)
        queue4.add(jsonRequest)
        //JSON 배열 요청하기
        val jsonArraayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                for (i in 0 until response.length()){
                    val jsonObject = response[i] as JSONObject
                    val title = jsonObject.getString("title")
                    val date = jsonObject.getString("date")
                    Log.d("kkang","$title,$date")
                }
            },
            Response.ErrorListener { error -> Log.d("kkang","error......$error") }
        )
        //Retrofit라이브러리 사용하기
        //모델 클래스
        data class UserModel(
            var id:String,
            @SerializedName("first_name")
            var firstName:String,
            //@SerializedNAme("last_name")
            var lastName: String,
            var avatar: String,
            var avatarBitmap: Bitmap
        )
        //데이터 안에 또 데이터가 있는경우
        //모델 클래스 분리 이용
        data class UserListModel(
            var page:String,
            @SerializedName("per_page")
            var perPage: String,
            var total:String,
            @SerializedName("total_pages")
            var totalPages:String,
            var data: List<UserModel>?
        )
        //서비스 인터페이스 정의
        interface INetworkService{
            @GET("api/users")
            fun doGetUserList(@Query("page") page:String): retrofit2.Call<UserListModel>
            @GET
            fun getAvatarImage(@Url url:String): retrofit2.Call<ResponseBody>
        }
        //Retrofit 객체생성
        object retrofitobj{
            val retrofit:Retrofit
                get()= Retrofit.Builder()
                    .baseUrl("https://reqres.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            //서비스 객체 얻기
            var networkService:INetworkService = retrofit.create(INetworkService::class.java)
        }
        //call 객체 얻기
        val userListCall=networkService.doGetUserList("1")
        //네트워크 통신 수행
        userListCall.enqueue(object : retrofit2.Callback<UserListModel>{
            //통신 성공시 호출
            override fun onResponse(
                call: retrofit2.Call<UserListModel>,
                response: retrofit2.Response<UserListModel>
            ) {
                //서버에서 온 데이터를 response.body로 받아올 수 있음
                val userList=response.body()
            }
            //통신 실패시 호출
            override fun onFailure(call: retrofit2.Call<UserListModel>, t: Throwable) {
                call.cancel()
            }
        })
        //HTTP 메서드 애너테이션
        //인터페이스에 선언한 함수
        @GET("users/list?sort=desc")
        fun test1(): Call<UserModel>
        //Call 객체를 얻는 구문
        val call:retrofit2.Call<UserModel> = networkService.test1()
        //최종 서버 요청 URL
        //https://어쩌구저쩌구
        //동적인 경로 애너테이션
        //인터페이스에 선언한 함수
        @GET("group/{id}/users{name}")
        fun test2(
            @Path("id")userId:String,
            @Path("name")qrg2:String
        ):Call<UserModel>
        //Call 객체를 얻는 구문
        val call2:retrofit2.Call<UserModel> = networkService.test2("10","kkang")
        //최종 서버 요청 URL
        //https://reqres.in/group/10/users/kkang
        //질의 애너테이션 예
        //인터페이스에 선언한 함수
        @GET("group/users")
        fun test3(
            @Query("sort") arg1:String,
            @Query("name") arg2:String
        ):retrofit2.Call<UserModel>
        //Call객체를 얻는 구문
        val call3:retrofit2.Call<UserModel> = networkService.test3("age","kkang")
        //최종 서버 요청 URL
        //https://reqres.in/group/users?sort=age$name=kkang
        //질의 맵 애너테이션 예
        //인터페이스에 선언한 함수
        @GET("group/users")
        fun test4(
            @QueryMap options:Map<String,String>,
            @Query("name") name:String
        ):retrofit2.Call<UserModel>
        //Call객체를 얻는 구문
        val call4:retrofit2.Call<UserModel> = networkService.test4(
            mapOf<String,String>("one" to "hello","two" to "world"),
            "kkang"
        )
        //최종 서버 요청 URL
        //서버에 전송할 데이터를 모델로 하고싶을때
        //모델 객체 애너테이션 예
        //인터페이스에 선언한 함수
        @POST("group/users")
        fun test5(
            @Body user: UserModel,
            @Query("name") name: String
        ): retrofit2.Call<UserModel>
        //Call 객체를 얻는 구문
        val call5:retrofit2.Call<UserModel> = networkService.test5(
            UserModel(id = "1",firstName = "gildong",lastName = "hong",avatar = "someurl"),
            "kkang"
        )
        //최종 서버 요청 URL
        //https://reares.in/group/users?name=kkang
        //FieldUrlEncoded는 데이터를 URL형태로 전송 *POST일떄만 사용가능
        //인터페이스에 선언한 함수
        @FormUrlEncoded
        @POST("user/edit")
        fun test6(
            @Field("first_name") first:String?,
            @Field("last_name") last:String?,
            @Query("name") name:String?
        ):retrofit2.Call<UserModel>
        //Call 객체를 얻는 구문
        val call6:retrofit2.Call<UserModel> = networkService.test6(
            "gildong 길동",
            "hong 홍",
            "kkang"
        )
        //최종 서버 요청 URL
        //https://reares.in/user?name=kkang
        //리스트에 필드 애너테이션 사용 예
        @FormUrlEncoded
        @POST("tasks")
        fun test7(@Field("title")titles:List<String>):retrofit2.Call<UserModel>
        //Call 객체를 얻는 구문
        val list: MutableList<String> = ArrayList()
        list.add("홍길동")
        list.add("류현진")
        val call7:retrofit2.Call<UserModel> = networkService.test7(list)
        //최종 서버 요청 URL
        //https://reares.in/tasks
        //서버요청에서 헤더값 조정하고싶을때
        //인터페이스에 선언한 함수
        @Headers("Cache-Control:max-age=640000")
        @GET("widget/list")
        fun test8(): retrofit2.Call<UserModel>
        //base URL 무시하고 완전 새로운 URL 사용시
        //인터페이스에 선언한 함수
        @GET
        fun test9(@Url url:String,@Query("name") name:String):retrofit2.Call<UserModel>
        //Call 객체를 얻는 구문
        val call9:retrofit2.Call<UserModel> = networkService.test9("http://www.google.com","kkang")
        //Glide 라이브러리
        //이미지 가져와 출력하기
        //리소스 이미지 출력
        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .into(binding.resultView)
        //파일 이미지 출력
        //갤러리 앱의 목록 화면에서 선택한 이미지를 가져옴
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            Glide.with(this)
                .load(it.data!!.data)
                .into(binding.resultView)
        }
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        requestLauncher.launch(intent)
        //서버 이미지 출력
        Glide.with(this)
            .load("https://")
            .into(binding.reSultview)
        //이미지 파일 크기 조절
        Glide.with(this)
            .load(R.drawable.ic_launcher_background)
            .override(200,200)
            .into(binding.resultView)
        //로딩, 오류 이미지 출력
        Glide.with(this)
            .load("https://")
            .override(200,200)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(binding.resultView)
        //이미지 데이터 사용하기
        Glide.with(this)
            .load("https://")
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    //이미지를 불러왔을때 호출되는 부분
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        //이미지를 Bitmap형식으로 받아오기
        Glide.with(this)
            .asBitmap()
            .load("https://")
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    //이미지를 불러왔을때 호출되는 부분
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
    //네트워크 접속 가능 여부
    private fun isNetworkAvailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when{
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.d("kkang","wifi available")
                    true
                }
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.d("kkang","celluar available")
                    true
                }
                else -> false
            }
        }else{
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
        //네트워크 타입 지정-NetworkRequest 이용하기
        val networkReq:NetworkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        //네트워크 접속 가능 여부
        connectivityManager.requestNetwork(networkReq,object :ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("kkang","NetworkCallback...onAvailable...")
            }
            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("kkang","NetworkCallback...onUmAvailable...")
            }
        })
    }
    data class UserModel(
        var id:String,
        @SerializedName("first_name")
        var firstName:String,
        //@SerializedNAme("last_name")
        var lastName: String,
        var avatar: String,
        var avatarBitmap: Bitmap
    )
    //데이터 안에 또 데이터가 있는경우
    //모델 클래스 분리 이용
    data class UserListModel(
        var page:String,
        @SerializedName("per_page")
        var perPage: String,
        var total:String,
        @SerializedName("total_pages")
        var totalPages:String,
        var data: List<UserModel>?
    )
    interface INetworkService{
        @GET("api/users")
        fun doGetUserList(@Query("page") page:String): retrofit2.Call<UserListModel>
        @GET
        fun getAvatarImage(@Url url:String): retrofit2.Call<ResponseBody>

    }
    object retrofitobj{
        val retrofit:Retrofit
            get()= Retrofit.Builder()
                .baseUrl("https://reqres.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //서비스 객체 얻기
        var networkService:INetworkService = retrofit.create(INetworkService::class.java)
    }

}