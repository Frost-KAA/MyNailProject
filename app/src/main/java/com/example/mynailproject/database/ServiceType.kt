package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ServiceType (
        val name: String? = null,
        val price: Int? = null
)
