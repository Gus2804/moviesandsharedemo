package com.gperalta.moviesandshare.ui.locations

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.gperalta.moviesandshare.R
import com.gperalta.moviesandshare.model.Location

class LocationInfoAdapter(private val context : Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.marker_location_info, null)
        val location = marker.tag as? Location
        location?.let {
            view.findViewById<TextView>(R.id.text_view_date).text = it.date.format()
            view.findViewById<TextView>(R.id.text_view_coordinates).text = context.getString(R.string.phone_location_coordinates, it.latitude, it.longitude)
        }
        return view

    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}