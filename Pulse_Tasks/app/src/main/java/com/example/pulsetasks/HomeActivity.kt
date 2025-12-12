package com.example.pulsetasks

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class HomeActivity: AppCompatActivity() {

    lateinit var btnAddReminder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //---------------------------------
        val uid = FirebaseAuth.getInstance().uid
        var testo = findViewById<TextView>(R.id.letest)

        testo.text = uid.toString()
        //---------------------------------

        btnAddReminder = findViewById(R.id.btn_addReminder)
        btnAddReminder.setOnClickListener { _ ->
            Util.Util.openActivity(this, AddReminderActivity::class.java)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.home_toolbar_menu, menu)

        return true
    }

    //Creates option selection panel
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.btnLogOut -> {
                Toast.makeText(this, "Logged out yey", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                Log.d("AUTH", "UID = ${FirebaseAuth.getInstance().currentUser?.uid}")
                Util.Util.openActivity(this, MainActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}