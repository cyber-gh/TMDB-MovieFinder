package dev.skyit.tmdb_findyourmovie.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.models.MovieMinimal
import dev.skyit.tmdb_findyourmovie.databinding.FragmentProfileBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemMovieMinimalBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemRecentlyWatchedBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.home.HomeFragmentDirections
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val vModel: ProfileViewModel by viewModels()
    private val binding: FragmentProfileBinding by viewBinding()

    private lateinit var recentlyWatchedMoviesAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemRecentlyWatchedBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signInBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToSignInFragment())
        }

        recentlyWatchedMoviesAdapter = SimpleRecyclerAdapter({
            ListItemRecentlyWatchedBinding.inflate(it)
        }, { data ->
            this.moviePreview.transitionName = data.id.toString()
            this.moviePreview.load(data.posterPath)
            this.moviePreviewName.text = data.title
            this.simpleRatingBar.rating = (data.voteAverage / 2).toFloat()
        }, onItemClick = { v, item ->
            findNavController().navigate(HomeFragmentDirections
                    .actionNavigationHomeToMovieDetailsFragment(
                            item, v.moviePreview.transitionName
                    ),
                    FragmentNavigatorExtras(v.moviePreview to v.moviePreview.transitionName)
            )
        })

        binding.recentlyWatchedList.adapter = recentlyWatchedMoviesAdapter
        binding.recentlyWatchedList.layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        vModel.moviesList.observe(viewLifecycleOwner, {
            recentlyWatchedMoviesAdapter.updateData(ArrayList(it))
        })


        vModel.loadData()
    }
}