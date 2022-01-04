package com.gperalta.moviesandshare.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.gperalta.moviesandshare.ui.locations.format
import java.util.*

data class Location(
    val date : Date,
    val latitude : Double,
    val longitude : Double
) : ClusterItem {
    override fun getPosition(): LatLng =
        LatLng(latitude, longitude)

    override fun getTitle(): String =
        "Ubicaciones"

    override fun getSnippet(): String =
        date.format()?:""
}
