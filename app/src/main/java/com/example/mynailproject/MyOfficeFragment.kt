package com.example.mynailproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class MyOfficeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var database: DatabaseReference = Firebase.database.reference

    lateinit var surname : TextView
    lateinit var name : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_office, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val current_user = auth.currentUser
        addUserEventListener(database, current_user?.uid!!)

        surname = view.findViewById(R.id.u_surname)
        name = view.findViewById(R.id.u_name)

        // Выход из аккаунта
        val exit: Button = view.findViewById(R.id.button_exit)
        exit.setOnClickListener {
            Firebase.auth.signOut()
            view.findNavController().navigate(R.id.action_global_loginFragment)
        }

        // Редактирование аккаунта
        val edit: Button = view.findViewById(R.id.button_edit_user)
        edit.setOnClickListener {
            view.findNavController().navigate(R.id.action_global_infoSignupFragment)
        }


        val info: TextView = view.findViewById(R.id.text_info)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        info.text = currentUser?.uid.toString()
    }

    fun addUserEventListener(userReference: DatabaseReference, uid:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*var user : User? = null
                val t = Thread{
                    user = dataSnapshot.child("users").child(uid).getValue<User>()
                }
                t.start()
                t.join()*/
                val user = dataSnapshot.child("users").child(uid).getValue<User>()
                surname.text = user?.surname
                name.text = user?.name
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }
}