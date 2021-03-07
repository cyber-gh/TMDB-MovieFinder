package dev.skyit.tmdb_findyourmovie.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentHomeBinding
import dev.skyit.tmdb_findyourmovie.databinding.FragmentMovieDetailsBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.home.HomeViewModel

class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {
    private val vModel: MovieDetailsViewModel by viewModels()
    private val binding: FragmentMovieDetailsBinding by viewBinding()
    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.movieNameLabel.text = args.movieMinimal.title

//        vModel.loadData()

    }
}