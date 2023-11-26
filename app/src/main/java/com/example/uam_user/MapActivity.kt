package com.example.uam_user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class MapActivity : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_view, it).commit()
            }

        mapFragment.getMapAsync(this)

        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults))
        {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }



    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }



    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }



    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }



    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }



    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}