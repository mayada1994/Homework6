package com.example.mayada.weatherapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkWifiOnAndConnected()
    }

    private fun checkWifiOnAndConnected() {

        val wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.connectionInfo.networkId != -1) {
            Toast.makeText(this, "WiFi is available", Toast.LENGTH_LONG).show()
            Log.i("APP_TAG", "WiFi is available")
        } else {
            Toast.makeText(this, "WiFi is unavailable", Toast.LENGTH_LONG).show()
            Log.i("APP_TAG", "WiFi is unavailable")
        }
    }
}
