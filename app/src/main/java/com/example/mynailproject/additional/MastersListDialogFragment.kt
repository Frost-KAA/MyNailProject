package com.example.mynailproject.additional

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynailproject.R
import com.example.mynailproject.adapter.MasterListDialogAdapter
import com.example.mynailproject.database.Master
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class MastersListDialogFragment(context: Context, var clicked_lick: ArrayList<String>, val fr: Fragment) : Dialog(context){

    lateinit var recycler : RecyclerView
    private var database: DatabaseReference = Firebase.database.reference
    val list = ArrayList<Master>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fragment_masters_list_dialog)

        addUserEventListener(database)

        recycler =  findViewById(R.id.recycler_view_masters)
        recycler.adapter = this.context.let { MasterListDialogAdapter(it, list, clicked_lick) }
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.setHasFixedSize(true)


        val yes: Button = findViewById(R.id.no)
        val no: Button = findViewById(R.id.yes)

        yes.setOnClickListener{
            clicked_lick = (recycler.adapter as MasterListDialogAdapter).getClickedList()
            val fragment = fr as AddServiceFragment
            fragment.getClicked(clicked_lick)
            dismiss()
        }
        no.setOnClickListener{
            dismiss()
        }

    }

    fun addUserEventListener(userReference: DatabaseReference) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (list.size > 0) list.clear()
                for (ds in dataSnapshot.child("masters").children){
                    val mast: Master? = ds.getValue<Master>()
                    if (mast != null) {
                        list.add(mast)
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