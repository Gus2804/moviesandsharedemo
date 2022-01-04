package com.gperalta.moviesandshare.data.remote.response

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page : Int,
    @SerializedName("total_pages") val totalPages : Int,
    @SerializedName("total_results") val totalResults : Int,
    @SerializedName("results") val items : List<MovieDto>,
)
