package com.gperalta.moviesandshare.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gperalta.moviesandshare.databinding.ItemMovieBinding
import com.gperalta.moviesandshare.model.Movie

class MoviesAdapter : PagingDataAdapter<Movie, MoviesAdapter.ViewHolder>(MOVIE_COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    inner class ViewHolder(val binding : ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie?) {
            movie?.let {
                binding.ivMoviePoster.load(it.poster)
                binding.tvMovieTitle.text = it.title
            }
        }

    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

}