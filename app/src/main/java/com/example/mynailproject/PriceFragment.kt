package com.example.mynailproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.adapter.PriceAdapter
import com.example.mynailproject.database.ServiceType
import com.example.mynailproject.database.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class PriceFragment : Fragment() {

    lateinit var recycler : RecyclerView
    val list = ArrayList<ServiceType>()
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addUserEventListener(database)
        return inflater.inflate(R.layout.fragment_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //подключение адаптера
        recycler =  view.findViewById(R.id.recycler_view)
        recycler.adapter = this.context?.let { PriceAdapter(it, list) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)

        val add_b : FloatingActionButton = view.findViewById(R.id.add_button)
        add_b.setOnClickListener {
            val action = PriceFragmentDirections.actionPriceFragmentToAddServiceFragment(0, list[list.size-1].id!!)
            view.findNavController().navigate(action)
        }
    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (list.size > 0) list.clear()
                  for (ds in dataSnapshot.child("service_type").children){
                      val serv: ServiceType? = ds.getValue<ServiceType>()
                      if (serv != null) {
                          Log.d("SERV", serv.name.toString())
                          list.add(serv)
                      }
                  }
                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}