package com.example.emre_bitik_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.emre_bitik_final.fragments.CategoriesFragment
import com.example.emre_bitik_final.fragments.HomeFragment
import com.example.emre_bitik_final.fragments.LikedFragment
import com.example.emre_bitik_final.fragments.OrdersFragment
import com.example.emre_bitik_final.fragments.ProfileFragment
import com.example.emre_bitik_final.fragments.SearchFragment
import com.example.emre_bitik_final.models.User
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    lateinit var new :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById<DrawerLayout>(R.id.main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val user = intent.getSerializableExtra("user") as? User




        //ilk yüklenecek fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()


    }
    //fragmentlara tıklama işlemi
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_categories -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CategoriesFragment()).commit()
            R.id.nav_search -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment()).commit()
            R.id.nav_liked -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LikedFragment()).commit()
            R.id.nav_ordes -> {
                val user = intent.getSerializableExtra("user") as? User
                user?.let {
                    val ordesFragment = OrdersFragment.newInstance(user)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ordesFragment)
                        .commit()
                }
            }
            R.id.nav_profile -> {
                val user = intent.getSerializableExtra("user") as? User
                user?.let {
                    val profileFragment = ProfileFragment.newInstance(user)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment)
                        .commit()
                }
            }
            R.id.nav_exit -> finish()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}