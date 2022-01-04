package com.gperalta.moviesandshare.ui.locations

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.gperalta.moviesandshare.R
import com.gperalta.moviesandshare.model.Location
import com.gperalta.moviesandshare.utils.BitmapHelper

class LocationGroupRenderer(
    private val context: Context,
    private val googleMap: GoogleMap,
    clusterManager: ClusterManager<Location>
) : DefaultClusterRenderer<Location>(context, googleMap, clusterManager) {

    private val deviceIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context,
            R.color.teal_200
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.cellphone_wireless,
            color
        )
    }

    override fun onBeforeClusterItemRendered(
        item: Location,
        markerOptions: MarkerOptions
    ) {
        googleMap.setInfoWindowAdapter(LocationInfoAdapter(context))

        markerOptions.title(item.title)
            .snippet(item.snippet)
            .position(item.position)
            .icon(deviceIcon)
    }

    override fun onClusterItemRendered(clusterItem: Location, marker: Marker) {
        marker.tag = clusterItem
    }



}