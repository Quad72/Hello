package com.example.ch19

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {
    lateinit var providerClient: FusedLocationProviderClient
    lateinit var apiClient: GoogleApiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //플랫폼 API의 위치 매니저
        //위치 매니저 사용
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        //모든 위치 제공자 알아보기-어떤 위치제공자가 있는지 궁금할때
        var result = "All Provider :"
        val providers = manager.allProviders
        for (provider in providers) {
            result += "$provider, "
        }
        Log.d("kkang",result)
        //지금 사용할 수 있는 위치 제공자 알아보기
        result = "Enabled Providers : "
        val enabledProviders = manager.getProviders(true)
        for (provider in enabledProviders) {
            result += "$provider, "
        }
        Log.d("kkang",result)
        //위치 정보 얻기
        //위치 한 번만 가져오기
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ){
            val location:Location? =
                manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude
                val accuracy = location.accuracy
                val time = location.time

                Log.d("kkang","$latitude, $longitude, $accuracy, $time")
            }
        }
        //위치 계속 가져오기
        val listener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("kkang","${location.latitude},${location.longitude},${location.accuracy}")
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }

            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
            }
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10_000L,10f,listener)
        manager.removeUpdates(listener)
        //FusedLocationProvider 사용하기
        //FusedLocationProviderClient 초기화
        val providerClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        //GoogleApiClient 초기화
        val connectionCallback=object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(p0: Bundle?) {
                //위치 제공자를 사용할 수 있을때
                //위치 획득
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED){
                    providerClient.getLastLocation().addOnSuccessListener(
                        this@MainActivity,
                        object : OnSuccessListener<Location> {
                            override fun onSuccess(location: Location?) {
                                val latitude = location?.latitude
                                val longitude = location?.longitude
                            }
                        })
                }
            }
            override fun onConnectionSuspended(p0: Int) {
                //위지 제공자를 사용할 수 없을때
            }
        }
        val onConnectionFailedCallback = object : GoogleApiClient
                .OnConnectionFailedListener {
            override fun onConnectionFailed(p0: ConnectionResult) {
                //사용할 수 있는 위치 제공자가 없을때
            }
        }
        val apiClient:GoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(connectionCallback)
            .addOnConnectionFailedListener(onConnectionFailedCallback)
            .build()
        //위치 제공자 요청
        apiClient.connect()
    }
}