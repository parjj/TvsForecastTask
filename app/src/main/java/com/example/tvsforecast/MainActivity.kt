package com.example.tvsforecast

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.tvsforecast.adapter.DailyWeatherListAdapter
import com.example.tvsforecast.model.retrofitData.entity.Daily
import com.example.tvsforecast.model.retrofitData.entity.DailyDataDetail
import com.example.tvsforecast.model.retrofitData.entity.ForecastData
import com.example.tvsforecast.model.roomData.LocalDatabase
import com.example.tvsforecast.model.roomData.entity.CurrentlyData
import com.example.tvsforecast.model.roomData.entity.DailyData
import com.example.tvsforecast.viewmodel.DataViewModel
import com.example.tvsforecast.viewmodel.ForecaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {

        fun timeConverter(timeDaily: Long?): String? {
            var date = Date(timeDaily!!.times(1000L));
            var datef = SimpleDateFormat("E,M d yyyy HH:mm a");
            return datef.format(date)
        }

        val TAG: String = this.javaClass.name
        lateinit var current_latitude: String
        lateinit var current_longitude: String
    }

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var region: TextView
    private lateinit var dateTime: TextView
    private lateinit var temperature: TextView
    private lateinit var feelTemperature: TextView
    private lateinit var summeryForDay: TextView
    private lateinit var summary_for_daily: TextView
    private lateinit var icon_for_daily: TextView
    private lateinit var pullToRefresh: SwipeRefreshLayout

    private lateinit var recyclerViewDaily: RecyclerView
    private var dailyAdapter: DailyWeatherListAdapter? = null

    private var localDB: LocalDatabase? = null

    private val REQUEST_CODE = 2
    private var provider_name: String? = null

    private lateinit var daily_list: ArrayList<DailyData>
    private lateinit var dailyData_list: ArrayList<DailyDataDetail>


    private lateinit var foreCastModel : ForecaseViewModel
    private lateinit var dataViewModel : DataViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initial declarations
        init()

        // network availability check
        networkCheckCall()


        //refresh fucntionality
        pullToRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { ->
            pullToRefresh.isRefreshing = false
            networkCheckCall()
        })
    }

    fun init() {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        region = findViewById(R.id.region)
        dateTime = findViewById(R.id.dateTime)
        temperature = findViewById(R.id.temperature)
        feelTemperature = findViewById(R.id.feelsLike)
        summeryForDay = findViewById(R.id.summeryForDay)
        summary_for_daily = findViewById(R.id.summary_daily)
        icon_for_daily = findViewById(R.id.icon_summary)
        recyclerViewDaily = findViewById(R.id.recyclerViewDaily)

        pullToRefresh = findViewById(R.id.refresh)

        localDB = LocalDatabase.getDB(this)

        daily_list = ArrayList<DailyData>()
        dailyData_list = ArrayList<DailyDataDetail>()

        //setting the adapter
        dailyAdapter = DailyWeatherListAdapter(dailyData_list, this)
        recyclerViewDaily.adapter = dailyAdapter


        foreCastModel = ViewModelProviders.of(this).get(ForecaseViewModel::class.java)
        foreCastModel.forecastNotifier.observe(this, object: Observer<ForecastData> {
            override fun onChanged(data: ForecastData?) {
                //insert current data
                InsertCurrentlyTask(data).execute()
                //insert daily data
                InsertDailyTask(data?.daily!!).execute()

                dataViewModel.updateData()

            }
        })


        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        dataViewModel.owner = this
        dataViewModel.changeCurrentNotifier.observe(this, object: Observer<CurrentlyData> {
            override fun onChanged(data: CurrentlyData?) {
                generatedData(data!!)
            }
        })

        dataViewModel.changeDailyNotifier.observe(this, object: Observer<List<DailyData>> {
            override fun onChanged(data: List<DailyData>?) {
                generatedDataForDaily(data!!)
            }
        })

    }

    fun networkCheckCall() {
        //network availability call
        if (isNetworkAvailable()) {
            //get the current location
            getLocations(locationManager)
        }
    }

    //retrieving latitude and longitude
    fun getLocations(locatnManager: LocationManager) {

        //permission check
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), REQUEST_CODE
                )
            }
            return
        }

        // retrieving action
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                getLatLng(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Toast.makeText(applicationContext, "status changed", Toast.LENGTH_LONG).show()
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(applicationContext, "provider enabled", Toast.LENGTH_LONG).show()
            }

            override fun onProviderDisabled(provider: String?) {
                Toast.makeText(applicationContext, "provider disabled", Toast.LENGTH_LONG).show()
            }

        }

        var criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isCostAllowed = false //Returns whether the provider is allowed to incur monetary cost.
        if (locatnManager != null) {
            if (locatnManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locatnManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            ) {
                provider_name = locatnManager.getBestProvider(criteria, true)
            }
            if (provider_name != null) {
                val location_lastKnown = locationManager.getLastKnownLocation(provider_name)
                if (location_lastKnown != null) {
                    getLatLng(location_lastKnown)                                                                                                        // creating the json object for lat and long

                } else {
                    locatnManager.requestLocationUpdates(provider_name, 1000, 5f, this.locationListener)
                }
            } else {
                Toast.makeText(applicationContext, "no provider available", Toast.LENGTH_LONG)
            }
        } else {
            Toast.makeText(applicationContext, "location manager is null", Toast.LENGTH_LONG)
        }
    }


    fun getLatLng(location: Location?) {

        if (location != null) {
            current_latitude = location.latitude.toString()
            current_longitude = location.longitude.toString()

            foreCastModel.updateWeatherData(current_latitude, current_longitude)
        }


    }

    //network availability check
    fun isNetworkAvailable(): Boolean {

        var activeNetworkInfo: NetworkInfo
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected

    }

    //UI update
    private fun generatedData(body: CurrentlyData) {

        var unix_time = timeConverter(body?.time)

        dateTime.text = unix_time

        //setting the city name
        if (body != null) {
            var geoCoder = Geocoder(this, Locale.getDefault())
            var address =
                geoCoder.getFromLocation(body.latitude as Double, body.longitude as Double, 1)
            if (address.size > 0) {
                var cityName = address.get(0).locality
                region.text = cityName
            } else {
                region.text = body?.timezone
            }

            //setting the temperature
            temperature.text = body.temperature.toString() + 0x00B0.toChar()

            //setting the feels like temperature
            feelTemperature.text = body.apparentTemperature.toString() + 0x00B0.toChar()

            //setting the summery for the day
            summeryForDay.text = body.summary

            //setting the weather icon
            iconFix(body?.icon)

            //setting the daily summery
            summary_for_daily.text = body.daily_summary

            // setting the text of daily icon like cloudy, sunny, fog etc
            icon_for_daily.text = body.daily_icon

        }
    }

    fun generatedDataForDaily(body: List<DailyData>?) {

        //fetching the daily list and setting it to the adapter
        dailyData_list.clear()

        body?.forEach { t: DailyData? ->
            dailyData_list.add(
                DailyDataDetail(
                    t?.time,
                    t?.icon,
                    t?.sunriseTime,
                    t?.sunsetTime,
                    t?.temperature_high,
                    t?.temperature_low
                )
            )

        }
        dailyAdapter?.notifyDataSetChanged()

    }

    fun iconFix(iconCurrent: String?) {

        val linearLayoutIcon = findViewById<LinearLayout>(R.id.linearLayoutIcon)
        var iconView = IconViewClass(iconCurrent, this)
        var view_icon = iconView.test()
        linearLayoutIcon.addView(view_icon, 0)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            networkCheckCall();

        }
    }


    //Async Insert task class
    inner class InsertCurrentlyTask(private val data: ForecastData?) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {

            var cu = data?.currently
            var current = CurrentlyData(null, cu?.temperature, data?.timezone, cu?.summary, cu?.icon, cu?.apparentTemperature, data?.latitude, data?.longitude, (cu?.time)?.toLong(), "currently", data?.daily?.summary, data?.daily?.icon)
            localDB?.dao()?.deleteAllData()
            localDB?.dao()?.insertCurrentData(current)

            return null
        }
    }

    inner class InsertDailyTask(private val daily: Daily) : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {

            daily?.data?.forEach { dt: DailyDataDetail ->

                var daily = DailyData(null, null, dt.temperatureHigh, dt.temperatureLow, dt.icon, dt.time, dt.sunriseTime, dt.sunsetTime, "daily")
                localDB?.dao()?.insertDailyData(daily)

            }
            return null
        }
    }

}

