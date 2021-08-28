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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.data.LocalDB
import com.mad.mad_bookworms.data.LocalDao
import com.mad.mad_bookworms.data.User_Table
import com.mad.mad_bookworms.databinding.ActivityLoginBinding
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.*


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

    lateinit var dao: LocalDao
    private lateinit var auth: FirebaseAuth
    private val vm : UserViewModel by viewModels()
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
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
//            Firebase.auth.signOut()
        }

        binding.btnSignIn2.setOnClickListener()
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

        //Animation
        val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload();
            binding.tvButton.startAnimation(animFadeIn)
            binding.tvButton.setText("Signing In...");

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }, 1500)

        }
    }

    fun Signin(email:String,password:String){

        //Animation
        val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        val animFadeOut: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)

        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                    binding.tvButton.startAnimation(animFadeIn)
                    binding.tvButton.setText("Signing In...");


//                    val u = user?.let { vm.get(it.uid) }
//                    if (u != null) {
//                        Toast.makeText(baseContext, "HI.",Toast.LENGTH_SHORT).show()
//                        createUser(u)
//                    }

//                    CoroutineScope(Dispatchers.IO).launch {
//                         auth.currentUser?.let { createUser(it.uid)}
//                    }

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }, 1500)

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

    suspend fun createUser(u: String) = coroutineScope{

        Log.w(TAG, "Create User : "+u)
        val c = vm.get(u)
//        val u = async { vm.get(u) }
//        val p = u.await()

        Log.w(TAG, "Create User : " + c.toString())
        dao = LocalDB.getInstance(application).LocalDao

        val m = c?.let { User_Table(it.id,it.username,it.email,it.level,it.role,it.earn_points,it.usable_points,it.referral_code,it.referred_by) }
//            val m = User_Table("asdasdasd","GKB","kengboon@gmail.com",
//                "Silver","Admin",1000,1000,"asdasdasd","sadasd")
        CoroutineScope(Dispatchers.IO).launch {
            if (m != null) {
                dao.insertUser(m)
                Log.w(TAG, "Create User Not null")
            }else{
                Log.w(TAG, "Create User null")
            }
        }

    }


}