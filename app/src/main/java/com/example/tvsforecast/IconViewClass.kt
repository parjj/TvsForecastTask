package com.example.tvsforecast

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.thbs.skycons.library.*

data class IconViewClass(var iconCurrent: String?,var context: Context) {

    private var viewGroup: SkyconView? = null

    fun test(): SkyconView {

        when (iconCurrent) {
            "clear-day" -> viewGroup = SunView(context, null)
            "clear-night" -> viewGroup = MoonView(context, null)
            "rain" -> viewGroup = CloudRainView(context, null)
            "snow" -> viewGroup = CloudSnowView(context, null)
            "sleet" -> viewGroup = CloudHvRainView(context, null)
            "wind" -> viewGroup = WindView(context, null)
            "fog" -> viewGroup = CloudFogView(context, null)
            "cloudy" -> viewGroup = CloudView(context, null)
            "partly-cloudy-day" -> viewGroup = CloudSunView(context, null)
            "partly-cloudy-night" -> viewGroup = CloudMoonView(context, null)
            "hail" -> viewGroup = CloudSnowView(context, null)
            "thunderstorm" -> viewGroup = CloudThunderView(context, null)
            "tornado" -> viewGroup = WindView(context, null)
            else -> viewGroup = CloudView(context, null)
        }

        viewGroup!!.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        viewGroup!!.bgColor=Color.parseColor("#00000000")
        return viewGroup as SkyconView

    }
}