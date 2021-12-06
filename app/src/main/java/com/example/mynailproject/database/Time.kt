package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Time(
    val hour : Int? = null,
    val free: Boolean? = null
)
