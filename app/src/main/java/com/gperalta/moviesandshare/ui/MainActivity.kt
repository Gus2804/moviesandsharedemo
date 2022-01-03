package com.gperalta.moviesandshare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gperalta.moviesandshare.R
import com.gperalta.moviesandshare.ui.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MoviesFragment.newInstance())
                .commitNow()
        }
    }
}