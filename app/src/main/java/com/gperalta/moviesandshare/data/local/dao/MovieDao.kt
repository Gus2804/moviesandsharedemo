package com.gperalta.moviesandshare.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gperalta.moviesandshare.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun popularMovies() : PagingSource<Int, Movie>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}