package com.mad.mad_bookworms.security

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.databinding.ActivityForgotPasswordBinding
import com.mad.mad_bookworms.databinding.ActivityLoginBinding

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

//        binding.btnSignUp.setOnClickListener()
//        {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }


    }
}