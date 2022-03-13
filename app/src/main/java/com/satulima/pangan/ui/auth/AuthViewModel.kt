package com.satulima.pangan.ui.auth

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.satulima.pangan.repository.AuthAppRepository


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppRepository: AuthAppRepository = AuthAppRepository(application)
    val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()
    val loggedOutLiveData: MutableLiveData<Boolean> = authAppRepository.getLoggedOutLiveData()

    @RequiresApi(Build.VERSION_CODES.P)
    fun loginWithEmail(email: String, password: String) {
        authAppRepository.loginWithEmail(email, password)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun registerWithEmail(email: String, password: String) {
        authAppRepository.registerWithEmail(email, password)
    }

    fun logout(){
        authAppRepository.logout()
    }

}