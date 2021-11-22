package com.example.mynailproject.additional

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mynailproject.additional.AddStaffFragmentArgs
import com.example.mynailproject.R
import com.example.mynailproject.database.DBCall
import com.example.mynailproject.database.Master
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class InfoStaffFragment : Fragment() {

    private var database: DatabaseReference = Firebase.database.reference
    val storage = Firebase.storage

    lateinit var surname : TextView
    lateinit var name : TextView
    lateinit var pathronim : TextView
    lateinit var info : TextView
    lateinit var phone : TextView
    lateinit var photo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_staff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        surname = view.findViewById(R.id.staff_info_surname)
        name = view.findViewById(R.id.staff_info_name)
        pathronim = view.findViewById(R.id.staff_info_pathronim)
        info = view.findViewById(R.id.staff_info_info)
        phone = view.findViewById(R.id.staff_info_phone)
        photo = view.findViewById(R.id.staff_info_photo)

        val uid = getUid()
        addUserEventListener(database, uid)

        val storageRef = storage.reference
        val imagesRef = storageRef.child("master_photo/$uid.jpg")
        addUserEventListener(database, uid)

        val ONE_MEGABYTE: Long = 1024 * 1024 * 100
        imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            val bm: Bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            photo.setImageBitmap(bm)
        }.addOnFailureListener {
            Log.d("FAIL", it.toString())
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
                surname.text = master?.user?.surname
                name.text = master?.user?.name
                pathronim.text = master?.user?.pathronim
                info.text = master?.info
                phone.text = master?.user?.phone
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        userReference.addValueEventListener(userListener)
    }

}