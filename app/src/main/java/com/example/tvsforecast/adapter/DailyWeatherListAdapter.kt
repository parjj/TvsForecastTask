package com.example.tvsforecast.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tvsforecast.IconViewClass
import com.example.tvsforecast.MainActivity
import com.example.tvsforecast.R
import com.example.tvsforecast.model.retrofitData.entity.DailyDataDetail
import java.text.SimpleDateFormat

class DailyWeatherListAdapter(var dailyList: List<DailyDataDetail>?, var context: Context) :
    RecyclerView.Adapter<DailyWeatherListAdapter.DailyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): DailyViewHolder {

        var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.daily_forecast_layout, viewGroup, false)
        var viewHolder = DailyViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return dailyList!!.count()
    }

    override fun onBindViewHolder(viewHolder: DailyWeatherListAdapter.DailyViewHolder, pos: Int) {

        var date = MainActivity.timeConverter(dailyList?.get(pos)?.time)

        //setting the day
        viewHolder.day.text = date?.substring(0, 3)

        //setting the sunrise time for the day
        var sunrise = dailyList?.get(pos)?.sunriseTime?.times(1000L)
        var sunrise_t = SimpleDateFormat("HH:mm a")
        val date1 = sunrise_t.format(sunrise)
        viewHolder.sunrise.text = "Sunrise :" + " " + date1

        // //setting the sunset time for the day
        var time_sunset = dailyList?.get(pos)?.sunsetTime?.times(1000L)
        var sunset = SimpleDateFormat("HH:mm a")
        var sunset_time = sunset.format(time_sunset)
        viewHolder.sunset.text = "Sunset :" + " " + sunset_time

        var iconView = IconViewClass(dailyList?.get(pos)?.icon, context)
        var view_icon = iconView.test()

        //setting the icon
        viewHolder.iconSummary.addView(view_icon, 0)

        viewHolder.tempHigh.text =
            dailyList?.get(pos)?.temperatureHigh.toString() + 0x00B0.toChar() // setting the temo high
        viewHolder.tempLow.text =
            dailyList?.get(pos)?.temperatureLow.toString() + 0x00B0.toChar() // setting the temo low

    }


    class DailyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var iconSummary = view.findViewById<LinearLayout>(R.id.icon_daily_datum)
        var tempHigh = view.findViewById<TextView>(R.id.tempH_daily_datum)
        var tempLow = view.findViewById<TextView>(R.id.TempL_daily_datum)
        var day = view.findViewById<TextView>(R.id.day)
        var sunrise = view.findViewById<TextView>(R.id.sunrise_day)
        var sunset = view.findViewById<TextView>(R.id.sunset_day)

    }


}


