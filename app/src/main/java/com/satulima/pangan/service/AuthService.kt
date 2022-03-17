package com.satulima.pangan.service

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


class AuthService(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.P)
    fun registerWithEmail(email: String, password: String) : Flow<StatusState<FirebaseUser>> {
        return flow {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(StatusState.success(result.user))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    suspend fun loginWithEmail(email: String, password: String): Flow<StatusState<FirebaseUser>>{
        return flow {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(StatusState.success(result.user))
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}