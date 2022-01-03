package com.gperalta.moviesandshare.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gperalta.moviesandshare.R
import com.gperalta.moviesandshare.data.LocationRepository
import com.gperalta.moviesandshare.utils.LOCATION_INTERVAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LocationServices : LifecycleService() {

    private val TAG = "LocationService"

    lateinit var notificationManager: NotificationManager

    private var timer : Job? = null

    @Inject
    lateinit var repository: LocationRepository

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getNotification(text : String = getString(R.string.waiting_location)) : Notification {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(chan)
        }
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startTimer()
        startForeground(SERVICE_ID, getNotification())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy")
        timer?.cancel()
    }

    private fun startTimer() {
        timer = lifecycleScope.launch(Dispatchers.IO) {
            val difInMillis = Date().time - repository.getLastLocationDate().time
            val firstDelay = if(difInMillis <= LOCATION_INTERVAL) {
                LOCATION_INTERVAL - difInMillis
            } else 0
            delay(firstDelay)
            while (true) {
                _requestLocation.value = Date()
                delay(LOCATION_INTERVAL)
            }
        }

        lifecycleScope.launch {
            requestLocation.collectLatest {
                val location = repository.getCurrentLocation()
                notificationManager.notify(SERVICE_ID, getNotification(getString(R.string.sending_location)))
                repository.saveLocation(location)
                delay(5000)
                notificationManager.notify(SERVICE_ID, getNotification())
            }
        }
    }

    companion object {
        const val SERVICE_ID = 222
        const val NOTIFICATION_CHANNEL_ID = "movies_and_share_channel_id"
        const val NOTIFICATION_CHANNEL_NAME = "movies_and_share_channel_name"

        private val _requestLocation = MutableStateFlow(Date())
        val requestLocation : StateFlow<Date> = _requestLocation

    }

}