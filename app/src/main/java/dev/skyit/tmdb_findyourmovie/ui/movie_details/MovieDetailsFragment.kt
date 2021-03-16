package dev.skyit.tmdb_findyourmovie.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.models.moviecredits.Cast
import dev.skyit.tmdb_findyourmovie.api.models.moviecredits.MovieCredits
import dev.skyit.tmdb_findyourmovie.api.models.moviedetails.MovieDetails
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.api.models.movievideo.MovieVideo
import dev.skyit.tmdb_findyourmovie.databinding.FragmentMovieDetailsBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemActorBinding
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert
import dev.skyit.tmdb_findyourmovie.ui.utils.setItemSpacing
import dev.skyit.tmdb_findyourmovie.ui.utils.snack
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment(R.layout.fragment_movie_details) {
    private val vModel: MovieDetailsViewModel by viewModels()
    private val binding: FragmentMovieDetailsBinding by viewBinding()
    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var crewAdapter: SimpleRecyclerAdapter<Cast, ListItemActorBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.moviePoster.transitionName = args.imageTransitionId
        super.onViewCreated(view, savedInstanceState)

        bindUI()

        vModel.addToRecentlyWatched(args.movieDb)
    }

    private fun setupListeners(didWatch: Boolean) {
        if (didWatch) {
            binding.btnAddWatchLater.isVisible = false
            binding.btnMarkWatched.text = "Remove from watched"

            binding.btnMarkWatched.setOnClickListener {
                vModel.removeFromWatched(args.movieDb)

                snack("Removed from watched")

                setupListeners(!didWatch)
            }

            return
        }

        binding.btnAddWatchLater.isVisible = true
        binding.btnMarkWatched.text = "Mark as watched"


        binding.btnAddWatchLater.setOnClickListener {
            vModel.addToWatchLater(args.movieDb)

            snack(getString(R.string.added_watch_later))
        }

        binding.btnMarkWatched.setOnClickListener {
            vModel.markAsWatched(args.movieDb)

            snack("Marked as Watched")

            setupListeners(!didWatch)
        }


    }



    private fun bindUI() {

        prepopulateData(args.movieDb)

        vModel.movieDetailsLive.observe(viewLifecycleOwner, {
            isLoading = it is LoadingResource.Loading

            when(it) {
                is LoadingResource.Error -> errAlert(it.errorMessage ?: "Unable to get movie")
                is LoadingResource.Success -> {
                    populateDetails(it.data!!.movieDetails)
                    populateCredits(it.data!!.credits)
                    setTrailerLink(it.data.videos)
                    setupListeners(it.data.didWatch)
                }
            }
        })

        vModel.loadData()

    }

    private fun prepopulateData(movie: MovieDb) {
        binding.movieNameLabel.text = movie.title
        binding.moviePoster.load(movie.backdropPath)
        binding.movieDescription.text = movie.overview ?: "No description available"
        binding.simpleRatingBar.rating = (movie.voteAverage / 2).toFloat()

        parentActivity.setAppBarTitle(movie.title)
    }

    private fun setTrailerLink(videos: List<MovieVideo>) {
        val youtubeVidoe = videos.firstOrNull {
            it.site == "YouTube"
        }

        binding.movieTrailer.setOnClickListener {
            if (youtubeVidoe == null) {
                errAlert("No available video")
            } else {
                findNavController().navigate(MovieDetailsFragmentDirections.actionMovieDetailsFragmentToWebFragment("https://www.youtube.com/watch?v=${youtubeVidoe.key}", youtubeVidoe.name))
            }
        }
    }

    private fun populateDetails(movie: MovieDetails) {
        binding.movieRuntime.text = movie.runtime?.minutesFormatted()
        binding.movieYear.text = movie.releaseDate.take(4)
    }

    private fun populateCredits(credits: MovieCredits) {
        crewAdapter = SimpleRecyclerAdapter({
            ListItemActorBinding.inflate(it)
        }, {data ->
            this.imageView.load(data.profilePath)
            this.actorName.text = data.originalName

            this.actorCharacterName.text = data.character
        })

        binding.movieCrewRecyclerView.adapter = crewAdapter
        binding.movieCrewRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        crewAdapter.updateData(ArrayList(credits.cast))

        binding.movieCrewRecyclerView.setItemSpacing()
    }

    private fun Int.minutesFormatted() : String {
        val minutes = this
        if (minutes < 60) return "${minutes}m"
        return "${minutes / 60}h ${minutes % 60}m"
    }
}