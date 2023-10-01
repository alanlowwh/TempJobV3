package com.example.tempjobv3

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Change toolbar name
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)

        // Enable the "Up" button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up navigation with the NavController
        setupActionBarWithNavController(navController)

        // Get a reference to the BottomNavigationView
        bottomNavView = findViewById(R.id.bottom_nav_view)

        // Connect the BottomNavigationView with the NavController
        bottomNavView.setupWithNavController(navController)

        // Listen for destination changes and hide/show the BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the current destination is the login, signUp, or firstPage
            if (destination.id == R.id.login || destination.id == R.id.signUp || destination.id == R.id.firstPage) {
                bottomNavView.visibility = View.GONE
            } else {
                bottomNavView.visibility = View.VISIBLE
            }
        }

        bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.job_btm_nav -> {
                    // Check the user's role and navigate accordingly
                    FirebaseAuth.getInstance().currentUser?.email?.let { email ->
                        checkUserRole(email)
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile_btm_nav -> {
                    // Navigate to Profile
                    navController.navigate(R.id.profile)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navController // Make sure navController is properly initialized
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkUserRole(email: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val usersRef: DatabaseReference = databaseReference.child("users")

        // Check the user's role in the 'users' entity
        usersRef.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User found in 'users' entity, check their role
                        val user = dataSnapshot.children.firstOrNull()
                        val role = user?.child("role")?.value as? String

                        if (role == "admin") {
                            // User has the 'admin' role, navigate to the admin screen
                            navController.navigate(R.id.list_job)
                        } else {
                            // User has the 'customer' role, navigate to the customer job list
                            navController.navigate(R.id.cusJobList)
                        }
                    } else {
                        // User not found in 'users' entity
                        Toast.makeText(
                            this@MainActivity,
                            "User not found in 'users' entity.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    Toast.makeText(
                        this@MainActivity,
                        "Database error: " + databaseError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
