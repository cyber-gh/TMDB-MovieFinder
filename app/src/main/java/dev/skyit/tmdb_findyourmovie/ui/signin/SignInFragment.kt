package dev.skyit.tmdb_findyourmovie.ui.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.vvalidator.form
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentSignInBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.repo.UserDetails
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert
import dev.skyit.tmdb_findyourmovie.ui.utils.snack
import dev.skyit.tmdb_findyourmovie.utils.LoadingResult

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {
    private val vModel: SignInViewModel by viewModels()
    private val binding: FragmentSignInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isLoading = true

        form {
            inputLayout(binding.usernameInputField) {
                isEmail()
            }
            inputLayout(binding.passwordInputField) {
                isNotEmpty()
            }

            submitWith(binding.signInButton) {
                vModel.login(binding.usernameInputField.editText!!.text.toString(), binding.passwordInputField.editText!!.text.toString())
            }
        }

        vModel.state.observe(viewLifecycleOwner, {
            isLoading = it is LoadingResult.Loading

            when (it) {
                is LoadingResult.Failure -> errAlert(it.msg)
                is LoadingResult.Success<*> -> {
                    snack("Logged In")
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToNavigationHome())
                }
            }
        })
    }
}