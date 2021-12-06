package com.example.mynailproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordServiceFragmentDirections
import com.example.mynailproject.database.Time

class TimeAdapter(val context: Context, val list: List<Time>, val time: Int?): RecyclerView.Adapter<TimeAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()
    private var color_pos: Int = -1
    private var color_id: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_time, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        viewBinderHelper.setOpenOnlyOne(true)
        val str : String = currentItem.hour.toString()+":00"
        holder.name.text = str

        if (!currentItem.free!!){
            holder.main_layout.setBackgroundResource(R.color.shrine_pink_800)
        }
        else{
            if (color_pos == position){
                color_id = currentItem.hour!!
                holder.main_layout.setBackgroundResource(R.color.shrine_pink_200)
            }
            else{
                holder.main_layout.setBackgroundResource(R.color.shrine_pink_50)
            }
        }

        holder.main_layout.setOnClickListener {
            if (currentItem.free) {
                if (checkTime(position)) changeColorOnPosition(position)
                else Toast.makeText(context, "Продолжительность свободного времени меньше времени услуги", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Это время уже занято", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changeColorOnPosition(ex_num: Int){
        color_pos = ex_num
        notifyDataSetChanged()
    }

    fun getColorPos(): Int{
        return color_id
    }

    fun checkTime(pos: Int): Boolean{
        var i:Int = 0
        while (i < time!!){
            if (pos+i >= list.size || list[pos+i].free==false){
                return false
            }
            i++
        }
        return true
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.hour_name)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val main_layout: ConstraintLayout = itemView.findViewById(R.id.layout)
    }
}