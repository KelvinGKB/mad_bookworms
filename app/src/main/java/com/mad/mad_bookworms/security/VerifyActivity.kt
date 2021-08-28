package com.mad.mad_bookworms.security

import android.R
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.viewModels.UserViewModel
import com.mad.mad_bookworms.databinding.ActivityVerifyBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

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

    private lateinit var auth: FirebaseAuth
//    private var mAuth: FirebaseAuth? = null

    private val vm : UserViewModel by viewModels()

    private lateinit var binding: ActivityVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Animation
        val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        val animFadeOut: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)

        val username = intent.getStringExtra("username") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""
        val referral = intent.getStringExtra("referral") ?: ""
        var code = ""

        binding.edtEmail.hint = email

        binding.btnSend.setOnClickListener()
        {

            code = (10000..999999).random().toString()
            sendMail(email, code)
            binding.btnSend.isEnabled = false

            object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.tvResend.setText("Didn't not receive code ? Click resend after " + millisUntilFinished / 1000 + " seconds.")
                    binding.tvResend.isVisible = true
                    binding.btnSend.setText("Resend")
                }

                override fun onFinish() {
                    binding.tvResend.startAnimation(animFadeOut)
                    binding.tvResend.isVisible = false
                    binding.btnSend.isEnabled = true
                }
            }.start()

        }

        binding.btnVerify2.setOnClickListener()
        {
            binding.tvButton.startAnimation(animFadeIn)
            binding.tvButton.setText("Verifying...");

            Handler(Looper.getMainLooper()).postDelayed({
                verification(code,email,password)
            }, 2000)

        }


    }

//    fun main(args: Array<String>) {
//        Transport.send(plainMail())
//    }

    fun sendMail(email: String, code: String){

        val email = email
        val message = "This is your verification code : " + code
        val subject = "Email Verification @ Bookworms"

        //send main
        val javaMainAPI = JavaMailAPI(this,email,subject,message)
        javaMainAPI.execute();

    }

    fun verification(code : String,email: String,password:String)
    {
        //Animation
        val animFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        val animFadeOut: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)

        val entered_code = binding.edtCode.editText?.text.toString().trim()

        if (entered_code == code)
        {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvButton.startAnimation(animFadeIn)
                binding.tvButton.setText("Verified");
            }, 1000)

            Registration(email,password)
        }else{
            binding.tvButton.startAnimation(animFadeIn)
            binding.tvButton.setText("Incorrect Confirmation Code.");
        }
    }

    fun Registration(email: String,password: String){

        // Initialize Firebase Auth
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    createAccount()
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }

    fun createAccount()
    {
        auth = Firebase.auth
        val user = auth.currentUser

        user?.let {
            val email = user.email?: ""
            val uid = user.uid

            val username = intent.getStringExtra("username") ?: ""
            val referral = "B" + (100000..999999).random().toString()
            val referred_by = intent.getStringExtra("referred_by") ?: ""

            val u = User(
                id    = uid,
                email = email,
                username  = username,
                referral_code  = referral,
                referred_by  = referred_by
            )

            vm.set(u)

            // Update Referrer Points
            if(referred_by != "")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    val current_earn_points = vm.get(referred_by)?.earn_points.toString().toIntOrNull() ?: 0
                    val current_usable_points = vm.get(referred_by)?.usable_points.toString().toIntOrNull() ?: 0

                    vm.updateEarnPoints(referred_by,current_earn_points,500)
                    vm.updateUsablePoints(referred_by,current_usable_points,500)
                }
            }

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }, 2000)
        }

    }

    fun showText(text : String ){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


//    private fun plainMail(): MimeMessage {
//        val tos = arrayListOf("kengboongoh@gmail.com", "kengboonogoh@gmail.com") //Multiple recipients
//        val from = "kelvingkb@gmail.com" //Sender email
//
//        val properties = System.getProperties()
//
//        with (properties) {
//            put("mail.smtp.host", "smtp.gmail.com") //Configure smtp host
//            put("mail.smtp.port", "465") //Configure port
//            put("mail.smtp.starttls.enable", "true") //Enable TLS
//            put("mail.smtp.auth", "true") //Enable authentication
//        }
//
//        val auth = object: Authenticator() {
//            override fun getPasswordAuthentication() =
//                PasswordAuthentication(from, "kengboon1109") //Credentials of the sender email
//        }
//
//        val session = Session.getDefaultInstance(properties, auth)
//
//        val message = MimeMessage(session)
//
//        with (message) {
//            setFrom(InternetAddress(from))
//            for (to in tos) {
//                addRecipient(Message.RecipientType.TO, InternetAddress(to))
//                subject = "This is the long long long subject" //Email subject
//                setContent("<html><body><h1>This is the actual message</h1></body></html>", "text/html; charset=utf-8") //Sending html message, you may change to send text here.
//            }
//        }
//
//        return message
//    }
}