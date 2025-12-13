package com.example.pulsetasks

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar

class AddReminderActivity: AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var btnPickImage: Button
    private var selectedImageUri: Uri? = null

    lateinit var txtTitle: EditText
    lateinit var txtDescription: EditText
    lateinit var txtHour: EditText
    lateinit var txtMinute: EditText
    lateinit var btnSaveReminder: Button
    lateinit var btnCancelAddReminder: Button

    val db = FirebaseFirestore.getInstance()

    private lateinit var calendarView: CalendarView
    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        calendarView = findViewById(R.id.calendarView)
        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDescription)
        txtHour = findViewById(R.id.txtHour)
        txtMinute = findViewById(R.id.txtMinute)

        calendarView.setOnDateChangeListener { _, year, month, day ->
            selectedYear = year
            selectedMonth = month + 1
            selectedDay = day
        }

        imgPreview = findViewById(R.id.imgPreview)
        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    selectedImageUri = it
                    imgPreview.setImageURI(it)
                    imgPreview.visibility = View.VISIBLE
                }
            }

        btnPickImage = findViewById(R.id.btnAddImage)
        btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSaveReminder = findViewById(R.id.btnSaveReminder)
        btnSaveReminder.setOnClickListener {
            val title = txtTitle.text.toString().trim()
            val description = txtDescription.text.toString().trim()
            val hour = txtHour.text.toString().toInt()
            val minute = txtMinute.text.toString().toInt()

            val reminderRef = db.collection("reminders").document()

            val reminderId = reminderRef.id

            if (selectedImageUri != null) {

                val imageRef = FirebaseStorage.getInstance()
                    .reference
                    .child("reminder_images/$reminderId.jpg")

                imageRef.putFile(selectedImageUri!!)
                    .continueWithTask { task ->
                        if (!task.isSuccessful) {
                            throw task.exception!!
                        }
                        imageRef.downloadUrl
                    }
                    .addOnSuccessListener { imageUrl ->
                        saveReminderToFirestore(
                            reminderRef,
                            reminderId,
                            title,
                            description,
                            hour,
                            minute,
                            imageUrl.toString()
                        )
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }

            } else {
                // No image
                saveReminderToFirestore(
                    reminderRef,
                    reminderId,
                    title,
                    description,
                    hour,
                    minute,
                    null
                )
            }
        }

        btnCancelAddReminder = findViewById(R.id.btnCancelReminder)
        btnCancelAddReminder.setOnClickListener {
            onBackPressed()
        }

    }

    fun saveReminderToFirestore(
        reminderRef: DocumentReference,
        reminderId: String,
        title: String,
        description: String,
        hour: Int,
        minute: Int,
        imageUrl: String?
    ) {

        val userUid = FirebaseAuth.getInstance().uid

        val reminderData = hashMapOf(
            "id" to reminderId,
            "uid" to userUid,
            "title" to title,
            "description" to description,
            "year" to selectedYear,
            "month" to selectedMonth,
            "day" to selectedDay,
            "hour" to hour,
            "minute" to minute,
            "imageUrl" to imageUrl,
            "createdAt" to FieldValue.serverTimestamp()
        )

        reminderRef.set(reminderData)
            .addOnSuccessListener {
                Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save reminder", Toast.LENGTH_SHORT).show()
            }
    }

}