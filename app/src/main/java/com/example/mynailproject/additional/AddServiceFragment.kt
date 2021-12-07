package com.example.mynailproject.additional

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.mynailproject.BasicActivity
import com.example.mynailproject.additional.AddServiceFragmentArgs
import com.example.mynailproject.R
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.ServiceType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AddServiceFragment : Fragment() {

    private var database: DatabaseReference = Firebase.database.reference

    lateinit var serv_name_view : TextView
    lateinit var price_view : TextView
    lateinit var info_view: TextView
    lateinit var time_view: TextView
    var masters_clicked_list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val num = getNumber()
        val isAdded = getIsAdd()

        val activity: BasicActivity = activity as BasicActivity
        if (isAdded) activity.supportActionBar?.title = "Добавление услуги"
        else activity.supportActionBar?.title = "Редактирование услуги"

        serv_name_view = view.findViewById(R.id.name_serv)
        price_view = view.findViewById(R.id.price)
        info_view = view.findViewById(R.id.info)
        time_view = view.findViewById(R.id.time)
        val button_masters: Button = view.findViewById(R.id.button_masters)

        addUserEventListener(database, "00$num")



        val save: Button = view.findViewById(R.id.button_save_serv)
        save.setOnClickListener {
            val serv_name = serv_name_view.text.toString()
            val price = price_view.text.toString()
            val time = time_view.text.toString()
            val info = info_view.text.toString()
            if (serv_name.length == 0 || price.length == 0 || time.length == 0 || info.length == 0 || masters_clicked_list.size == 0){
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
            else{
                val serv = ServiceType(num, serv_name, price.toInt(), time.toInt(), info)
                val db_call = DBCall()
                db_call.addNewServType(num, serv, masters_clicked_list)
                view.findNavController().navigate(R.id.action_addServiceFragment_to_priceFragment)
            }
        }

        button_masters.setOnClickListener {
            val dialog = context?.let { it1 -> MastersListDialogFragment(it1, masters_clicked_list, this) }
            dialog!!.show()
        }

    }

    fun getClicked(list: ArrayList<String>){
        masters_clicked_list = list
    }



    fun getNumber(): Int {
        var num = 0
        val args = arguments?.let { AddServiceFragmentArgs.fromBundle(it) }
        if (!(args?.number == null)){
            num = args.number
        }
        return num
    }

    fun getIsAdd(): Boolean {
        var f = false
        val args = arguments?.let { AddServiceFragmentArgs.fromBundle(it) }
        if (!(args?.isAdded == null)){
            f = args.isAdded
        }
        return f
    }



    fun addUserEventListener(userReference: DatabaseReference, id:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val serv = dataSnapshot.child("service_type").child(id).getValue<ServiceType>()
                serv_name_view.text = serv?.name
                price_view.text = serv?.price?.toString()
                time_view.text = serv?.time?.toString()
                info_view.text = serv?.info
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}