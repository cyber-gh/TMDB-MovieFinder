package dev.skyit.tmdb_findyourmovie.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentSignUpBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.home.HomeFragmentDirections
import dev.skyit.tmdb_findyourmovie.ui.signin.SignInFragment
import dev.skyit.tmdb_findyourmovie.ui.signin.SignInFragmentDirections

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val vModel: SignUpViewModel by viewModels()
    private val binding: FragmentSignUpBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goToSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
    }
}