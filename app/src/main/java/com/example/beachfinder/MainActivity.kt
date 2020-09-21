package com.example.beachfinder

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.beachfinder.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

 //Extension function to replace fragment
fun AppCompatActivity.replaceFragment(fragment: Fragment){
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()

    transaction.add(R.id.nav_host_fragment,fragment)
//     transaction.add(R.id.nav_host_fragment,fragment)
     Log.d("R.id.mapView >>", "${R.id.mapView}")
//    transaction.replace(R.id.mapView,fragment)
//     transaction.add(R.id.mapView,fragment)
    transaction.addToBackStack(null)
    transaction.commit()
}


fun AppCompatActivity.finishFragment(fragment: Fragment){
    Log.d("MainActivity","finish")
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()
    Log.d("MainActivity","${transaction}")
    transaction.remove(fragment)
    transaction.commit()
    Log.d("MainActivity","${transaction}")

}


