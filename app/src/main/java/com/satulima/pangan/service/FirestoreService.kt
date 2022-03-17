package com.satulima.pangan.service

import com.google.firebase.firestore.FirebaseFirestore
import com.satulima.pangan.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val firestore = FirebaseFirestore.getInstance()

    fun createNewUser(user: User): Flow<StatusState<String>> {
        return flow {
            val userCollection = firestore.collection("users").document(user.id).set(user).await()
            emit(StatusState.success("Success create new user"))
        }
    }
}