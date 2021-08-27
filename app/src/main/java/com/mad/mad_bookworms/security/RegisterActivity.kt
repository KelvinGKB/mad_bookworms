package com.mad.mad_bookworms.security

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.mad.mad_bookworms.databinding.ActivityLoginBinding
import com.mad.mad_bookworms.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener()
        {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener()
        {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val validation =  binding.validation

            val username = binding.edtUsername.editText?.text.toString().trim()
            val email = binding.edtEmail.editText?.text.toString().trim()
            val conPassword = binding.edtConPassword.editText?.text.toString()
            val password = binding.edtPassword.editText?.text.toString()
            val referral = binding.edtReferral.editText?.text.toString()

            if(username == null || username == "")
            {
//                validation.text = "Username cannot be empty !"
//                validation.isVisible = true
                showText("Username is required !")
                return@setOnClickListener

            }
            if(email == null || email == "")
            {
                showText("Email is required !")
                return@setOnClickListener
            }
            if (!email.matches(emailPattern.toRegex())) {
                showText("Invalid Email !")
                return@setOnClickListener
            }
            if(password == null || password == "")
            {
                showText("Password is required !")
                return@setOnClickListener
            }
            if(password.length < 6 )
            {
                showText("Password must be at least 6 characters !")
                return@setOnClickListener
            }
            if(conPassword == null || conPassword == "")
            {
                showText("Confirm Password is required !")
                return@setOnClickListener
            }
            if(password != conPassword)
            {
                showText("Confirm Password does not match Password !")
                return@setOnClickListener
            }
            if(referral != null || referral != "")
            {
                validateReferral(referral)
            }

            val intent = Intent(this, VerifyActivity::class.java)
                .putExtra("username",username)
                .putExtra("email",email)
                .putExtra("password",password)
                .putExtra("referral",referral)

            startActivity(intent)

        }


    }

    fun showText(text : String ){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun validateReferral(code : String)
    {

    }

}