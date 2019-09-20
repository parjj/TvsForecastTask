package com.example.tvsforecast.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.widget.Toast
import com.example.tvsforecast.MainActivity
import com.example.tvsforecast.model.retrofitData.DataApiAccess
import com.example.tvsforecast.model.retrofitData.entity.ForecastData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecaseViewModel(application: Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext

    val forecastNotifier = MutableLiveData<ForecastData>()

    fun updateWeatherData(currentLatitude: String, currentLongitude: String) {

        var append_url = currentLatitude + "," + currentLongitude + "?" + "exclude=minutely,hourly,alerts,flags"

        var forecastData = DataApiAccess().getRetrofitInstance()?.getWeatherForecast(append_url)  // getWeather from dataApi
        Log.d(MainActivity.TAG, forecastData?.request()!!.url().toString())

        forecastData?.enqueue(object : Callback<ForecastData> {

            override fun onResponse(call: Call<ForecastData>, response: Response<ForecastData>) {

                var data = response.body()
                forecastNotifier.value = data

            }

            override fun onFailure(call: Call<ForecastData>, t: Throwable) {

                Toast.makeText(applicationContext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT)
                    .show();
            }
        })
    }

}