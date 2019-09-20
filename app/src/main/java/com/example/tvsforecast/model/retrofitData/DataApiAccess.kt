package com.example.tvsforecast.model.retrofitData

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

class DataApiAccess {

    private val apiKey="308949c1bacb34f18661a6f186a9198f"
    private val url_string = "https://api.darksky.net/forecast/$apiKey/"


    private var getDataService: GetDataService? =null

    fun getRetrofitInstance(): GetDataService? {
        if (getDataService == null) {
           val  retrofit = Retrofit.Builder()
                .baseUrl(url_string)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            getDataService= retrofit.create(GetDataService::class.java)
        }
        return getDataService
    }

}

