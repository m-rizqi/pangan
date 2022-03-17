package com.satulima.pangan.service

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.satulima.pangan.entity.User
import com.satulima.pangan.utility.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirestoreService {
    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection("users")

    fun createNewUser(user: User): Flow<StatusState<String>> {
        return flow {
            userCollection.document(user.id).set(user).await()
            emit(StatusState.success("Success create new user"))
        }
    }

    fun getUserByEmail(email: String): Flow<StatusState<User>>{
        return flow {
            val snapshot = userCollection.whereEqualTo("email", email).get().await()
            if (snapshot.documents.isEmpty()) throw Exception("User is not exist")
            emit(StatusState.success(snapshot.documents[0].toObject(User::class.java)))
        }
    }
}
