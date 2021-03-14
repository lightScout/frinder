package com.britshbroadcast.frinder.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.britshbroadcast.frinder.R
import com.britshbroadcast.frinder.databinding.ActivityMainBinding
import com.britshbroadcast.frinder.model.data.Result
import com.britshbroadcast.frinder.util.Type
import com.britshbroadcast.frinder.view.fragment.MarkerFragment
import com.britshbroadcast.frinder.viewmodel.FrinderViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private val REQUEST_CODE = 222

    private lateinit var binding: ActivityMainBinding

    private lateinit var slideInAnimation: Animation
    private lateinit var slideOutAnimation: Animation

    private lateinit var locationManager: LocationManager

//    private lateinit var dataBase: DataBase


    private val frinderViewModel by viewModels<FrinderViewModel>()
    private var SEARCH_TYPE = Type.bar.toString()
    private var RADIUS = "2000"
    private var LOCATION = "0,0"




    private lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        slideInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)
//        dataBase = Room.inMemoryDatabaseBuilder(this, DataBase::class.java).allowMainThreadQueries().build()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        frinderViewModel.searchResultLiveData.observe(this, Observer{
            Log.d("TAG_J", "Results retrieved -> ${it.size}")

            updateMap(it)
        })


        binding.settingsButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                val uri = Uri.fromParts("package", packageName, null)
                //package://com.bri...frinder#permissions
                Log.d("TAG_J", uri.toString())
                it.data = uri
            })
        }
    }

    private fun updateMap(it: List<Result>) {
        it.forEach { result ->
            val loc = LatLng(result.geometry.location.lat, result.geometry.location.lng)
           map.addMarker(MarkerOptions()
                    .position(loc)
                    .title(result.name))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }
    override fun onStart() {
        super.onStart()
        //1. Check if permission if granted
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            hideOverlay()
            registerLocationListener()
        }else{
            requestPermission()
        }


    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE && permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                registerLocationListener()

            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                    requestPermission()
                }
                else{
                    showOverlay()

                }

            }

        }
    }

    private fun hideOverlay() {
        binding.onverlayView.visibility = View.GONE
        binding.onverlayView.animation = slideOutAnimation
    }

    private fun showOverlay() {
        binding.onverlayView.visibility = View.VISIBLE
        binding.onverlayView.animation = slideInAnimation
    }

    @SuppressLint("MissingPermission")
    private fun registerLocationListener() {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000L,
                    100f,
                    this,
            )
    }

    override fun onLocationChanged(location: Location) {
        Log.d("TAG_J", "Location is ready...")

        if (this::map.isInitialized) {

            val location = LatLng(location.latitude, location.longitude)
            map.addMarker(
                    MarkerOptions()
                            .position(location).title("This is me!")
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            LOCATION = "${location.latitude},${location.longitude}"
            frinderViewModel.getCafeResult(LOCATION, RADIUS, SEARCH_TYPE)

        }




    }

    override fun onMapReady(gMap: GoogleMap) {
        this.map = gMap
        map.setOnMarkerClickListener(this)


    }

    @SuppressLint("RestrictedApi")
    fun onMenuClick(view: View){
        val animation = AnimationUtils.loadAnimation(this,R.anim.scale_up)
        view.startAnimation(animation)

        val popupMenu = PopupMenu(this,view)
        popupMenu.inflate(R.menu.main_menu)

        // Another solution restricted to Android Q and above
//        val mInflater = popupMenu.menuInflater
//        mInflater.inflate(R.menu.main_menu, menu.menu)
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            menu.setForceShowIcon(true)
//        }

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.health_item -> {
                    //Get drink location
                    map.clear()
                    SEARCH_TYPE = Type.doctor.toString()
                }
                R.id.self_item -> {
                    //Get Soccer location
                 map.clear()
                 SEARCH_TYPE = Type.park.toString()
                }
                else -> {
                    //Get food location
                    map.clear()
                    SEARCH_TYPE = Type.cafe.toString()
                }
            }
            frinderViewModel.getCafeResult(LOCATION, RADIUS,SEARCH_TYPE)
            true
        }
        val menuHelper = MenuPopupHelper(this, popupMenu.menu as MenuBuilder, view)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
        supportFragmentManager.popBackStack()
        val markerFragment: MarkerFragment = MarkerFragment()

        val bundle: Bundle = Bundle()
        bundle.putString("KEY", marker.title)
        markerFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).replace(R.id.main_frame, markerFragment)
            .addToBackStack(null)
            .commit()
        return true
    }
}