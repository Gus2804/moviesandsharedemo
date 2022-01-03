package com.gperalta.moviesandshare.data.remote.api

import com.gperalta.moviesandshare.BuildConfig
import com.gperalta.moviesandshare.data.remote.response.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    companion object {
        const val PAGE_SIZE = 20
    }

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page : Int,
                         @Query("api_key") apiKey : String = BuildConfig.MOVIES_API_KEY,
                         @Query("language") language : String = "es-LA") : MoviesResponse

}