package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.weather.favourit.search
import com.example.weather.home.HomeFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    //lateinit var btn:Button
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*btn=findViewById(R.id.button)
        btn.setOnClickListener{
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }*/
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)

        drawerLayout=findViewById(R.id.drawer)
        val navigationView=findViewById<NavigationView>(R.id.nav)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle= ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.home-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
           R.id.favourit-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,search()).commit()
          // R.id.home-> supportFragmentManager.beginTransaction().replace(R.id.fragment_container,HomeFragment()).commit()
       }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
       }else{
           onBackPressedDispatcher.onBackPressed()
       }
    }
}