package dev.skyit.tmdb_findyourmovie.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await


data class UserDetails(val fullName: String, val email: String, val profilePic: String? = null)

interface UserRepo {

    val isAuthenticated: Boolean

    val currentUser: UserDetails?

    suspend fun login(email: String, pass: String)
    suspend fun signUp(fullName: String, email: String, pass: String)
}

class FirebaseUserRepo: UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null
    override val currentUser: UserDetails?
        get() = if (isAuthenticated) UserDetails(auth.currentUser?.displayName ?: "Missing Name", auth.currentUser?.email ?: "Missing Email", auth.currentUser?.photoUrl.toString()) else null

    override suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }

    override suspend fun signUp(fullName: String, email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).await()
        auth.currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(fullName).build()).await()
    }

}