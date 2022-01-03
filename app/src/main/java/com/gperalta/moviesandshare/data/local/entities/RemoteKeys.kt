package com.gperalta.moviesandshare.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey @ColumnInfo(name = "movie_id") val movieId : Long,
    @ColumnInfo(name = "prev_key") val prevKey : Int?,
    @ColumnInfo(name = "next_key") val nextKey : Int?
    )