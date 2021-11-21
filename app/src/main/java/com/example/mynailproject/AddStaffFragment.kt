package com.example.mynailproject

import android.R.attr.bitmap
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.IOException


class AddStaffFragment : Fragment() {

    private var database: DatabaseReference = Firebase.database.reference

    lateinit var info_view : TextView
    lateinit var photo_view : ImageView
    lateinit var user: User

    val GALLERY_REQUEST = 1
    val storage = Firebase.storage


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
        val storageRef = storage.reference
        val imagesRef = storageRef.child("master_photo/$uid")

        val load: Button = view.findViewById(R.id.button_photo_staff)
        load.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }

        val save: Button = view.findViewById(R.id.button_save_staff)
        save.setOnClickListener {
            val info = info_view.text.toString()
            if (photo_view.visibility == VISIBLE){
                photo_view.isDrawingCacheEnabled = true
                photo_view.buildDrawingCache()
                val bitmap = (photo_view.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imagesRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    Toast.makeText(this.context, "Ошибка загрузки",
                        Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this.context, "Фото загружено",
                        Toast.LENGTH_SHORT).show()
                }
            }
            val mas = Master(uid, info, null, user)
            val db_call = DBCall()
            db_call.editMaster(uid, mas)
            view.findNavController().navigate(R.id.action_global_staffFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageIntent)
        when (requestCode) {
            GALLERY_REQUEST -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = imageIntent?.getData()
                photo_view.setImageURI(selectedImage)
                photo_view.visibility = VISIBLE
            }
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
                user = master?.user!!
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