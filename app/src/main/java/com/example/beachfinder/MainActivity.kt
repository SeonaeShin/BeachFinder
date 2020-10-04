package com.example.beachfinder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //Actionbar 숨기기
        getSupportActionBar()?.hide()
    }
}

 //Extension function to replace fragment
fun AppCompatActivity.replaceFragment(fragment: Fragment){
//    val fragmentManager = supportFragmentManager
//    val transaction = fragmentManager.beginTransaction()
//
//    transaction.add(R.id.nav_host_fragment,fragment)
//    Log.d("R.id.mapView >>", "${R.id.mapView}")
//    transaction.addToBackStack(null)
//    transaction.commit()
     if (!isFinishing && !isDestroyed) {
         val ft: FragmentTransaction = supportFragmentManager
             .beginTransaction()
         ft.replace(R.id.nav_host_fragment, fragment)
         ft.commit()
     }
//    transaction.commitAllowingStateLoss()

}


fun AppCompatActivity.finishFragment(fragment: Fragment){
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()
    transaction.remove(fragment)
    transaction.commit()
}


