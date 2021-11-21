package com.example.mynailproject.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Master (
        val uid : String? = null,
        val info: String? = null,
        val photo: String? = null,
        val user: User? = null
)