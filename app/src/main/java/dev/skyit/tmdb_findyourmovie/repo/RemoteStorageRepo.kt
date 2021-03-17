package dev.skyit.tmdb_findyourmovie.repo

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

interface RemoteStorageRepo {
    suspend fun uploadImage(bitmap: Bitmap): Uri // returns the url
    suspend fun deletePick(uri: Uri)
}

class FirebaseRemoteStorageRepo @Inject constructor(): RemoteStorageRepo {
    private val storage = Firebase.storage

    override suspend fun uploadImage(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes)

        val data = bytes.toByteArray()

        val remoteFile = storage.reference.child("user_avatars/${UUID.randomUUID().toString()}.jpeg")

        val uploadedFile = remoteFile.putBytes(data).await()

        return uploadedFile.storage.downloadUrl.await()
    }

    override suspend fun deletePick(uri: Uri) {
        val remoteFile: StorageReference? = kotlin.runCatching { storage.getReferenceFromUrl(uri.toString()) }.getOrNull()

        remoteFile?.delete()?.await()
    }

}