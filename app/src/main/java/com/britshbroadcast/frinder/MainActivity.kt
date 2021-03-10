package com.britshbroadcast.frinder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import com.britshbroadcast.frinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LocationListener {
    private val REQUEST_CODE = 222

    private lateinit var binding: ActivityMainBinding

    private lateinit var slideInAnimation: Animation
    private lateinit var slideOutAnimation: Animation

    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        slideInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        slideOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        binding.settingsButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                val uri = Uri.fromParts("package", packageName, null)
                //package://com.bri...frinder#permissions
                Log.d("TAG_J", uri.toString())
                it.data = uri
            })
        }
    }

    override fun onStart() {
        super.onStart()
        //1. Check if permission if granted
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            hideOverlay()
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
                    LocationManager.GPS_PROVIDER,
                    5000L,
                    0f,
                    this,

            )
    }

    override fun onLocationChanged(location: Location) {
        Log.d("TAG_J", location.toString())
            binding.locationTextView.text = getString(R.string.location_text, location.longitude, location.latitude)
    }
}