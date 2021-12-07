package com.example.mynailproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.adapter.DateAdapter
import com.example.mynailproject.adapter.StaffAdapter
import com.example.mynailproject.database.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

    lateinit var recycler_now : RecyclerView
    lateinit var recycler_history : RecyclerView
    val list_now = ArrayList<ServiceDate>()
    val list_history = ArrayList<ServiceDate>()

    lateinit var surname : TextView
    lateinit var name : TextView
    var current_user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_office, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        current_user = auth.currentUser
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
        val edit: ImageButton = view.findViewById(R.id.button_edit_user)
        edit.setOnClickListener {
            view.findNavController().navigate(R.id.action_global_infoSignupFragment)
        }

        val work: Button = view.findViewById(R.id.button_to_work)
        work.setOnClickListener {
            view.findNavController().navigate(R.id.action_myOfficeFragment_to_bookingFragment)
        }

        //подключение адаптера
        recycler_now =  view.findViewById(R.id.recycler_view_now)
        recycler_now.adapter = this.context?.let { DateAdapter(it, list_now) }
        recycler_now.layoutManager = LinearLayoutManager(this.context)
        recycler_now.setHasFixedSize(true)

        //подключение адаптера
        recycler_history =  view.findViewById(R.id.recycler_view_history)
        recycler_history.adapter = this.context?.let { DateAdapter(it, list_history) }
        recycler_history.layoutManager = LinearLayoutManager(this.context)
        recycler_history.setHasFixedSize(true)

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

                //val sdf = SimpleDateFormat("yyyy,MM,dd")
                //val currentDate = sdf.format(Date())
                val now = Date().time
                if (list_now.size > 0) list_now.clear()
                if (list_history.size > 0) list_history.clear()
                for (ds in dataSnapshot.child("users").child(current_user?.uid!!).child("date").children){
                    val d: ServiceDate? = ds.getValue<ServiceDate>()
                    if (d != null) {
                        if (d.date!! >= now.toString()) list_now.add(d)
                        else list_history.add(d)
                        //val d_format = d.date!!.substring(6)+d.date!!.substring(2,6)+d.date!!.substring(0,2)
                        //Log.d("DATEE", d_format)
                        //if (d_format >= currentDate) list_now.add(d)
                        //else list_history.add(d)
                    }
                }
                recycler_now.adapter?.notifyDataSetChanged()
                recycler_history.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }
}