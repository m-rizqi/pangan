package com.satulima.pangan.entity

import java.util.*

data class User(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val birthday: Date
)
