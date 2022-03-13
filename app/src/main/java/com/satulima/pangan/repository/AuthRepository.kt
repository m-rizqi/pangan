package com.satulima.pangan.repository

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthAppRepository(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun registerWithEmail(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor) { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        application.applicationContext,
                        "Registration Failure: " + (task.exception?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun loginWithEmail(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(application.mainExecutor){task ->
                if (task.isSuccessful){
                    userLiveData.postValue(firebaseAuth.currentUser)
                }else{
                    Toast.makeText(
                        application.applicationContext,
                        "Registration Failure: " + (task.exception?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun logout() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    fun getUserLiveData() : MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }

}