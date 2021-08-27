package com.mad.mad_bookworms.security

import android.R
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.databinding.ActivityForgotPasswordBinding


class ForgotPasswordActivity : AppCompatActivity() {
    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener()
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSend2.setOnClickListener()
        {


            val email = binding.edtEmail.editText?.text.toString().trim()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            if(email == null || email == "")
            {
                showText("Email is required !")
                return@setOnClickListener
            }
            if (!email.matches(emailPattern.toRegex())) {
                showText("Invalid Email !")
                return@setOnClickListener
            }


            val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
            val animFadeOut: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)

            binding.tvButton.startAnimation(animFadeIn)
            binding.progressBar2.startAnimation(animFadeIn)

            binding.progressBar2.isVisible = true
            binding.tvButton.setText("Sending...");

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    Handler(Looper.getMainLooper()).postDelayed({

                        if (task.isSuccessful) {
                            Log.d(TAG, "Reset Email sent.")

                            binding.tvButton.startAnimation(animFadeIn)
                            binding.tvButton.setText("Sent")

                            binding.progressBar2.startAnimation(animFadeOut)
                            binding.progressBar2.isVisible=false

                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }, 1000)


                        }else{
                            Log.d(TAG, "Account does not exists.")

                            binding.tvButton.startAnimation(animFadeIn)
                            binding.tvButton.setText("Email send unsuccessful.")

                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.tvButton.startAnimation(animFadeIn)
                                binding.tvButton.setText("Send")
                            }, 2000)

                            binding.progressBar2.startAnimation(animFadeOut)
                            binding.progressBar2.isVisible=false
                        }

                    }, 2000)
                }
        }


    }

    fun showText(text : String ){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}