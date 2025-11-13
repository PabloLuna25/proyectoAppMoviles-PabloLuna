package com.example.pulsetasks

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.toColorInt

class SignUpActivity: AppCompatActivity() {
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
                Shader.TileMode.CLAMP
            )

            titulo.paint.shader = shader
            titulo.invalidate()
        }
        animator.start()

    }
}