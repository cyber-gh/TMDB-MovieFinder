package dev.skyit.tmdb_findyourmovie.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val vModel: SignInViewModel by viewModels()
    private val binding: FragmentSignInBinding by viewBinding()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    //WHY here?, Because
    //Fragment SignInFragment{7732b77} (1d1a6e0c-031c-4b93-ab2c-187a3c244654) id=0x7f0a0145} is attempting to registerForActivityResult after being created. Fragments must call registerForActivityResult() before they are created (i.e. initialization, onAttach(), or onCreate()).
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)!!
            toastl("Logged in with ${account.email}")

            vModel.signInWithFirebase(account.idToken!!)
        } catch (ex: Exception) {
            toastl(ex.localizedMessage)
        }

    }

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
        val client = googleSignInClient
        val intent = client.signInIntent
        resultLauncher.launch(intent)
    }
}