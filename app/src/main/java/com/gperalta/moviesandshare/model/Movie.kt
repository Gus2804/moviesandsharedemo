package com.gperalta.moviesandshare.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey val id : Long,
    val title : String,
    val poster : String,
    val popularity : Double
)
