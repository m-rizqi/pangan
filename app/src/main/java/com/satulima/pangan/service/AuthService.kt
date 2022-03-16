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
        return flow{
            val authInformation = MutableLiveData<StatusState<FirebaseUser>>()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.mainExecutor){task ->
                    if (task.isSuccessful) {
                        authInformation.postValue(StatusState.success(firebaseAuth.currentUser))
                    }else{
                        var msg = ""
                        task.exception?.let {
                            msg = it.message.toString()
                        }
                        authInformation.postValue(StatusState.error(msg))
                    }
                }
            authInformation.value?.let {
                emit(it)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun logout() {
        firebaseAuth.signOut()
    }

}