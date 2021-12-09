package com.example.mynailproject.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mynailproject.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity: BasicActivity = activity as BasicActivity
        activity.supportActionBar?.title = "Главная"

        val text1 = view.findViewById<TextView>(R.id.hello_name)
        text1.text = "Рады видеть вас!"

        val bottom = view.findViewById<Button>(R.id.button_service)
        bottom.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeFragment_to_recordServiceFragment)
        }

    }
}