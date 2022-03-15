package com.satulima.pangan.ui.auth

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.satulima.pangan.service.AuthService
import com.satulima.pangan.service.AuthServiceState
import com.satulima.pangan.service.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authService: AuthService = AuthService(application)

    val registerWithEmailState = MutableStateFlow(AuthServiceState(Status.LOADING, FirebaseAuth.getInstance().currentUser, ""))

    @RequiresApi(Build.VERSION_CODES.P)
    fun registerWithEmail(email: String, password: String){
        registerWithEmailState.value = AuthServiceState.loading()

        viewModelScope.launch {
            authService.registerWithEmail(email, password)
                .catch {
                    registerWithEmailState.value = AuthServiceState.error(it.message.toString())
                }
                .collect {
                    registerWithEmailState.value = AuthServiceState.success(it.data)
                }
        }
    }
}