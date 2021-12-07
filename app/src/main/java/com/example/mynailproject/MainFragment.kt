package com.example.mynailproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mynailproject.database.DBCall


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DBCall.CurrentRole.setRole()
        val bottom = view.findViewById<Button>(R.id.button_service)
        bottom.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_basicActivity)
        }
        /*
        view.findViewById<Button>(R.id.button_service).setOnClickListener {
            val action = EnterFragmentOnMainDirections.actionEnterFragmentOnMainToBasicActivity(name)
            view.findNavController().navigate(action)
            //view.findNavController().navigate(R.id.action_enterFragmentOnMain_to_basicActivity, bundle)
        }*/
    }
}