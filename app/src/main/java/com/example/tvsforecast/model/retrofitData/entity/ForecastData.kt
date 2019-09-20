package com.example.tvsforecast.model.retrofitData.entity

class ForecastData {

    var latitude: Double? = null
    var longitude: Double? = null
    var timezone: String? = null
    var currently: Currently? = null
    var daily: Daily? = null
    var time: Long?=null

}
