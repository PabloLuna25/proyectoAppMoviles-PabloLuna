package com.example.pulsetasks

import Data.Reminder
import Util.ReminderAdapter
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class HomeActivity: AppCompatActivity() {

    lateinit var btnAddReminder: Button
    private val reminders = mutableListOf<Reminder>()
    private lateinit var adapter: ReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //---------------------------------
        val uid = FirebaseAuth.getInstance().uid
        //---------------------------------

        FirebaseFirestore.getInstance()
            .collection("reminders")
            .whereEqualTo("uid", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)

        val recycler = findViewById<RecyclerView>(R.id.recyclerReminders)
        adapter = ReminderAdapter(
            reminders,
            onEdit = { reminder -> openEdit(reminder) },
            onDelete = { reminder -> deleteReminder(reminder) }
        )

        loadReminders()

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

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
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                Log.d("AUTH", "UID = ${FirebaseAuth.getInstance().currentUser?.uid}")
                Util.Util.openActivity(this, MainActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadReminders() {
        val userUid = FirebaseAuth.getInstance().uid

        if (userUid == null) {
            Log.e("REMINDERS", "User UID is null")
            return
        }

        FirebaseFirestore.getInstance()
            .collection("reminders")
            .whereEqualTo("uid", userUid)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("REMINDERS", "Firestore error", error)
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    Log.d("REMINDERS", "No reminders found")
                    reminders.clear()
                    adapter.notifyDataSetChanged()
                    return@addSnapshotListener
                }

                reminders.clear()
                reminders.addAll(snapshot.toObjects(Reminder::class.java))
                adapter.notifyDataSetChanged()

                Log.d("REMINDERS", "Loaded ${reminders.size} reminders")
            }
    }

    fun deleteReminder(reminder: Reminder) {
        FirebaseFirestore.getInstance()
            .collection("reminders")
            .document(reminder.id)
            .delete()

        reminder.imageUrl?.let { url ->
            FirebaseStorage.getInstance()
                .getReferenceFromUrl(url)
                .delete()
        }
    }

    fun openEdit(reminder: Reminder) {
        val intent = Intent(this, AddReminderActivity::class.java)
        intent.putExtra("reminderId", reminder.id)
        startActivity(intent)
    }

}