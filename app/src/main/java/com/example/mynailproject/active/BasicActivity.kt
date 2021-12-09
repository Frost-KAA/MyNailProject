package com.example.mynailproject.active

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.mynailproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class BasicActivity : AppCompatActivity() {

   private var name: String = "Дорогой пользователь"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
        setSupportActionBar(findViewById(R.id.toolbar))

        this.supportActionBar?.title = "Главная"
        val is_autorized: Boolean = false
        val navController = Navigation.findNavController(this, R.id.fragment)
        val bottom_nav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottom_nav, navController)

    }


}