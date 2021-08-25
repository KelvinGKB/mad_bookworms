package com.mad.mad_bookworms.security

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            val intent = Intent(this, VerifyActivity::class.java)
            startActivity(intent)
        }


    }

}