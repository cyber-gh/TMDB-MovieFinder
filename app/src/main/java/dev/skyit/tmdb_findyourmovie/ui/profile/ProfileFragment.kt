package dev.skyit.tmdb_findyourmovie.ui.profile

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
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentProfileBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val vModel: ProfileViewModel by viewModels()
    private val binding: FragmentProfileBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signInBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToSignInFragment())
        }

    }
}