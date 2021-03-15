package dev.skyit.tmdb_findyourmovie.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.vvalidator.form
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentSignUpBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.ui.home.HomeFragmentDirections
import dev.skyit.tmdb_findyourmovie.ui.signin.SignInFragment
import dev.skyit.tmdb_findyourmovie.ui.signin.SignInFragmentDirections
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert
import dev.skyit.tmdb_findyourmovie.ui.utils.snack
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private val vModel: SignUpViewModel by viewModels()
    private val binding: FragmentSignUpBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        form {
            inputLayout(binding.usernameInputField) {
                isNotEmpty()
            }
            inputLayout(binding.emailInputField) {
                isEmail()
            }
            inputLayout(binding.passwordInputField) {
                isNotEmpty()
            }

            submitWith(binding.signUpButton) {
                vModel.signUp(binding.usernameInputField.editText!!.text.toString(), binding.emailInputField.editText!!.text.toString(), binding.passwordInputField.editText!!.text.toString())
            }
        }

        vModel.state.observe(viewLifecycleOwner, {
            isLoading = it is LoadingResource.Loading

            when (it) {
                is LoadingResource.Error -> errAlert(it.errorMessage ?: "Unknown Error")
                is LoadingResource.Success -> {
                    snack("Welcome ${it.data!!.username}")
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToNavigationHome())
                }
            }
        })

        binding.goToSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
    }
}