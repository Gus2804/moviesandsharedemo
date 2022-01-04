package com.gperalta.moviesandshare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gperalta.moviesandshare.data.local.dao.MovieDao
import com.gperalta.moviesandshare.data.local.dao.RemoteKeyDao
import com.gperalta.moviesandshare.data.local.entities.RemoteKeys
import com.gperalta.moviesandshare.model.Movie

@Database(
    entities = [Movie::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDB : RoomDatabase() {

    abstract fun moviesDao() : MovieDao
    abstract fun remoteKeysDao() : RemoteKeyDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDB? = null

        fun getInstance(context: Context): MovieDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                MovieDB::class.java, "Movies.db")
                .build()
    }


}