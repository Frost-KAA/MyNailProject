package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    val surname: String? = null,
    val name: String? = null,
    val pathronim: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val role: String? = null
)