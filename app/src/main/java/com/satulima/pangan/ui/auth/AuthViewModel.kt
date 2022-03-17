package com.satulima.pangan.ui.auth

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.satulima.pangan.entity.User
import com.satulima.pangan.service.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authService: AuthService = AuthService(application)
    private val storageService: FirebaseStorageService = FirebaseStorageService()
    private val firestoreService: FirestoreService = FirestoreService()

    val registerWithEmailState = MutableStateFlow(StatusState(Status.LOADING, FirebaseAuth.getInstance().currentUser, ""))
    val uploadPhotoState = MutableStateFlow(StatusState(Status.LOADING, "", ""))
    val createNewUserState = MutableStateFlow(StatusState(Status.LOADING, "", ""))

    @RequiresApi(Build.VERSION_CODES.P)
    fun registerWithEmail(email: String, password: String){
        registerWithEmailState.value = StatusState.loading()

        viewModelScope.launch {
            authService.registerWithEmail(email, password)
                .catch {
                    registerWithEmailState.value = StatusState.error(it.message.toString())
                }
                .collect {
                    registerWithEmailState.value = StatusState.success(it.data)
                }
        }
    }

    fun uploadPhoto(uri: Uri, imageName : String){
        uploadPhotoState.value = StatusState.loading()
        viewModelScope.launch {
            storageService.uploadPhotoProfile(uri, imageName)
                .catch {
                    uploadPhotoState.value = StatusState.error(it.message.toString())
                }
                .collect {
                    uploadPhotoState.value = StatusState.success(it.data)
                }
        }
    }

    fun createNewUser(user: User){
        createNewUserState.value = StatusState.loading()
        viewModelScope.launch {
            firestoreService.createNewUser(user)
                .catch {
                    createNewUserState.value = StatusState.error(it.message.toString())
                }
                .collect {
                    createNewUserState.value = StatusState.success(it.data)
                }
        }
    }
}