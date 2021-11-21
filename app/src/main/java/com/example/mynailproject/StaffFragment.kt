package com.example.mynailproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.adapter.PriceAdapter
import com.example.mynailproject.database.Master
import com.example.mynailproject.database.ServiceType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class StaffFragment : Fragment() {

    lateinit var recycler : RecyclerView
    val list = ArrayList<Master>()
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addUserEventListener(database)
        return inflater.inflate(R.layout.fragment_staff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //подключение адаптера
       /* recycler =  view.findViewById(R.id.recycler_view)
        recycler.adapter = this.context?.let { PriceAdapter(it, list) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)*/

        val add_b : FloatingActionButton = view.findViewById(R.id.add_button_staff)
        add_b.setOnClickListener {
            val action = StaffFragmentDirections.actionStaffFragmentToAddStaffFragment(null)
            view.findNavController().navigate(action)
        }
    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (list.size > 0) list.clear()
                for (ds in dataSnapshot.child("masters").children){
                    val master: Master? = ds.getValue<Master>()
                    if (master != null) {
                        list.add(master)
                    }
                }
                recycler.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}