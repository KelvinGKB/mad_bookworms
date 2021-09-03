package com.mad.mad_bookworms.security

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.databinding.ActivityLoginBinding
import com.mad.mad_bookworms.databinding.ActivityRegisterBinding
import com.mad.mad_bookworms.viewModels.UserViewModel
import java.util.*

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

    private val vm : UserViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = intent
        val data = intent.data

        if(data != null)
        {
            val u = data!!.pathSegments
            val referral_code = u.get(u.size - 1)
            binding.edtReferral.editText?.setText(referral_code)

        }
//        Toast.makeText(applicationContext,"referral : " + p,Toast.LENGTH_LONG).show()
        val c = vm.getAll()

        Log.w(ContentValues.TAG, "data :" + data)

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
            var referred_by = ""


            val u = User(
                username  = username,
                email   = email,
                referral_code = referral,
            )

            val err = vm.validate(u,conPassword,password)

            if (err != "") {
                showText(err)
                Log.d(ContentValues.TAG, err)
                return@setOnClickListener
            }

            if(referral != "") {

//                val u = validateReferral(referral)
//                if(u == false)
//                {
//                    Toast.makeText(applicationContext,"Invalid Referral Code.", Toast.LENGTH_SHORT).show()
////                    return@setOnClickListener
//                }
                referred_by = vm.getReferrer(referral)?.id.toString()

            }

            val intent = Intent(this, VerifyActivity::class.java)
                .putExtra("username",username)
                .putExtra("email",email)
                .putExtra("password",password)
                .putExtra("referral",referral)
                .putExtra("referred_by",referred_by)

            startActivity(intent)

        }


    }

    fun showText(text : String ){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun validateReferral(code : String) : Boolean
    {

        val users = vm.getAll()
        val p =  users.value?.any { f -> f.referral_code == code } ?: false

        if(p == false)
        {
            return false
        }

        return true
    }

}