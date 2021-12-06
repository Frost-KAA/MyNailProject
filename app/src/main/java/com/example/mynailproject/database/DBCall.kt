package com.example.mynailproject.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
        if (user.role=="master"){
            val m = Master(uid, null, user)
            database.child("masters").child(uid).setValue(m)
        }
    }

    fun addNewServType(id: Int, ser: ServiceType){
        database.child("service_type").child("00"+id.toString()).setValue(ser)
    }

    fun deleteServType(id: Int){
        database.child("service_type").child("00"+id.toString()).removeValue()
    }

    fun deleteMaster(uid: String){
        database.child("masters").child(uid).removeValue()
        database.child("users").child(uid).child("role").setValue("client")
    }

    fun editMaster(uid: String, master: Master){
        database.child("masters").child(uid).setValue(master)
    }

    fun addRecord(uid: String, date:String, hour: Int, time:Int, serv: Int){
        var i: Int = 0
        val auth = Firebase.auth
        val current_user = auth.currentUser

        while(i < time){
            database.child("masters").child(uid).child("date").child(date).child((hour+i).toString()).setValue("False")
            i++
        }

        database.child("users").child(current_user?.uid!!).child("date").child(date).child("hour").setValue(hour)
        database.child("users").child(current_user?.uid!!).child("date").child(date).child("master").setValue(uid)
        database.child("users").child(current_user?.uid!!).child("date").child(date).child("service").setValue("00"+serv.toString())
    }



}