package com.example.pulsetasks

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.cardview.widget.CardView
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun dpToPx(dp: Float): Float {
            val density = resources.displayMetrics.density
            return dp * density
        }


        val card = findViewById<CardView>(R.id.loginPanel_main)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_up)
        card.post {
            card.visibility = View.VISIBLE
            card.startAnimation(animation)
        }

        //Label y text field para la animación
        val lbUsername = findViewById<TextView>(R.id.lbUsername)
        val txtUsername = findViewById<TextView>(R.id.txtUsername)
        val lbPassword = findViewById<TextView>(R.id.lbPassword)
        val txtPassword = findViewById<TextView>(R.id.txtPassword)

        //Animaciones
        val usernameLabelAnimUp = AnimationUtils.loadAnimation(this, R.anim.label_move_top)
        val usernameLabelAnimBack = AnimationUtils.loadAnimation(this, R.anim.label_move_back)
        val passwordLabelAnimUp = AnimationUtils.loadAnimation(this, R.anim.label_move_top)
        val passwordLabelAnimBack = AnimationUtils.loadAnimation(this, R.anim.label_move_back)

        //Prueba movimiento real de margenes
        val constraintLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        var transition = AutoTransition()


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


    }
}