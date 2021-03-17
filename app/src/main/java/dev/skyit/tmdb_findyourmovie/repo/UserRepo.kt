package dev.skyit.tmdb_findyourmovie.repo

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class UserDetails(val username: String, val email: String, val profilePic: Uri? = null)

interface UserRepo {

    val isAuthenticated: Boolean

    val currentUser: UserDetails?

    suspend fun login(email: String, pass: String): UserDetails
    suspend fun loginWithCredential(credential: AuthCredential): UserDetails
    suspend fun signUp(username: String, email: String, pass: String): UserDetails
    suspend fun signOut()

    suspend fun uploadAvatar(bitmap: Bitmap)
}

class FirebaseUserRepo @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val remoteStorageRepo: RemoteStorageRepo
): UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null
    override val currentUser: UserDetails?
        get() = if (isAuthenticated) UserDetails(auth.currentUser?.displayName ?: "Missing Name", auth.currentUser?.email ?: "Missing Email", auth.currentUser?.photoUrl) else null

    override suspend fun login(email: String, pass: String): UserDetails {
        auth.signInWithEmailAndPassword(email, pass).await()
        return currentUser!!
    }

    override suspend fun loginWithCredential(credential: AuthCredential): UserDetails {
        auth.signInWithCredential(credential).await()
        return currentUser!!
    }

    override suspend fun signUp(username: String, email: String, pass: String): UserDetails {
        auth.createUserWithEmailAndPassword(email, pass).await()
        auth.currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build()).await()
        return currentUser!!
    }

    override suspend fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().await()
    }

    override suspend fun uploadAvatar(bitmap: Bitmap) {
        if (!isAuthenticated) return

        val currentPic = currentUser?.profilePic
        if (currentPic != null) {
            remoteStorageRepo.deletePick(currentPic)
        }
        val link = remoteStorageRepo.uploadImage(bitmap)
        auth.currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setPhotoUri(link).build()).await()

    }
}