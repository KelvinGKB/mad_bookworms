package com.mad.mad_bookworms.security

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.databinding.ActivityForgotPasswordBinding
import com.mad.mad_bookworms.databinding.ActivityVerifyBinding

class VerifyActivity : AppCompatActivity() {
    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    private lateinit var binding: ActivityVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnSignUp.setOnClickListener()
//        {
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }


    }
}