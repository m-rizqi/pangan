package com.satulima.pangan.entity

import android.content.ContentValues.TAG
import android.os.Parcelable
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class User(
    var id: String,
    var email: String,
    var password: String,
    var username: String,
    var firstName: String,
    var lastName: String,
    var gender: String,
    var birthday: Date,
    var profilePicture: String
) : Parcelable {

    constructor() : this("", "", "", "","","","", Date(), "")

    companion object {
        fun DocumentSnapshot.toUser(): User {
            val id = getString("id").toString()
            val email = getString("email").toString()
            val password = getString("password").toString()
            val username = getString("username").toString()
            val firstname = getString("firstname").toString()
            val lastname = getString("lastname").toString()
            val gender = getString("gender").toString()
            val birthday = SimpleDateFormat("dd/MM/yyyy").parse(getString("birthday"))
            val profilePicture = getString("profilePicture").toString()
            return User(id, email ,password, username, firstname, lastname, gender, birthday, profilePicture)
        }
    }

    fun setBirthday(date: String){
        birthday = SimpleDateFormat("dd/MM/yyyy").parse(date)
    }

    fun getBirthday(): String {
        val temp = SimpleDateFormat("dd/MM/yyyy").format(birthday)
        val now = SimpleDateFormat("dd/MM/yyyy").format(Date())
        return if (temp == now) "" else temp
    }
    override fun toString(): String {
        return "User(id='$id', email='$email', password='$password', username='$username', firstName='$firstName', lastName='$lastName', gender='$gender', birthday=$birthday, profilePicture='$profilePicture')"
    }


}
