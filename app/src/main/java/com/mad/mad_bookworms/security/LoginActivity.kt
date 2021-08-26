package com.mad.mad_bookworms.security

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }
    private lateinit var auth: FirebaseAuth


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener()
        {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnForgot.setOnClickListener()
        {
//            val intent = Intent(this, ForgotPasswordActivity::class.java)
//            startActivity(intent)
            Firebase.auth.signOut()
        }

        binding.btnSignIn.setOnClickListener()
        {
            val email = binding.edtEmail.editText?.text.toString().trim()
            val password = binding.edtPassword.editText?.text.toString().trim()

            if(email == null || email == "")
            {
                showText("Email is required !")
                return@setOnClickListener
            }
            if(password == null || password == "")
            {
                showText("Password is required !")
                return@setOnClickListener
            }

            Signin(email,password)
        }


    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload();
            Toast.makeText(this, "Signed In", Toast.LENGTH_LONG).show()
        }
    }

    fun Signin(email:String,password:String){
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Password incorrect or Account does not exist.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }

    fun showText(text : String ){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


}