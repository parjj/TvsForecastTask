package com.example.tvsforecast.model.retrofitData

import com.example.tvsforecast.model.retrofitData.entity.ForecastData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GetDataService {

    @GET()
    fun getWeatherForecast(@Url url:String):Call<ForecastData>

}