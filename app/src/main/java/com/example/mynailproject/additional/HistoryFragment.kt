package com.example.mynailproject.additional

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.BasicActivity
import com.example.mynailproject.PriceFragmentDirections
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordBookingFragmentArgs
import com.example.mynailproject.adapter.DateAdapter
import com.example.mynailproject.adapter.PriceAdapter
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.ServiceDate
import com.example.mynailproject.database.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    lateinit var recycler : RecyclerView
    val list_history = ArrayList<ServiceDate>()
    var current_user: FirebaseUser? = null
    private lateinit var auth: FirebaseAuth
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity: BasicActivity = activity as BasicActivity
        activity.supportActionBar?.title = "История услуг"

        auth = Firebase.auth
        current_user = auth.currentUser
        addUserEventListener(database, current_user?.uid!!)

        //подключение адаптера
        recycler =  view.findViewById(R.id.recycler_view_masters)
        recycler.adapter = context?.let { DateAdapter(it, list_history) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)

    }


    fun addUserEventListener(userReference: DatabaseReference, uid:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val now = Date().time
                if (list_history.size > 0) list_history.clear()
                for (ds in dataSnapshot.child("users").child(current_user?.uid!!).child("date").children){
                    val d: ServiceDate? = ds.getValue<ServiceDate>()
                    if (d != null) {
                        if (d.date!! < now.toString()) list_history.add(d)
                    }
                }
                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}