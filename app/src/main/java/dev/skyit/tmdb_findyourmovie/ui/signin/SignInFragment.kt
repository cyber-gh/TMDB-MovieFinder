package dev.skyit.tmdb_findyourmovie.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.vvalidator.form
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.databinding.FragmentSignInBinding
import dev.skyit.tmdb_findyourmovie.generic.BaseFragment
import dev.skyit.tmdb_findyourmovie.repo.UserDetails
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert
import dev.skyit.tmdb_findyourmovie.ui.utils.snack
import dev.skyit.tmdb_findyourmovie.ui.utils.toastl
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val RC_SIGN_IN: Int = 8791
    private val vModel: SignInViewModel by viewModels()
    private val binding: FragmentSignInBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        form {
            inputLayout(binding.emailInputField) {
                isEmail()
            }
            inputLayout(binding.passwordInputField) {
                isNotEmpty()
            }

            submitWith(binding.signInButton) {
                vModel.login(binding.emailInputField.editText!!.text.toString(), binding.passwordInputField.editText!!.text.toString())
            }
        }

        vModel.state.observe(viewLifecycleOwner, {
            isLoading = it is LoadingResource.Loading

            when (it) {
                is LoadingResource.Error -> errAlert(it.errorMessage ?: "Unknown Error")
                is LoadingResource.Success -> {
                    snack("Logged In")
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToNavigationHome())
                }
            }
        })

        binding.goToSignUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.signInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(requireContext(), gso)

        val intent = client.signInIntent
        startActivityForResult(intent,  RC_SIGN_IN)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!
                toastl(account.id ?: "No id")

                vModel.signInWithFirebase(account.idToken!!)
            } catch (ex: Exception) {
                errAlert(ex.localizedMessage)
            }
        }
    }
}