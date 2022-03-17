package com.satulima.pangan.entity

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.satulima.pangan.utility.toDate
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(
    var id: String,
    var email: String,
    var password: String,
    var username: String,
    var firstname: String,
    var lastname: String,
    var gender: String,
    var birthday: Date,
    var profilePicture: String
) : Parcelable {

    constructor() : this("", "", "", "","","","", Date(), "")

    override fun toString(): String {
        return "User(id='$id', email='$email', password='$password', username='$username', firstName='$firstname', lastName='$lastname', gender='$gender', birthday=$birthday, profilePicture='$profilePicture')"
    }


}
