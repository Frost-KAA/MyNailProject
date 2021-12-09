package com.example.mynailproject.active

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.mynailproject.R
import com.example.mynailproject.database.DBCall
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*


class BookingFragment : Fragment() {

    lateinit var start_view : TextView
    lateinit var end_view : TextView
    lateinit var start_time_view : TextView
    lateinit var end_time_view : TextView
    var startDate : Long? = null
    var endDate : Long? = null
    var startTime : Int? = null
    var endTime : Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start_view = view.findViewById(R.id.start_date)
        end_view = view.findViewById(R.id.end_date)
        start_time_view = view.findViewById(R.id.start_time)
        end_time_view = view.findViewById(R.id.end_time)

        start_view.text = ""
        end_view.text = ""
        start_time_view.text = ""
        end_time_view.text = ""

        val button_calendar: ImageButton = view.findViewById(R.id.button_calendar)
        button_calendar.setOnClickListener {
            showDateRangePicker()
        }

        val button_time: ImageButton = view.findViewById(R.id.button_time)
        button_time.setOnClickListener {
            showTimePicker(start_time_view)
        }

        val button_time2: ImageButton = view.findViewById(R.id.button_time2)
        button_time2.setOnClickListener {
            showTimePicker(end_time_view)
        }

        val save: Button = view.findViewById(R.id.save)
        save.setOnClickListener {
            val db_call = DBCall()
            if (startDate==null || endDate==null || startTime == null || endTime == null){
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
            /*Log.d("DATTE", startDate.toString())
            Log.d("DATTE", endDate.toString())
            Log.d("DATTE", (startDate?.plus(2*24*60*60*1000)).toString())*/
            else db_call.addWorkTime(startDate!!, endDate!!, startTime!!, endTime!!)
        }

    }

    private fun showDateRangePicker(){
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Выберите дату")
            .build()
        dateRangePicker.show(requireFragmentManager(), "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { datePicked->
            startDate = datePicked.first
            endDate = datePicked.second

            start_view.text = convertLongToDate(startDate!!)
            end_view.text = convertLongToDate(endDate!!)
        }
    }

    private fun showTimePicker(text_view: TextView){
        val timePicker = TimePickerDialog(
            context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (text_view == start_time_view) startTime = hourOfDay
                else endTime = hourOfDay
                text_view.text = hourOfDay.toString()+":00"
            },
            0, 0, true
        )
        timePicker.show()

    }

    private fun convertLongToDate(time: Long): String{
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
    }
}