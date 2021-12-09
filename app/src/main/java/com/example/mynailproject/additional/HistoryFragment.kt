package com.example.mynailproject.additional

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.BasicActivity
import com.example.mynailproject.MyOfficeFragmentDirections
import com.example.mynailproject.PriceFragmentDirections
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordBookingFragmentArgs
import com.example.mynailproject.Record.RecordStaffFragmentArgs
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

    var isMaster = false
    var isHistory = false

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
        isMaster = getMaster()
        isHistory = getHistory()
        val button_hist : Button = view.findViewById(R.id.button_hist)

        if (isMaster && !isHistory) {
            activity.supportActionBar?.title = "Записи клиентов"
            button_hist.visibility = View.VISIBLE
        }
        else {
            activity.supportActionBar?.title = "История услуг"
            button_hist.visibility = View.GONE
        }

        button_hist.setOnClickListener {
            val action = MyOfficeFragmentDirections.actionMyOfficeFragmentToHistoryFragment(true, true)
            view.findNavController().navigate(action)
        }

        auth = Firebase.auth
        current_user = auth.currentUser
        addUserEventListener(database, current_user?.uid!!)

        //подключение адаптера
        recycler =  view.findViewById(R.id.recycler_view_masters)
        recycler.adapter = context?.let { DateAdapter(it, list_history, isMaster, this) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)

    }

    fun getMaster(): Boolean {
        var m = false
        val args = arguments?.let { HistoryFragmentArgs.fromBundle(it) }
        if (!(args?.isMaster == null)){
            m = args.isMaster
        }
        return m
    }

    fun getHistory(): Boolean {
        var m = false
        val args = arguments?.let { HistoryFragmentArgs.fromBundle(it) }
        if (!(args?.isHistory == null)){
            m = args.isHistory
        }
        return m
    }


    fun addUserEventListener(userReference: DatabaseReference, uid:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val now = Date().time

                if (isMaster == false){
                    if (list_history.size > 0) list_history.clear()
                    for (ds in dataSnapshot.child("users").child(current_user?.uid!!).child("date").children){
                        val d: ServiceDate? = ds.getValue<ServiceDate>()
                        if (d != null) {
                            if (d.date!! < now.toString()) list_history.add(d)
                        }
                    }
                }
                else{
                    if (list_history.size > 0) list_history.clear()
                    //for (dsss in dataSnapshot.child("masters").child(current_user?.uid!!).child("date").children) {
                     //   for (dss in dsss.children) {
                     //       val u_uid: String? = dss.getValue<String>()
                     //       if (u_uid == "True") continue
                        for (dss in dataSnapshot.child("users").children){
                            val k: String? = dss.key
                            val u: User? = dss.getValue<User>()
                            for (ds in dss.child("date").children) {
                                val d: ServiceDate? = ds.getValue<ServiceDate>()
                                if (d != null && d.master_uid == current_user?.uid) {
                                    if ((!isHistory && d.date!! > now.toString()) || (isHistory && d.date!! <= now.toString())) {
                                        val new_d = ServiceDate(d.hour, d.serv_id, k!!, d.date)
                                        Log.d("HISTORY 1 ", list_history.size.toString())
                                        list_history.add(new_d)
                                    }

                                }
                            }
                        }
                    //}
                    Log.d("HISTORY", list_history.size.toString())
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