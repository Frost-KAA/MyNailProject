package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ServiceType (
        val id: Int? = null,
        val name: String? = null,
        val price: Int? = null,
        val time: Int? = null,
        val info: String? = null
)
