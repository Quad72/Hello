package com.example.ch19

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ch19.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    //지도 뷰 객체 얻기
    lateinit var binding: ActivityMapBinding
    var googleMap:GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?)!!.getMapAsync(this)
        //지도의 중심 이동
        val latLng = LatLng(37.566610,126.978403)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(16f)
            .build()
        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(position))
        //마커 표시하기
        //마커 옵션
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background))
        markerOptions.position(latLng)
        markerOptions.title("서울시청")
        markerOptions.snippet("Tel:01-120")
        //마커 표시하기
        googleMap?.addMarker(markerOptions)
        //지도에서 사용자 이벤트 처리
        //지도 이벤트 핸들러
        //지도 클릭 이벤트
        googleMap?.setOnMapClickListener { latLng ->
            Log.d("kkang","click: ${latLng.latitude}, ${latLng.longitude}")
        }
        //지도 롱클릭 이벤트
        googleMap?.setOnMapLongClickListener { latLng ->
            Log.d("kkang","long click: ${latLng.latitude}, ${latLng.longitude}")
        }
        //지도화면 확대수준이나 지도 중심 변경될 때
        googleMap?.setOnCameraIdleListener {
            val position = googleMap!!.cameraPosition
            val zoom = position.zoom
            val latitude = position.target.latitude
            val longitude = position.target.longitude
            Log.d("kkang","user change : $zoom, $latitude, $longitude")
        }
        //마커 클릭시 이벤트
        googleMap?.setOnMarkerClickListener { marker ->
            true
        }
        //정보창 클릭 이벤트
        googleMap?.setOnInfoWindowClickListener { marker ->
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap=p0
    }
}