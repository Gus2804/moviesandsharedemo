package com.gperalta.moviesandshare.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class DataSourceModule {

   @Provides
   fun providesFirestore() : FirebaseFirestore {
       return Firebase.firestore
   }

   @Provides
   fun providesLocationClient(context : Context) : FusedLocationProviderClient {
       return LocationServices.getFusedLocationProviderClient(context)
   }



}