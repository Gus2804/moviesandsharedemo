package com.gperalta.moviesandshare.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gperalta.moviesandshare.data.MoviesRepository
import com.gperalta.moviesandshare.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MoviesRepository) : ViewModel() {

    fun getMovies() : Flow<PagingData<Movie>>{
        return repository.getPopularMovies().cachedIn(viewModelScope)
    }

}