package com.example.mynailproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordStaffFragmentDirections
import com.example.mynailproject.database.Master

class RecordStaffAdapter(val context: Context, val list: List<Master>, val time: Int?, val serv: Int?): RecyclerView.Adapter<RecordStaffAdapter.ViewHolder>() {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_staff, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        viewBinderHelper.setOpenOnlyOne(true)

        viewBinderHelper.bind(holder.layout, currentItem.user?.name)
        viewBinderHelper.closeLayout(currentItem.user?.name)
        holder.delete.visibility = View.GONE
        holder.detail_layout.visibility = View.GONE

        holder.name.text = currentItem.user?.name
        holder.pathronim.text = currentItem.user?.pathronim
        holder.surname.text = currentItem.user?.surname

        holder.img_info.setOnClickListener {
            holder.itemView.findNavController().navigate(R.id.action_recordStaffFragment_to_staffFragment)
        }

        holder.lin_layout.setOnClickListener {
            val action = RecordStaffFragmentDirections.actionRecordStaffFragmentToRecordBookingFragment(currentItem.uid, time!!, serv!!)
            holder.itemView.findNavController().navigate(action)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.u_name)
        val pathronim: TextView = itemView.findViewById(R.id.u_pathronim)
        val surname: TextView = itemView.findViewById(R.id.u_surname)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val img_info: ImageView = itemView.findViewById(R.id.img_info_master)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val lin_layout: LinearLayout = itemView.findViewById(R.id.layout_staff_card)
        val detail_layout: ConstraintLayout = itemView.findViewById(R.id.layout_details)
    }


}