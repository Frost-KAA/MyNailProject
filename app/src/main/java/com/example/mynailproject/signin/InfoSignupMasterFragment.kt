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
import com.example.mynailproject.active.BasicActivity
import com.example.mynailproject.R
import com.example.mynailproject.additional.MastersListDialogFragment
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.Master
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

class InfoSignupMasterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var database: DatabaseReference = Firebase.database.reference
    lateinit var info: TextView
    var user: User? = null
    var serv_clicked_list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_signup_master, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity: BasicActivity = activity as BasicActivity
        activity.supportActionBar?.title = "Введите данные о себе"

        auth = Firebase.auth
        val current_user = auth.currentUser
        addUserEventListener(database, current_user?.uid!!)

        info = view.findViewById(R.id.signup_info)

        val button_serv : Button = view.findViewById(R.id.button_list_service)
        button_serv.setOnClickListener {
            val dialog = context?.let { it1 -> MastersListDialogFragment(it1, serv_clicked_list, this, false) }
            dialog!!.show()
        }

        val end_signup : Button = view.findViewById(R.id.button_end_signup)
        end_signup.setOnClickListener {
            val info_res = info.text.toString()
            if (info_res.length == 0 || serv_clicked_list.size == 0){
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.d("INFO", info_res)
                val mast = Master(current_user.uid, info_res, user)
                val db_call = DBCall()
                db_call.addNewMaster(current_user.uid, mast, serv_clicked_list)
                view.findNavController().navigate(R.id.action_global_myOfficeFragment)
            }

        }

    }

    fun getClicked(list: ArrayList<String>){
        serv_clicked_list = list
    }

    fun addUserEventListener(userReference: DatabaseReference, uid:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.child("users").child(uid).getValue<User>()
                val mast = dataSnapshot.child("masters").child(uid).getValue<Master>()
                if (mast != null){
                    info.text = mast.info
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }
}