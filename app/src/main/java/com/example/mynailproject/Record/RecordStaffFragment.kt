package com.example.mynailproject.Record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.BasicActivity
import com.example.mynailproject.R
import com.example.mynailproject.adapter.RecordStaffAdapter
import com.example.mynailproject.adapter.StaffAdapter
import com.example.mynailproject.additional.AddServiceFragmentArgs
import com.example.mynailproject.database.Master
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class RecordStaffFragment : Fragment() {

    lateinit var recycler : RecyclerView
    var list = ArrayList<Master>()
    var id: String? = null
    var time: Int? = null
    var serv: Int? = null
    private var database: DatabaseReference = Firebase.database.reference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getRecordId()
        getRecordTime()
        getRecordServ()
        addUserEventListener(database)
        return inflater.inflate(R.layout.fragment_record_staff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity: BasicActivity = activity as BasicActivity
        activity.supportActionBar?.title = "Выберите специалиста"

        //подключение адаптера
        recycler =  view.findViewById(R.id.recycler_view_staff)
        recycler.adapter = this.context?.let { RecordStaffAdapter(it, list, time, serv) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)
    }

    fun getRecordId(): String? {
        val args = arguments?.let { RecordStaffFragmentArgs.fromBundle(it) }
        if (!(args?.id == null)){
            id = args.id
        }
        return id
    }

    fun getRecordServ(): Int? {
        val args = arguments?.let { RecordStaffFragmentArgs.fromBundle(it) }
        if (!(args?.serv == null)){
            serv = args.serv
        }
        return serv
    }

    fun getRecordTime(): Int? {
        val args = arguments?.let { RecordStaffFragmentArgs.fromBundle(it) }
        if (!(args?.time == null)){
            time = args.time
        }
        return time
    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (list.size > 0) list.clear()
                for (ds in id?.let { dataSnapshot.child("service_type").child(it).child("masters").children }!!){
                    val master_uid: String? = ds.getValue<String?>()
                    val master = dataSnapshot.child("masters").child(master_uid!!).getValue<Master>()
                    if (master != null) {
                        list.add(master)
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