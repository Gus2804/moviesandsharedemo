package com.gperalta.moviesandshare.data

import androidx.paging.*
import com.gperalta.moviesandshare.data.local.MovieDB
import com.gperalta.moviesandshare.data.mediator.MoviesMediator
import com.gperalta.moviesandshare.data.remote.api.MoviesApi
import com.gperalta.moviesandshare.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(val db : MovieDB, private val mediator: MoviesMediator){

    @OptIn(ExperimentalPagingApi::class)
    fun getPopularMovies() : Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                MoviesApi.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { db.moviesDao().popularMovies() },
            remoteMediator = mediator
        ).flow
    }

}