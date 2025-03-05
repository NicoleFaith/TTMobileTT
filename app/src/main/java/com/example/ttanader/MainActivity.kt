package com.example.ttanader

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var fabAddProject: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        fabAddProject = findViewById(R.id.AddBtn)

        // Set up Toolbar
        setSupportActionBar(toolbar)

        // Set up Navigation Drawer Toggle
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)

        // Change the drawer button (hamburger icon) color
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.white) // Change color of toggle btn here

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set Navigation Listener
        navigationView.setNavigationItemSelectedListener(this)

        // Load default fragment (HomeFragment)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        // Floating Action Button Click Event
        fabAddProject.setOnClickListener {
            Toast.makeText(this, "Add New Project Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Open 'Add Project' Activity or Fragment
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_project -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProjectFragment()).commit()
            R.id.nav_info -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment()).commit()
            R.id.nav_logout -> Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
        }

        // Close the drawer after selecting an item
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
