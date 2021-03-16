package dev.skyit.tmdb_findyourmovie.ui.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
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
import dev.skyit.tmdb_findyourmovie.databinding.FragmentProfileBinding
import dev.skyit.tmdb_findyourmovie.databinding.FragmentSearchBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemRecentlyWatchedBinding
import dev.skyit.tmdb_findyourmovie.databinding.ListItemSearchBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.repo.toDbFormat
import dev.skyit.tmdb_findyourmovie.ui.profile.ProfileFragmentDirections
import dev.skyit.tmdb_findyourmovie.ui.profile.ProfileViewModel
import dev.skyit.tmdb_findyourmovie.ui.utils.SimpleRecyclerAdapter
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private val vModel: SearchViewModel by viewModels()
    private val binding: FragmentSearchBinding by viewBinding()

    private lateinit var filteredMoviesAdapter: SimpleRecyclerAdapter<MovieMinimal, ListItemSearchBinding>

    private fun buildFilteredMoviesList() {
        filteredMoviesAdapter = SimpleRecyclerAdapter({
            ListItemSearchBinding.inflate(it)
        }, { data ->
            this.moviePreview.transitionName = data.id.toString()
            if (data.posterPath == null) {
                this.moviePreview.setImageResource(R.drawable.no_photo)
            } else {
                this.moviePreview.load(data.posterPath)
            }
            this.movieTitle.text = data.title
            this.movieYear.text = data.releaseDate.take(4)

            Coil.enqueue(
                ImageRequest.Builder(requireContext())
                .data(data.backdropPath)
                .build())
        }, onItemClick = { v, item ->
            findNavController().navigate(SearchFragmentDirections
                .actionNavigationSearchToMovieDetailsFragment(
                    item.toDbFormat(), v.moviePreview.transitionName, item.id
                ),
                FragmentNavigatorExtras(v.moviePreview to v.moviePreview.transitionName)
            )
        })

        binding.searchList.adapter = filteredMoviesAdapter
        binding.searchList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        vModel.searchResults.observe(viewLifecycleOwner, {
            filteredMoviesAdapter.updateData(ArrayList(it))
        })

        vModel.errorState.observe(viewLifecycleOwner, {
            errAlert("Error ${it}")
        })
    }

    private fun bindUI() {
//        binding.searchEditText.requestFocus()

        val imm: InputMethodManager? = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)

        binding.searchEditText.addTextChangedListener {
            val str = it?.toString() ?: return@addTextChangedListener
            vModel.newSearch(str)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()

        buildFilteredMoviesList()
    }
}