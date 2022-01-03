package com.gperalta.moviesandshare.data.remote

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.gperalta.moviesandshare.utils.DEVICE_ID
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class LocationStoredDataSource @Inject constructor(private val db : FirebaseFirestore, private val preferences: SharedPreferences) {

    suspend fun getLastStoredLocationTime() : Date {
        val docId = getDeviceId()
        val doc = db.collection(FirestoreKeys.DEVICES).document(docId)
        val locations = doc.collection(FirestoreKeys.LOCATIONS).orderBy(FirestoreKeys.DATE, Query.Direction.DESCENDING).get().await()
        return if(locations.isEmpty)
            Date()
        else {
            val timeStamp = locations.documents[0].data?.get(FirestoreKeys.DATE) as Timestamp?
            timeStamp?.toDate() ?: Date()
        }
    }

    suspend fun saveLocation(location: Location) {
        val documentId = getDeviceId()

        val locationData = hashMapOf(
            FirestoreKeys.DATE to Timestamp.now(),
            FirestoreKeys.LATITUDE to location.latitude,
            FirestoreKeys.LONGITUDE to location.longitude
        )

        db.collection(FirestoreKeys.DEVICES).document(documentId)
            .collection(FirestoreKeys.LOCATIONS)
            .add(locationData).await()
    }

    private fun getDeviceId() : String {
        val docId = preferences.getString(DEVICE_ID, null) ?: UUID.randomUUID().toString()
        if(!preferences.contains(DEVICE_ID)) {
            preferences.edit {
                putString(DEVICE_ID, docId)
                apply()
            }
        }
        return docId
    }

    object FirestoreKeys {
        const val DEVICES = "devices"
        const val LOCATIONS = "locations"
        const val DATE = "date"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

}