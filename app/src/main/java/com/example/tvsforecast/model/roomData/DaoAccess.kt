package com.example.tvsforecast.model.roomData

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.tvsforecast.model.roomData.entity.CurrentlyData
import com.example.tvsforecast.model.roomData.entity.DailyData

@Dao
interface DaoAccess {

    //Insert current data
    @Insert
    fun insertCurrentData(currentlyData: CurrentlyData)

    //Insert daily data
    @Insert
    fun insertDailyData(dailyData: DailyData)

    //Fetch all data's from currently
    @Query("SELECT * FROM CurrentlyData")
    fun fetchAllDataFromCurrent(): LiveData<List<CurrentlyData>>

    //Fetch all data's from daily
    @Query("SELECT * FROM DailyData")
    fun fetchAllDataFromDaily(): LiveData<List<DailyData>>

    //Delete all data
    @Query("DELETE FROM CurrentlyData")
    fun deleteAllData()

}