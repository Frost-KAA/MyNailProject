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

class RecordPriceAdapter(val context: Context, val list: List<ServiceType>): RecyclerView.Adapter<RecordPriceAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_price, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        viewBinderHelper.setOpenOnlyOne(true)

        viewBinderHelper.bind(holder.layout, currentItem.name)
        viewBinderHelper.closeLayout(currentItem.name)
        holder.detail_layout.visibility = View.GONE
        holder.edit.visibility = View.GONE
        holder.delete.visibility = View.GONE
        holder.detail_layout.visibility = View.GONE
        holder.name.text = currentItem.name
        holder.price.text = currentItem.price.toString()

        holder.info.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.action_recordServiceFragment_to_priceFragment)
        }

        holder.main_layout.setOnClickListener {
            val action = RecordServiceFragmentDirections.actionRecordServiceFragmentToRecordStaffFragment("00"+currentItem.id.toString(), currentItem.time!!, currentItem.id!!)
            holder.itemView.findNavController().navigate(action)
        }
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.date_name)
        val price: TextView = itemView.findViewById(R.id.time_name)
        val edit: ImageView = itemView.findViewById(R.id.img_edit)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val info: ImageView = itemView.findViewById(R.id.img_info)
        val detail_layout: ConstraintLayout = itemView.findViewById(R.id.layout_details)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val main_layout: ConstraintLayout = itemView.findViewById(R.id.layout)
    }


}