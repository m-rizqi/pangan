package com.satulima.pangan.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(
    var email: String,
    var firstName: String,
    var lastName: String,
    val gender: String,
    val birthday: Date
) : Parcelable {
    constructor() : this("","","","", Date())

    override fun toString(): String {
        return "User(email='$email', firstName='$firstName', lastName='$lastName', gender='$gender', birthday=$birthday)"
    }


}
