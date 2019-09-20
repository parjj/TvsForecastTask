package com.example.tvsforecast.model.roomData.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "DailyData")
data class DailyData(
    @ForeignKey(entity = CurrentlyData::class, parentColumns = ["current_id"], childColumns = ["daily_id"]) val daily_id: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "temperature_high") val temperature_high: Double?,
    @ColumnInfo(name = "temperature_low") val temperature_low: Double?,
    @ColumnInfo(name = "icon") val icon: String?,
    @ColumnInfo(name = "time") val time: Long?,
    @ColumnInfo(name = "sunriseTime") val sunriseTime: Long?,
    @ColumnInfo(name = "sunsetTime") val sunsetTime: Long?,
    @ColumnInfo(name = "category") val category: String?
) {}