package com.gperalta.moviesandshare.di

import android.content.Context
import com.gperalta.moviesandshare.MoviesAndShareApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MoviesAndShareApplication {
        return app as MoviesAndShareApplication
    }

    @Provides
    @Singleton
    fun provideContext(app: MoviesAndShareApplication) : Context {
        return app.applicationContext
    }

}