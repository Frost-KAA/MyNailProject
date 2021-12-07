package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ServiceDateCard(
    val hour : Int? = null,
    val date: String? = null,
    var expanded: Boolean? = null,
    val serv: String? = null,
    val price: Int? = null,
    val time: Int? = null,
    val master: String? = null
)
