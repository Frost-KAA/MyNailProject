package com.example.mynailproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.mynailproject.PriceFragmentDirections
import com.example.mynailproject.R
import com.example.mynailproject.Record.RecordServiceFragmentDirections
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.Master
import com.example.mynailproject.database.ServiceType

class MasterListDialogAdapter(val context: Context, val list: List<Master>, val clicked_list: ArrayList<String>): RecyclerView.Adapter<MasterListDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_masters_dialog, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        var isClicked = false
        for (uid in clicked_list){
            if (currentItem.uid == uid){
                isClicked = true
            }
        }
        if (isClicked) holder.layout.setBackgroundResource(R.color.shrine_pink_200)
        else holder.layout.setBackgroundResource(R.color.white)

        val str: String = currentItem.user?.surname+" "+currentItem.user?.name?.substring(0,1)+"."+currentItem.user?.pathronim?.substring(0,1)+"."
        holder.textView.text = str

        holder.layout.setOnClickListener {
            if (!isClicked) {
                clicked_list.add(currentItem.uid!!)
            }
            else {
                clicked_list.remove(currentItem.uid)
            }
            notifyDataSetChanged()
        }
    }

    fun getClickedList(): ArrayList<String>{
        return clicked_list
    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val layout: CardView = itemView.findViewById(R.id.layout)
    }


}