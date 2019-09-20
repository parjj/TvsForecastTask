package com.example.tvsforecast.model.retrofitData.entity


data class DailyDataDetail (

    var time: Long? ,
    var icon: String?,
    var sunriseTime: Long?,
    var sunsetTime: Long?,
    var temperatureHigh: Double?,
    var temperatureLow: Double?
){}

