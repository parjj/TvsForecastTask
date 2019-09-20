package com.example.tvsforecast.model.roomData.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "CurrentlyData")
data class CurrentlyData(
    @PrimaryKey(autoGenerate = true) val current_id: Int?,
    @ColumnInfo(name = "temperature") val temperature: Double?,
    @ColumnInfo(name = "timezone") val timezone: String?,
    @ColumnInfo(name = "summary") val summary: String?,
    @ColumnInfo(name = "icon") val icon: String?,
    @ColumnInfo(name = "apparentTemperature") val apparentTemperature: Double?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "time") val time: Long?,
    @ColumnInfo(name = "Category") val category: String?,
    @ColumnInfo(name = "daily summary") val daily_summary: String?,
    @ColumnInfo(name = "daily icon") val daily_icon: String?
) {}
