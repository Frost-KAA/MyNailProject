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
    val auth = Firebase.auth
    val current_user = auth.currentUser

    object CurrentRole{
        private var current_role : String? = null
        fun setRole(){
            val t = Thread {
                val auth = Firebase.auth
                val current_user = auth.currentUser
                val database: DatabaseReference = Firebase.database.reference
                if (current_user != null){
                    val userListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            current_role = dataSnapshot.child("users").child(current_user.uid)
                                .getValue<User>()?.role
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            current_role = null
                            Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
                        }
                    }
                    database.addValueEventListener(userListener)
                }
            }
            t.start()
            t.join()
        }

        fun getRole(): String?{
            return current_role
        }
    }

    fun addNewUser(uid: String, user: User){
        database.child("users").child(uid).setValue(user)
        if (user.role=="master"){
            val m = Master(uid, null, user)
            database.child("masters").child(uid).setValue(m)
        }
    }

    fun addNewServType(id: Int, ser: ServiceType, list: ArrayList<String>){
        database.child("service_type").child("00"+id.toString()).setValue(ser)
        for (item in list){
            database.child("service_type").child("00"+id.toString()).child("masters").child(item).setValue(item)
        }
    }

    fun addNewMaster(id: String, mast: Master, list: ArrayList<String>){
        database.child("masters").child(id).setValue(mast)
        for (item in list){
            database.child("service_type").child(item).child("masters").child(mast.uid!!).setValue(mast.uid)
        }
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
        while(i < time){
            database.child("masters").child(uid).child("date").child(date).child((hour+i).toString()).setValue(current_user?.uid)
            i++
        }

        val d = ServiceDate(hour, "00"+serv.toString(), uid, date, false)
        database.child("users").child(current_user?.uid!!).child("date").child(date).setValue(d)
    }


    fun deleteServDate(date: String, master_uid: String, hour: Int, time: Int){
        database.child("users").child(current_user?.uid!!).child("date").child(date).removeValue()
        var t = 0
        while(t < time){
            database.child("masters").child(master_uid).child("date").child(date).child((hour+t).toString()).setValue("Tue")
            t++
        }

    }

    fun addWorkTime(startDate: Long, endDate: Long, startTime: Int, endTime: Int){
        var date: Long = startDate
        while(date <= endDate){
            var time: Int = startTime
            while(time < endTime){
                database.child("masters").child(current_user?.uid!!).child("date").child(date.toString()).child(time.toString()).setValue("True")
                time ++
            }
            date += 24*60*60*1000

        }
    }

    fun isNotificated(date: String){
        database.child("users").child(current_user?.uid!!).child("date").child(date).child("notificat").setValue(true)
    }




}