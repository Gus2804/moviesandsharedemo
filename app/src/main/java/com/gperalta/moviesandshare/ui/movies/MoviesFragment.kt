package com.gperalta.moviesandshare.ui.movies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.gperalta.moviesandshare.R
import com.gperalta.moviesandshare.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    companion object {
        fun newInstance() = MoviesFragment()
    }

    private val viewModel: MoviesViewModel by activityViewModels()

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var getMoviesJob : Job? = null

    private val adapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMovies.adapter = adapter
        getMovies()
    }

    private fun getMovies() {
        getMoviesJob?.cancel()

        getMoviesJob = lifecycleScope.launch {
            viewModel.getMovies().collectLatest {
                adapter.submitData(it)
            }
        }
    }

}