package com.example.mynailproject.additional

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
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

    var num = 0
    private var database: DatabaseReference = Firebase.database.reference

    lateinit var serv_name_view : TextView
    lateinit var price_view : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var num = getNumber()
        val size = getSize()
        Log.d("SERV", num.toString())
        Log.d("SERV", size.toString())

        serv_name_view = view.findViewById(R.id.name_serv)
        price_view = view.findViewById(R.id.price)

        if (num == 0){
            num = size + 1
        }
        addUserEventListener(database, "00$num")



        val save: Button = view.findViewById(R.id.button_save_serv)
        save.setOnClickListener {
            val ser_name = serv_name_view.text.toString()
            val price = price_view.text.toString().toInt()
            val ser = ServiceType(num, ser_name, price)
            val db_call = DBCall()
            db_call.addNewServType(num, ser)
            view.findNavController().navigate(R.id.action_global_priceFragment)
        }
    }

    fun getNumber(): Int {
        val args = arguments?.let { AddServiceFragmentArgs.fromBundle(it) }
        if (!(args?.number == null)){
            num = args.number
        }
        return num
    }

    fun getSize(): Int {
        val args = arguments?.let { AddServiceFragmentArgs.fromBundle(it) }
        if (!(args?.size == null)){
            num = args.size
        }
        return num
    }

    fun addUserEventListener(userReference: DatabaseReference, id:String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val serv = dataSnapshot.child("service_type").child(id).getValue<ServiceType>()
                serv_name_view.text = serv?.name
                price_view.text = serv?.price?.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}