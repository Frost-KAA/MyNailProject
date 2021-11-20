package com.example.mynailproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.mynailproject.PriceFragmentDirections
import com.example.mynailproject.R
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.ServiceType
import com.example.mynailproject.database.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class PriceAdapter(val context: Context, val list: List<ServiceType>): RecyclerView.Adapter<PriceAdapter.ViewHolder>() {

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
        holder.name.text = currentItem.name
        holder.price.text = currentItem.price.toString()

        // редактирование услуги
        holder.edit.setOnClickListener {
            val action = PriceFragmentDirections.actionPriceFragmentToAddServiceFragment(currentItem.id!!, list.size)
            holder.itemView.findNavController().navigate(action)
        }

        // удаление услуги
        holder.delete.setOnClickListener {
            val db_call = DBCall()
            db_call.deleteServType(currentItem.id!!)
        }
    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.serv_name)
        val price: TextView = itemView.findViewById(R.id.serv_price)
        val edit: ImageView = itemView.findViewById(R.id.img_edit)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
    }


}