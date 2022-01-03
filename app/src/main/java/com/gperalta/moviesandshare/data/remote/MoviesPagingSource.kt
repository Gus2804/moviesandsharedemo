package com.gperalta.moviesandshare.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gperalta.moviesandshare.BuildConfig
import com.gperalta.moviesandshare.data.MoviesRepository
import com.gperalta.moviesandshare.data.remote.api.MoviesApi
import com.gperalta.moviesandshare.model.Movie
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(private val api : MoviesApi) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val response = api.getPopularMovies(page = position)
            val movies = response.items
            val nextKey = if (movies.isEmpty())
                null
            else{
                position + (params.loadSize / MoviesApi.PAGE_SIZE)
            }
            LoadResult.Page(
                data = movies.map {
                    Movie(it.title, BuildConfig.URL_IMAGES + it.posterPath)
                },
                prevKey = if(position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}