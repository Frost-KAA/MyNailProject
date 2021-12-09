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
import com.example.mynailproject.active.PriceFragmentDirections
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordServiceFragmentDirections
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.ServiceType

class PriceAdapter(val context: Context, val list: List<ServiceType>): RecyclerView.Adapter<PriceAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()
    var expanded : Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_price, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        val role = DBCall.CurrentRole.getRole()
        if (role == null || role == "client"){
            holder.delete.visibility = View.GONE
            holder.edit.visibility = View.GONE
        }
        holder.img_info.visibility = View.GONE

        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(holder.layout, currentItem.name)
        viewBinderHelper.closeLayout(currentItem.name)
        holder.name.text = currentItem.name
        holder.price.text = currentItem.price.toString()
        var time_text = currentItem.time.toString()+" часов"
        if (currentItem.time==1)  time_text = currentItem.time.toString()+" час"
        holder.time.text = time_text
        holder.info.text = "Описание: " + currentItem.info

        if (expanded == position) holder.detail_layout.visibility = View.VISIBLE
        else holder.detail_layout.visibility = View.GONE

        holder.main_layout.setOnClickListener {
            if (expanded == position) expanded = -1
            else expanded = position
            notifyDataSetChanged()
        }

        // редактирование услуги
        holder.edit.setOnClickListener {
            val action = PriceFragmentDirections.actionPriceFragmentToAddServiceFragment(currentItem.id!!, false)
            holder.itemView.findNavController().navigate(action)
        }

        // удаление услуги
        holder.delete.setOnClickListener {
            expanded = -1
            val db_call = DBCall()
            db_call.deleteServType(currentItem.id!!)
        }

    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.date_name)
        val price: TextView = itemView.findViewById(R.id.time_name)
        val edit: ImageView = itemView.findViewById(R.id.img_edit)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val img_info: ImageView = itemView.findViewById(R.id.img_info)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val main_layout: ConstraintLayout = itemView.findViewById(R.id.layout)
        val detail_layout: ConstraintLayout = itemView.findViewById(R.id.layout_details)
        val time: TextView = itemView.findViewById(R.id.details_time)
        val info: TextView = itemView.findViewById(R.id.details_info)
    }


}