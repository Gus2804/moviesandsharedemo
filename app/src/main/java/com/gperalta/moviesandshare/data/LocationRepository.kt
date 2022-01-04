package com.gperalta.moviesandshare.data

import android.location.Location
import com.gperalta.moviesandshare.data.local.device.LocationDataSource
import com.gperalta.moviesandshare.data.remote.LocationStoredDataSource
import com.gperalta.moviesandshare.model.OperationResult
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import com.gperalta.moviesandshare.model.Location as LocationModel

class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val locationStoredDataSource: LocationStoredDataSource
) {

    suspend fun getLocations() : Flow<OperationResult<List<LocationModel>>> {
        return locationStoredDataSource.getLocations().map {
            OperationResult.Success(it)
        }.catch {
            OperationResult.Error(it)
        }
    }

    suspend fun getCurrentLocation() : Location = locationDataSource.getCurrentLocation()

    suspend fun getLastLocationDate() : Date? = locationStoredDataSource.getLastStoredLocationTime()

    suspend fun saveLocation(location: Location) {
        locationStoredDataSource.saveLocation(location)
    }
}