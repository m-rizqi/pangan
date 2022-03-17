package com.satulima.pangan.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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

    override fun toString(): String {
        return "User(id='$id', email='$email', password='$password', username='$username', firstName='$firstName', lastName='$lastName', gender='$gender', birthday=$birthday, profilePicture='$profilePicture')"
    }


}
