package dev.skyit.tmdb_findyourmovie.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.Coil
import coil.load
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.databinding.FragmentProfileBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemRecentlyWatchedBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val vModel: ProfileViewModel by viewModels()
    private val binding: FragmentProfileBinding by viewBinding()

    private lateinit var recentlyWatchedMoviesAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemRecentlyWatchedBinding>

    private fun setupRecentlyList() {
        binding.signInBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToSignInFragment())
        }

        recentlyWatchedMoviesAdapter = SimpleRecyclerAdapter({
            ListItemRecentlyWatchedBinding.inflate(it)
        }, { data ->
            this.moviePreview.transitionName = data.id.toString()
            this.moviePreview.load(data.posterPath)
            this.moviePreviewName.text = data.title
            this.simpleRatingBar.isVisible = false

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

        binding.recentlyWatchedList.adapter = recentlyWatchedMoviesAdapter
        binding.recentlyWatchedList.layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        vModel.moviesList.observe(viewLifecycleOwner, {
            recentlyWatchedMoviesAdapter.updateData(ArrayList(it))
        })


        vModel.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecentlyList()


    }

    override fun onResume() {
        super.onResume()


        binding.signInBtn.isVisible = vModel.isAuth

        if (!vModel.isAuth) {
            binding.username.text = "Guest User"
        }
    }
}