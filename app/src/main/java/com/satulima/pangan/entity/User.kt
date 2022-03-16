package com.satulima.pangan.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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

    fun setBirthday(date: String){
        birthday = SimpleDateFormat("dd/MM/yyyy").parse(date)
    }

    fun getBirthday(): String = SimpleDateFormat("dd/MM/yyyy").format(birthday)
    override fun toString(): String {
        return "User(id='$id', email='$email', password='$password', username='$username', firstName='$firstName', lastName='$lastName', gender='$gender', birthday=$birthday, profilePicture='$profilePicture')"
    }


}
