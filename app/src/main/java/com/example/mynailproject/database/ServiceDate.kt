package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ServiceDate(
    val hour : Int? = null,
    val serv_id: String? = null,
    val master_uid: String? = null,
    val date: String? = null,
    val notificat: Boolean? = null
)
