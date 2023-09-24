package com.example.tempjobv3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Change toolbar name
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)


// Enable the "Up" button
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up navigation with the NavController
        setupActionBarWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navController // Make sure navController is properly initialized
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}