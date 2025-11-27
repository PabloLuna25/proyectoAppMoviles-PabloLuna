package com.example.pulsetasks

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date


class SignUpActivity: AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()

    fun dpToPx(dp: Float): Float {
        val density = resources.displayMetrics.density
        return dp * density
    }

    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titulo = findViewById<TextView>(R.id.lbTitle)


        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 2000L
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->

            val colorPrimary1 = ContextCompat.getColor(this, R.color.primary_1)
            val colorPrimary2 = ContextCompat.getColor(this, R.color.primary_2)
            val value = animation.animatedFraction
            val width = titulo.width.toFloat()

            val shader = LinearGradient(
                -width + (width * 2 * value), 0f, 0f + (width * 2f * value), 0f,
                intArrayOf(colorPrimary1, colorPrimary2, colorPrimary1),
                null,
                Shader.TileMode.MIRROR
            )

            titulo.paint.shader = shader
            titulo.invalidate()
        }
        animator.start()

        val lbUsername = findViewById<TextView>(R.id.lbUsername)
        txtUsername = findViewById(R.id.txtUsername)
        val lbPassword = findViewById<TextView>(R.id.lbPassword)
        txtPassword = findViewById(R.id.txtPassword)
        btnCancel = findViewById<Button>(R.id.btnCancel)
        btnSignIn = findViewById<Button>(R.id.btnSignIn)

        //Animaciones
        val usernameLabelAnimUp = AnimationUtils.loadAnimation(this, R.anim.label_move_top)
        val usernameLabelAnimBack = AnimationUtils.loadAnimation(this, R.anim.label_move_back)
        val passwordLabelAnimUp = AnimationUtils.loadAnimation(this, R.anim.label_move_top)
        val passwordLabelAnimBack = AnimationUtils.loadAnimation(this, R.anim.label_move_back)

        //Prueba movimiento real de margenes
        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        val transition = AutoTransition()


        //Llamado de la animación en username
        txtUsername.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && txtUsername.text.isEmpty()){
                transition.duration = 300
                constraintSet.setMargin(R.id.txtUsername, ConstraintSet.TOP, dpToPx(40f).toInt())
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                constraintSet.applyTo(constraintLayout)
                lbUsername.startAnimation(usernameLabelAnimUp)
                println("Username anim up")
            }else if(txtUsername.text.isEmpty()){
                transition.duration = 300
                constraintSet.setMargin(R.id.txtUsername, ConstraintSet.TOP, dpToPx(30f).toInt())
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                constraintSet.applyTo(constraintLayout)

                lbUsername.startAnimation(usernameLabelAnimBack)
            }
        }

        //Llamado de la animación en password
        txtPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && txtPassword.text.isEmpty()){
                transition.duration = 300
                constraintSet.setMargin(R.id.txtPassword, ConstraintSet.TOP, dpToPx(25f).toInt())
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                constraintSet.applyTo(constraintLayout)
                lbPassword.startAnimation(passwordLabelAnimUp)
                println("Password anim up")
            }else if(txtPassword.text.isEmpty()) {
                transition.duration = 300
                constraintSet.setMargin(R.id.txtPassword, ConstraintSet.TOP, dpToPx(15f).toInt())
                TransitionManager.beginDelayedTransition(constraintLayout, transition)
                constraintSet.applyTo(constraintLayout)
                lbPassword.startAnimation(passwordLabelAnimBack)
            }
        }

        btnSignIn.setOnClickListener {
            registrarUser()
        }

        btnCancel.setOnClickListener {
            onBackPressed()
        }


    }

    private fun registrarUser(){
        val username = txtUsername.text.toString()
        val password = txtPassword.text.toString()

        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Llene los campos pai", Toast.LENGTH_SHORT).show()
        }else{
            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val dt: Date = Date()
                        val user = hashMapOf(
                            "idemp" to task.result?.user?.uid,
                            "usuario" to username,
                            "email" to username,
                            "ultAcceso" to dt.toString(),
                        )
                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->

                                //Register the data into the local storage
                                val prefe = this.getSharedPreferences("appData", Context.MODE_PRIVATE)

                                //Create editor object for write app data
                                val editor = prefe.edit()

                                //Set editor fields with the new values
                                editor.putString("username", username.toString())
                                editor.putString("contra", password.toString())

                                //Write app data
                                editor.commit()

                                Toast.makeText(this,"Usuario registrado correctamente",Toast.LENGTH_SHORT).show()

                                Intent().let {
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this,"Error al registrar usuario - DB",Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this,"Error al registrar usuario - AUTH",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}