package dev.skyit.tmdb_findyourmovie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentHomeBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val vModel: HomeViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vModel.loadData()

        vModel.moviesList.observe(viewLifecycleOwner, Observer { movies ->
            binding.testButton.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToMovieDetailsFragment(movies.first()))
            }
        })

        binding.toSignUpButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToSignUpFragment())
        }
    }

}