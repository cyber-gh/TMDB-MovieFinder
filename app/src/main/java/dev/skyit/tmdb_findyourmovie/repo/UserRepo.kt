package dev.skyit.tmdb_findyourmovie.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class UserDetails(val username: String, val email: String, val profilePic: String? = null)

interface UserRepo {

    val isAuthenticated: Boolean

    val currentUser: UserDetails?

    suspend fun login(email: String, pass: String): UserDetails
    suspend fun signUp(username: String, email: String, pass: String): UserDetails
    suspend fun signOut()
}

class FirebaseUserRepo @Inject constructor(): UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null
    override val currentUser: UserDetails?
        get() = if (isAuthenticated) UserDetails(auth.currentUser?.displayName ?: "Missing Name", auth.currentUser?.email ?: "Missing Email", auth.currentUser?.photoUrl.toString()) else null

    override suspend fun login(email: String, pass: String): UserDetails {
        auth.signInWithEmailAndPassword(email, pass).await()
        return currentUser!!
    }

    override suspend fun signUp(username: String, email: String, pass: String): UserDetails {
        auth.createUserWithEmailAndPassword(email, pass).await()
        auth.currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(username).build()).await()
        return currentUser!!
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}