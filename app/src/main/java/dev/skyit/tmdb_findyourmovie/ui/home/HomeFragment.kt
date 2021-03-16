package dev.skyit.tmdb_findyourmovie.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.Coil
import coil.load
import coil.request.ImageRequest
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.azoft.carousellayoutmanager.CenterScrollListener
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.databinding.FragmentHomeBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemMovieMinimalBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemMoviePosterBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.repo.toDbFormat
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val vModel: HomeViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinding()

    private lateinit var recommendedMoviesAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemMovieMinimalBinding>
    private lateinit var trendingMoviesAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemMoviePosterBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecommendedMovies()
        setupTrendingMovies()
        vModel.loadData()

        vModel.errorLive.observe(viewLifecycleOwner, Observer {
            errAlert(it)
        })

    }

    private fun setupTrendingMovies() {
        trendingMoviesAdapter = SimpleRecyclerAdapter({
            ListItemMoviePosterBinding.inflate(it)
        }, {data ->
            this.imageView2.load(data.backdropPath)
            this.imageView2.transitionName = data.id.toString()
            this.movieName.text = data.title
        }, onItemClick = {v, item ->
            findNavController().navigate(HomeFragmentDirections
                .actionNavigationHomeToMovieDetailsFragment(
                    item.toDbFormat(), v.imageView2.transitionName, item.id
                ),
                FragmentNavigatorExtras(v.imageView2 to v.imageView2.transitionName)
            )
        })
        binding.trendingMoviesList.adapter = trendingMoviesAdapter
        binding.trendingMoviesList.layoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL).apply {
            setPostLayoutListener(CarouselZoomPostLayoutListener())
        }
        binding.trendingMoviesList.setHasFixedSize(true)

        binding.trendingMoviesList.addOnScrollListener(CenterScrollListener())

        vModel.trendingMoviesLive.observe(viewLifecycleOwner, {
            trendingMoviesAdapter.updateData(ArrayList(it))
        })

    }

    private fun setupRecommendedMovies() {
        recommendedMoviesAdapter = SimpleRecyclerAdapter({
            ListItemMovieMinimalBinding.inflate(it)
        }, { data ->
            this.moviePreview.transitionName = data.id.toString()
            this.moviePreview.load(data.posterPath)
            this.moviePreviewName.text = data.title
            this.simpleRatingBar.rating = (data.voteAverage / 2).toFloat()
            Coil.enqueue(ImageRequest.Builder(requireContext())
                .data(data.backdropPath)
                .build())
        }, onItemClick = { v, item ->
            findNavController().navigate(HomeFragmentDirections
                .actionNavigationHomeToMovieDetailsFragment(
                    item.toDbFormat(), v.moviePreview.transitionName, item.id
                ),
                FragmentNavigatorExtras(v.moviePreview to v.moviePreview.transitionName)
            )
        })

        binding.recomendedMoviesList.adapter = recommendedMoviesAdapter
        binding.recomendedMoviesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recomendedMoviesList.setHasFixedSize(true)

        vModel.recommendedMoviesLive.observe(viewLifecycleOwner, {
            recommendedMoviesAdapter.updateData(ArrayList(it))
        })



    }

}