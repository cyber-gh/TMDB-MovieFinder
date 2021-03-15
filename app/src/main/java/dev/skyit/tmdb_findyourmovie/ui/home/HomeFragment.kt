package dev.skyit.tmdb_findyourmovie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.Coil
import coil.load
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.models.MovieMinimal
import dev.skyit.tmdb_findyourmovie.databinding.FragmentHomeBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemMovieMinimalBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val vModel: HomeViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinding()

    private lateinit var recommendedMoviesAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemMovieMinimalBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
                    item, v.moviePreview.transitionName, item.id
                ),
                FragmentNavigatorExtras(v.moviePreview to v.moviePreview.transitionName)
            )
        })

        binding.recomendedMoviesList.adapter = recommendedMoviesAdapter
        binding.recomendedMoviesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        vModel.moviesList.observe(viewLifecycleOwner, {
            recommendedMoviesAdapter.updateData(ArrayList(it))
        })


        vModel.loadData()
    }

}