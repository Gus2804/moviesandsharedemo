package com.gperalta.moviesandshare.data

import androidx.paging.DataSource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gperalta.moviesandshare.data.remote.MoviesPagingSource
import com.gperalta.moviesandshare.data.remote.api.MoviesApi
import com.gperalta.moviesandshare.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val dataSource: MoviesPagingSource){

    fun getPopularMovies() : Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                MoviesApi.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = { dataSource }
        ).flow
    }

}