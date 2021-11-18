package com.example.mynailproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

class AddServiceFragment : Fragment() {

    var num = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_service, container, false)
    }

    fun getMyData(): Int {
        val args: AddServiceFragmentArgs by navArgs()
        if (!args.number.isNullOrEmpty()){
            num = args.number
        }
        return num
    }

}