package com.gperalta.moviesandshare.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.gperalta.moviesandshare.BuildConfig
import com.gperalta.moviesandshare.data.local.MovieDB
import com.gperalta.moviesandshare.data.local.entities.RemoteKeys
import com.gperalta.moviesandshare.data.remote.api.MoviesApi
import com.gperalta.moviesandshare.model.Movie
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MoviesMediator @Inject constructor(private val db : MovieDB, val api : MoviesApi) : RemoteMediator<Int, Movie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val rk = getRemoteKeyClosestToCurrentPosition(state)
                rk?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val rk = getRemoteKeyForFirstItem(state)
                val prevKey = rk?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = rk != null)
                prevKey
            }
            LoadType.APPEND -> {
                val rk = getRemoteKeyForLastItem(state)
                val nextKey = rk?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = rk != null)
                nextKey
            }
        }

        try {
            Log.e("Mediator", "page $page")
            val apiResponse = api.getPopularMovies(page = page)

            val movies = apiResponse.items
            val endOfPaginationReached = movies.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.moviesDao().clearMovies()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    RemoteKeys(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.moviesDao().insertAll(movies.map { Movie(it.id, it.title, BuildConfig.URL_IMAGES + it.posterPath, it.popularity) })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {

        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                db.remoteKeysDao().remoteKeyByMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {

        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                db.remoteKeysDao().remoteKeyByMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? {

        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.id?.let { movieId ->
                db.remoteKeysDao().remoteKeyByMovieId(movieId)
            }
        }
    }

}