package com.gperalta.moviesandshare.ui.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gperalta.moviesandshare.data.LocationRepository
import com.gperalta.moviesandshare.model.Location
import com.gperalta.moviesandshare.model.OperationResult
import com.gperalta.moviesandshare.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val repository: LocationRepository): ViewModel() {

    private val _locations = MutableStateFlow<UIState<List<Location>>>(UIState.Idle)
    val locations : StateFlow<UIState<List<Location>>> = _locations

    val locationCache = mutableListOf<Location>()

    fun getLocations() {
        viewModelScope.launch {
            repository.getLocations().collect {
                when(it) {
                    is OperationResult.Error -> _locations.value = UIState.Error(it.throwable)
                    is OperationResult.Success -> {
                        val diff = it.data?.filter { location -> locationCache.find { cached -> location.date == cached.date } == null }?: listOf()
                        locationCache.addAll(diff)
                        _locations.value = UIState.Success(diff)
                    }
                }
            }
        }
    }

}