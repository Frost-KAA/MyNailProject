package com.example.mynailproject.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mynailproject.R
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


class InfoSignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var database: DatabaseReference = Firebase.database.reference

    lateinit var surname : TextView
    lateinit var name : TextView
    lateinit var pathronim : TextView
    lateinit var phone : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val current_user = auth.currentUser

        surname = view.findViewById(R.id.surname)
        name = view.findViewById(R.id.name)
        pathronim = view.findViewById(R.id.pathronim)
        phone = view.findViewById(R.id.phone)

        addUserEventListener(database, current_user?.uid!!)

        val end_signup : Button = view.findViewById(R.id.button_end_signup)
        end_signup.setOnClickListener {
            val surname_text = surname.text.toString()
            val name_text = name.text.toString()
            val pathronim_text = pathronim.text.toString()
            val phone_text = phone.text.toString()
            val email = current_user?.email
            val uid = current_user?.uid
            val role: String = "master"

            if (surname_text == "" || name_text == "" || pathronim_text == "" || phone_text == ""){
                Toast.makeText(
                    this.context, "Не все данные введены",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{

                if (phone_text.length != 11){
                    Toast.makeText(
                        this.context, "Некорректный номер телефона",
                        Toast.LENGTH_SHORT
                    ).show()

                } else{

                    val user = User(surname_text, name_text, pathronim_text, phone_text, email!!, role)
                    val db_call = DBCall()
                    if (uid != null) {
                        db_call.addNewUser(uid, user)
                    }
                    view.findNavController().navigate(R.id.action_global_myOfficeFragment)
                }

            }


        }


    }

    fun addUserEventListener(userReference: DatabaseReference, uid:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.child("users").child(uid).getValue<User>()
                if (user != null){
                    surname.text = user.surname
                    name.text = user.name
                    pathronim.text = user.pathronim
                    phone.text = user.phone
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }
}