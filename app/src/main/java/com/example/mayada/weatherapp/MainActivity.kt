package com.example.mayada.weatherapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {

    lateinit var dailyWeatherAdapter: CustomAdapter
    private var temperatureUnits: Boolean = false
    val URL: String =
        "http://api.openweathermap.org/data/2.5/forecast?q=Kiev&mode=json&APPID=0b27c6340cd3d004436fafc9863ac1ff"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connect_button.setOnClickListener {
            checkWifiOnAndConnected()
        }

        temperatureUnits = intent.getBooleanExtra("TemperatureUnits", false)
    }

    override fun onStart() {
        super.onStart()
        checkWifiOnAndConnected()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open_settings -> {
                val intent = Intent(this, ThirdActivity::class.java)
                intent.putExtra("TemperatureUnits", temperatureUnits)
                startActivityForResult(intent, 1)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        temperatureUnits = data.getBooleanExtra("TemperatureUnits", false)
    }


    private fun checkWifiOnAndConnected() {
        progress.visibility = View.VISIBLE
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            recycle_view.visibility = View.GONE
            no_internet_connection.visibility = View.GONE
            recycle_view.layoutManager = LinearLayoutManager(this)

            CustomAsyncTask().execute("http://api.openweathermap.org/data/2.5/forecast?q=Cherkasy&mode=json&APPID=35ddaaf4d566ac6a42ca442b58fb66b2")
        } else {
            progress.visibility = View.GONE
            no_internet_connection.visibility = View.VISIBLE
        }
    }

    private inner class CustomAsyncTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String? {

            val client = OkHttpClient.Builder().build()

            val request = Request.Builder()
                .url(URL)
                .build()
            try {
                val response = client.newCall(request).execute()
                return response.body()?.string()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progress.visibility = View.GONE
            recycle_view.visibility = View.VISIBLE
            val dailyWeatherArray = ArrayList<DailyWeatherList>()

            try {
                if (result != null && result.isNotEmpty()) {

                    val weather = Gson().fromJson(result, DailyWeather::class.java)
                    for (itemWeather in weather.infoDailyWeatherList) {
                        dailyWeatherArray.add(itemWeather)
                    }
                    dailyWeatherAdapter = CustomAdapter(dailyWeatherArray, temperatureUnits)
                    recycle_view.adapter = dailyWeatherAdapter
                }
            } catch (e: Exception) {
            }

        }
    }
}
