package com.satulima.pangan.service

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseStorageService {
    private val storageReference = FirebaseStorage.getInstance().reference

    fun uploadPhotoProfile(uri: Uri, imageName: String): Flow<StatusState<String>> {
        return flow {
            val imageReference = storageReference.child("users/${imageName}.jpg")
            imageReference.putFile(uri).await()
            val url = imageReference.downloadUrl.await()
            emit(StatusState.success(url.toString()))
        }
    }
}