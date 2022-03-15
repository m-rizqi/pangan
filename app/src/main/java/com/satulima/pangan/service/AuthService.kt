package com.satulima.pangan.service

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AuthService(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.P)
    fun registerWithEmail(email: String, password: String) : Flow<AuthServiceState<FirebaseUser>> {
        return flow {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(AuthServiceState.success(result.user))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    suspend fun loginWithEmail(email: String, password: String): Flow<AuthServiceState<FirebaseUser>>{
        return flow{
            val authInformation = MutableLiveData<AuthServiceState<FirebaseUser>>()
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.mainExecutor){task ->
                    if (task.isSuccessful) {
                        authInformation.postValue(AuthServiceState.success(firebaseAuth.currentUser))
                    }else{
                        var msg = ""
                        task.exception?.let {
                            msg = it.message.toString()
                        }
                        authInformation.postValue(AuthServiceState.error(msg))
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