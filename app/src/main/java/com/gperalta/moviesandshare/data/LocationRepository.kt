package com.gperalta.moviesandshare.data

import android.location.Location
import com.gperalta.moviesandshare.data.device.LocationDataSource
import com.gperalta.moviesandshare.data.remote.LocationStoredDataSource
import java.util.*
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val locationStoredDataSource: LocationStoredDataSource
) {

    suspend fun getCurrentLocation() : Location = locationDataSource.getCurrentLocation()

    suspend fun getLastLocationDate() : Date = locationStoredDataSource.getLastStoredLocationTime()

    suspend fun saveLocation(location: Location) {
        locationStoredDataSource.saveLocation(location)
    }

}