package com.gperalta.moviesandshare.di

import android.content.Context
import com.gperalta.moviesandshare.data.local.MovieDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun providesDB(context: Context) : MovieDB {
        return MovieDB.getInstance(context)
    }

}