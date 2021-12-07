package com.example.mynailproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.mynailproject.PriceFragmentDirections
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordServiceFragmentDirections
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.ServiceDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter(val context: Context, val list: List<ServiceDate>): RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_date, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        viewBinderHelper.setOpenOnlyOne(true)

        viewBinderHelper.bind(holder.layout, currentItem.date)
        viewBinderHelper.closeLayout(currentItem.date)
        holder.date_name.text = convertLongToDate(currentItem.date!!.toLong())
        holder.time_name.text = currentItem.hour.toString()+":00"

        // удаление записи
        holder.delete.setOnClickListener {
            val db_call = DBCall()
            db_call.deleteServDate( currentItem.date)
        }

        /*holder.name.setOnClickListener {
            Log.d("CLICK", currentItem.id.toString())
            val action = RecordServiceFragmentDirections.actionRecordServiceFragmentToRecordStaffFragment("00"+currentItem.id.toString())
            holder.itemView.findNavController().navigate(action)
        }*/

        holder.main_layout.setOnClickListener {
            //val action = RecordServiceFragmentDirections.actionRecordServiceFragmentToRecordStaffFragment("00"+currentItem.id.toString(), currentItem.time!!, currentItem.id!!)
            //holder.itemView.findNavController().navigate(action)
        }
    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date_name: TextView = itemView.findViewById(R.id.date_name)
        val time_name: TextView = itemView.findViewById(R.id.time_name)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val main_layout: ConstraintLayout = itemView.findViewById(R.id.layout)
    }

    private fun convertLongToDate(time: Long): String{
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
    }

}