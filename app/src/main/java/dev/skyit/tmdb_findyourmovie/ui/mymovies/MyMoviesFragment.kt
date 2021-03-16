package dev.skyit.tmdb_findyourmovie.ui.mymovies

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.Coil
import coil.load
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.databinding.FragmentMyMoviesBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemMovieAlreadyWatchedBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemMovieWatchLaterBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemRecentlyWatchedBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.profile.ProfileFragmentDirections
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter

@AndroidEntryPoint
class MyMoviesFragment : BaseFragment(R.layout.fragment_my_movies) {

    private val vModel: MyMoviesViewModel by viewModels()
    private val binding: FragmentMyMoviesBinding by viewBinding()

    private lateinit var watchLaterAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemMovieWatchLaterBinding>
    private lateinit var alreadyWatchedAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemMovieAlreadyWatchedBinding>

    private fun buildWatchLaterList() {
        watchLaterAdapter = SimpleRecyclerAdapter({
            ListItemMovieWatchLaterBinding.inflate(it)
        }, { data ->
            this.moviePreview.transitionName = data.id.toString()
            this.moviePreview.load(data.posterPath)
            this.moviePreviewName.text = data.title

            Coil.enqueue(ImageRequest.Builder(requireContext())
                    .data(data.backdropPath)
                    .build())
        }, onItemClick = { v, item ->
            findNavController().navigate(ProfileFragmentDirections
                    .actionNavigationProfileToMovieDetailsFragment(
                            item, v.moviePreview.transitionName, item.id
                    ),
                    FragmentNavigatorExtras(v.moviePreview to v.moviePreview.transitionName)
            )
        })

        binding.watchLaterList.adapter = watchLaterAdapter
        binding.watchLaterList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        vModel.moviesList.observe(viewLifecycleOwner, {
            watchLaterAdapter.updateData(ArrayList(it))
        })

        vModel.loadData()
    }

    private fun buildAlreadyWatchedList() {

    }

    private fun bindUI() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()

        buildWatchLaterList()

        buildAlreadyWatchedList()
    }
}