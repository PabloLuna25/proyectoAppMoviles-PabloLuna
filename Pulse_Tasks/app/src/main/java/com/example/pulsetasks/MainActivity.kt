package com.example.pulsetasks

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.cardview.widget.CardView
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var auth = FirebaseAuth.getInstance()
    private lateinit var btnLogin: Button
    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var lbPassword: TextView
    private lateinit var lbUsername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Conversión de density pixels (dp) a pixeles (px)
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
        lbUsername = findViewById(R.id.lbUsername)
        lbPassword = findViewById(R.id.lbPassword)
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)


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

        val btnSignIn = findViewById<Button>(R.id.btnSignup)
        btnSignIn.setOnClickListener(View.OnClickListener{ view->
            Util.Util.openActivity(this
                , SignUpActivity::class.java)

            resetMainScreen()
        })

        btnLogin = findViewById(R.id.btnLogin_main)
        btnLogin.setOnClickListener {
            onLogin()
        }

        Log.d("AUTH", "UID = ${FirebaseAuth.getInstance().currentUser?.uid}")


    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser?.uid
        if(user != null){
            Util.Util.openActivity(this, HomeActivity::class.java)
            finish()
        }

    }

    private fun onLogin(){
        btnLogin = findViewById(R.id.btnLogin_main)
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)

        if(txtUsername.text.isEmpty() || txtPassword.text.isEmpty()){
            Toast.makeText(this, "Llene los campos MAE PORFAVOR DAAAMN", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(txtUsername.text.toString(), txtPassword.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Auth OK", Toast.LENGTH_SHORT).show()
                    Util.Util.openActivity(this, HomeActivity::class.java)
                    Log.d("AUTH", "UID = ${FirebaseAuth.getInstance().currentUser?.uid}")
                    resetMainScreen()
                }else{
                    Toast.makeText(this,"Error con la autenticación", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun resetMainScreen(){
        //Variables to and for reset
        lbUsername = findViewById(R.id.lbUsername)
        lbPassword = findViewById(R.id.lbPassword)
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        val usernameLabelAnimBack = AnimationUtils.loadAnimation(this, R.anim.label_move_back)
        val passwordLabelAnimBack = AnimationUtils.loadAnimation(this, R.anim.label_move_back)

        //Reset of elements to a base state
        txtUsername.setText("")
        txtPassword.setText("")
        txtUsername.clearFocus()
        txtPassword.clearFocus()
        lbUsername.startAnimation(usernameLabelAnimBack)
        lbPassword.startAnimation(passwordLabelAnimBack)

    }
}