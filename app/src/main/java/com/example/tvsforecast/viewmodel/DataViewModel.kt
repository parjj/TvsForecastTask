package com.example.tvsforecast.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.example.tvsforecast.model.roomData.LocalDatabase
import com.example.tvsforecast.model.roomData.entity.CurrentlyData
import com.example.tvsforecast.model.roomData.entity.DailyData

class DataViewModel(application: Application) : AndroidViewModel(application) {

    private val applicationContext = getApplication<Application>().applicationContext

    private var localDB: LocalDatabase? = LocalDatabase.getDB(applicationContext)
    val changeCurrentNotifier = MutableLiveData<CurrentlyData>()
    val changeDailyNotifier = MutableLiveData<List<DailyData>>()

    lateinit var owner: LifecycleOwner

    fun updateData() {

        //fetching all current data
        localDB?.dao()?.fetchAllDataFromCurrent()?.observe(owner!!, Observer {
            if(!it!!.isEmpty()){
                changeCurrentNotifier.value = it!!.get(0)
            }

        })

        //fetching all daily data
        localDB?.dao()?.fetchAllDataFromDaily()?.observe(owner!!, object : Observer<List<DailyData>> {

            override fun onChanged(dailyData: List<DailyData>?) {
                changeDailyNotifier.value = dailyData

            }

        })
    }
}