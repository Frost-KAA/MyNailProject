package com.example.mynailproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.Master
import com.example.mynailproject.database.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class AddStaffFragment : Fragment() {

    private var database: DatabaseReference = Firebase.database.reference

    lateinit var info_view : TextView
    lateinit var photo_view : ImageView
    lateinit var user: User



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_staff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = getUid()

        info_view = view.findViewById(R.id.info_staff)
        photo_view = view.findViewById(R.id.photo_staff)

        addUserEventListener(database, uid)

        val load: Button = view.findViewById(R.id.button_photo_staff)
        load.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        val save: Button = view.findViewById(R.id.button_save_serv)
        save.setOnClickListener {
            val info = info_view.text.toString()
            val mas = Master(uid, info, null, user)
            val db_call = DBCall()
            db_call.editMaster(uid, mas)
            view.findNavController().navigate(R.id.action_global_priceFragment)
        }
    }

    fun getUid(): String {
        var u: String = ""
        val args = arguments?.let { AddStaffFragmentArgs.fromBundle(it) }
        if (!(args?.uid == null)){
            u = args.uid
        }
        return u
    }


    fun addUserEventListener(userReference: DatabaseReference, uid: String) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val master = dataSnapshot.child("masters").child(uid).getValue<Master>()
                info_view.text = master?.info
                user = master?.user
                if (master?.photo == null){
                    photo_view.visibility = INVISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}