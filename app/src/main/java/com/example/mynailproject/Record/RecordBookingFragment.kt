package com.example.mynailproject.Record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.R
import com.example.mynailproject.adapter.TimeAdapter
import com.example.mynailproject.database.ServiceType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class RecordBookingFragment : Fragment() {

    lateinit var recycler : RecyclerView
    val list = ArrayList<ServiceType>()
    var id:String? = null
    lateinit var date: String
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getRecordId()
        addUserEventListener(database)
        return inflater.inflate(R.layout.fragment_record_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = view.findViewById<CalendarView>(R.id.calendarView)
        //Log.d("TIME", calendar.date.toInt().toString())

        val sdf = SimpleDateFormat("dd,MM,yyyy")
        date = sdf.format(Date(calendar.date))
        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            date = dayOfMonth.toString() + "," + (month + 1).toString() + "," + year.toString()
            addUserEventListener(database)
        })

        //подключение адаптера
        recycler =  view.findViewById(R.id.recycler_view)
        recycler.adapter = this.context?.let { TimeAdapter(it, list) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)

        val save = view.findViewById<Button>(R.id.save)
        save.setOnClickListener {
            val date_view = view.findViewById<TextView>(R.id.date)
            date_view.text = "Дата записи: " + date
        }
    }

    fun getRecordId(): String? {
        val args = arguments?.let { RecordBookingFragmentArgs.fromBundle(it) }
        if (!(args?.id == null)){
            id = args.id
        }
        return id
    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (list.size > 0) list.clear()
                for (ds in dataSnapshot.child("masters").child(id!!).child(date).children){
                    val is_free: String? = ds.getValue<String>()
                    val time: String? = ds.key
                    if (is_free != null) {
                        //Log.d("SERV", serv.name.toString())
                        //list.add()
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