package com.example.mynailproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*


class BookingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = view.findViewById<CalendarView>(R.id.calendarView)
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        var date: String = sdf.format(Date(calendar.date))
        calendar.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            date = dayOfMonth.toString() + "."+ (month+1).toString() + "." + year.toString()
        })

        val save = view.findViewById<Button>(R.id.save)
        save.setOnClickListener {
            val date_view = view.findViewById<TextView>(R.id.date)
            date_view.text = "Дата записи: " + date
        }
    }
}