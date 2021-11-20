package com.example.mynailproject.database

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

class DBCall {

    private var database: DatabaseReference = Firebase.database.reference

    fun addNewUser(uid: String, user: User){
        database.child("users").child(uid).setValue(user)
    }

    fun addNewServType(id: Int, ser: ServiceType){
        database.child("service_type").child("00"+id.toString()).setValue(ser)
    }

    fun deleteServType(id: Int){
        database.child("service_type").child("00"+id.toString()).removeValue()
    }


}