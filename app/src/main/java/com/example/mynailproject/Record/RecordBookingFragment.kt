package com.example.mynailproject.Record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.R
import com.example.mynailproject.adapter.TimeAdapter
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.Time
import com.google.android.material.datepicker.MaterialDatePicker
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
    val list = ArrayList<Time>()
    var id:String? = null
    var time: Int? = null
    var serv: Int? = null
    //lateinit var date: String
    var date_long: Long?= null
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getRecordId()
        getRecordTime()
        getRecordServ()
        //addUserEventListener(database)
        return inflater.inflate(R.layout.fragment_record_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val calendar = view.findViewById<CalendarView>(R.id.calendarView)
        //Log.d("TIME", calendar.date.toInt().toString())

        val ps = view.findViewById<TextView>(R.id.ps)
        var text = "Длительность услуги "+time+" часа"
        if (time == 1) text = "Длительность услуги "+time+" час"
        ps.text = text

        /*val sdf = SimpleDateFormat("dd,MM,yyyy")
        date = sdf.format(Date(calendar.date))
        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            var d: String = dayOfMonth.toString()
            if (d.length < 2) d = "0$d"
            date = d + "," + (month + 1).toString() + "," + year.toString()
            addUserEventListener(database)
        })*/

        val button_calendar: ImageButton = view.findViewById(R.id.button_record_calendar)
        val data_view: TextView = view.findViewById(R.id.data_record)
        button_calendar.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Выберите дату")
                .build()
            datePicker.show(requireFragmentManager(), "date_picker")

            datePicker.addOnPositiveButtonClickListener { datePicked->
                date_long = datePicked
                data_view.text = convertLongToDate(date_long!!)
                addUserEventListener(database)
            }
        }

        //подключение адаптера
        recycler =  view.findViewById(R.id.recycler_view_masters)
        recycler.adapter = this.context?.let { TimeAdapter(it, list, time) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)

        val save = view.findViewById<Button>(R.id.save)
        save.setOnClickListener {
            val color_id = (recycler.adapter as TimeAdapter).getColorPos()
            if (color_id != -1){
                val db_call = DBCall()
                db_call.addRecord(id!!, date_long!!.toString(), color_id, time!!, serv!!)
            }
        }
    }

    private fun convertLongToDate(time: Long): String{
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun getRecordId(): String? {
        val args = arguments?.let { RecordBookingFragmentArgs.fromBundle(it) }
        if (!(args?.id == null)){
            id = args.id
        }
        return id
    }

    fun getRecordTime(): Int? {
        val args = arguments?.let { RecordStaffFragmentArgs.fromBundle(it) }
        if (!(args?.time == null)){
            time = args.time
        }
        return time
    }

    fun getRecordServ(): Int? {
        val args = arguments?.let { RecordStaffFragmentArgs.fromBundle(it) }
        if (!(args?.serv == null)){
            serv = args.serv
        }
        return serv
    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (list.size > 0) list.clear()
                for (ds in dataSnapshot.child("masters").child(id!!).child("date").child(date_long!!.toString()).children){
                    val is_free: String? = ds.getValue<String>()
                    var free: Boolean?
                    if (is_free == "True") free = true
                    else free = false
                    val hour: Int? = ds.key?.toInt()
                    val time = Time(hour, free)
                    if (is_free != null) {
                        list.add(time)
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