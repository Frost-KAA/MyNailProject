package com.example.mynailproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.Record.RecordStaffFragmentDirections
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
    val list_now = ArrayList<ServiceDate>()
    val list_history = ArrayList<ServiceDate>()

    lateinit var surname : TextView
    lateinit var name : TextView
    lateinit var pathronim: TextView
    var current_user: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_office, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity: BasicActivity = activity as BasicActivity
        activity.supportActionBar?.title = "Личный кабинет"

        auth = Firebase.auth
        current_user = auth.currentUser
        addUserEventListener(database, current_user?.uid!!)

        surname = view.findViewById(R.id.u_surname)
        name = view.findViewById(R.id.u_name)
        pathronim = view.findViewById(R.id.u_pathronim)

        val master_crown: LinearLayout = view.findViewById(R.id.layout_master)
        val see_work: Button = view.findViewById(R.id.button_work)
        val to_client: Button = view.findViewById(R.id.button_to_client)

        val role = DBCall.CurrentRole.getRole()
        if (role == null || role == "client"){
            to_client.visibility = View.GONE
            master_crown.visibility = View.GONE
            see_work.visibility = View.GONE
        }

        // Выход из аккаунта
        val exit: ImageView = view.findViewById(R.id.img_exit)
        exit.setOnClickListener {
            Firebase.auth.signOut()
            view.findNavController().navigate(R.id.action_global_loginFragment)
        }

        // Редактирование аккаунта
        val edit: ImageView = view.findViewById(R.id.img_edit)
        edit.setOnClickListener {
            view.findNavController().navigate(R.id.action_global_infoSignupFragment)
        }

        // просомтр времени работы для мастера
        see_work.setOnClickListener{
            val action =MyOfficeFragmentDirections.actionMyOfficeFragmentToRecordBookingFragment(current_user?.uid, 0, 0)
            view.findNavController().navigate(action)
        }

        //просмотр истории записей
        val history: ImageButton = view.findViewById(R.id.button_record_history)
        history.setOnClickListener {
            val action =MyOfficeFragmentDirections.actionMyOfficeFragmentToHistoryFragment(false, true)
            view.findNavController().navigate(action)
        }


        to_client.setOnClickListener {
            val action =MyOfficeFragmentDirections.actionMyOfficeFragmentToHistoryFragment(true, false)
            view.findNavController().navigate(action)
        }

        //подключение адаптера
        recycler_now =  view.findViewById(R.id.recycler_view_now)
        recycler_now.adapter = this.context?.let { DateAdapter(it, list_now, false, this) }
        recycler_now.layoutManager = LinearLayoutManager(this.context)
        recycler_now.setHasFixedSize(true)
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
                pathronim.text = user?.pathronim

                val now = Date().time
                if (list_now.size > 0) list_now.clear()
                if (list_history.size > 0) list_history.clear()
                for (ds in dataSnapshot.child("users").child(current_user?.uid!!).child("date").children){
                    val d: ServiceDate? = ds.getValue<ServiceDate>()
                    if (d != null) {
                        if (d.date!! >= now.toString()) list_now.add(d)
                        else list_history.add(d)
                    }
                }
                recycler_now.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }
}