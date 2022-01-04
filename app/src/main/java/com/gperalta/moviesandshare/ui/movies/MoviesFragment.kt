package com.gperalta.moviesandshare.ui.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.itemLocation -> {
                findNavController(activity!!, R.id.nav_host_fragment).navigate(R.id.action_moviesFragment_to_locationsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

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
        with(binding.toolbar) {
            title = getString(R.string.popular_movies)
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.itemLocation -> {
                        findNavController(activity!!, R.id.nav_host_fragment).navigate(R.id.action_moviesFragment_to_locationsFragment)
                        true
                    }
                    else -> false
                }
            }
        }
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