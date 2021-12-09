package com.example.mynailproject.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.mynailproject.R
import com.example.mynailproject.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DateAdapter(val context: Context, val list: List<ServiceDate>, val isMaster: Boolean, val fr: Fragment): RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    private var database: DatabaseReference = Firebase.database.reference
    private val viewBinderHelper = ViewBinderHelper()
    val data_list: ArrayList<ServiceDateCard> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card_date,
            parent,
            false
        )
        addUserEventListener(database)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]
        var current: ServiceDateCard? = null
        if (data_list.size!= 0) current = data_list[position]

        viewBinderHelper.setOpenOnlyOne(true)

        viewBinderHelper.bind(holder.layout, currentItem.date)
        viewBinderHelper.closeLayout(currentItem.date)

        if (isMaster) holder.textview.text = "Клиент:"

        holder.date_name.text = convertLongToDate(currentItem.date!!.toLong())
        holder.time_name.text = currentItem.hour.toString()+":00"

        holder.master.text = current?.master
        holder.service.text = current?.serv
        holder.price.text = current?.price.toString()+" рублей"
        var time_text = current?.time.toString()+" часов"
        if (current?.time==1)  time_text = current.time.toString()+" час"
        holder.time.text = time_text

        if (current?.expanded == true) holder.detail_layout.visibility=View.VISIBLE
        else holder.detail_layout.visibility = View.GONE

        holder.main_layout.setOnClickListener {
            if (current?.expanded == true) current.expanded=false
            else current?.expanded=true
            notifyItemChanged(position)
        }

        // удаление записи
        holder.delete.setOnClickListener {
            val db_call = DBCall()
            db_call.deleteServDate(
                currentItem.date,
                currentItem.master_uid!!,
                currentItem.hour!!,
                current?.time!!
            )
        }

        //переход в вотсап
        holder.whatsapp.setOnClickListener {
            Log.d("Whats", "whats")
            val num: String = current?.phone!!
            val url = "https://api.whatsapp.com/send?phone=$num"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            fr.activity?.startActivity(i)
        }

    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val date_name: TextView = itemView.findViewById(R.id.date_name)
        val time_name: TextView = itemView.findViewById(R.id.time_name)
        val delete: ImageView = itemView.findViewById(R.id.img_delete)
        val layout : SwipeRevealLayout = itemView.findViewById(R.id.swipe_layout)
        val main_layout: ConstraintLayout = itemView.findViewById(R.id.layout)
        val detail_layout: ConstraintLayout = itemView.findViewById(R.id.layout_details)
        val master: TextView = itemView.findViewById(R.id.details_info)
        val time: TextView = itemView.findViewById(R.id.details_time)
        val service: TextView = itemView.findViewById(R.id.details_service)
        val price: TextView = itemView.findViewById(R.id.details_price)
        val textview: TextView = itemView.findViewById(R.id.details_info_text)
        val whatsapp: Button = itemView.findViewById(R.id.button_to_whatsapp)


    }

    private fun convertLongToDate(time_long: Long): String{
        val date = Date(time_long)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return format.format(date)
    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (data_list.size != 0)data_list.clear()
                for (item in list){
                    //Log.d("LIISt", item.toString())
                    val serv = dataSnapshot.child("service_type").child(item.serv_id!!).getValue<ServiceType>()
                    val mast = dataSnapshot.child("users").child(item.master_uid!!).getValue<User>()
                    val detail = ServiceDateCard(
                        item.hour,
                        item.date,
                        false,
                        serv?.name,
                        serv?.price,
                        serv?.time,
                        mast?.surname + " " + mast?.name?.substring(
                            0,
                            1
                        ) + "." + mast?.pathronim?.substring(0, 1) + ".",
                        mast?.phone
                    )
                    data_list.add(detail)
                }
                notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}