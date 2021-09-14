package com.mad.mad_bookworms.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.ActivityChangeLanguageBinding
import com.mad.mad_bookworms.LoacalHelper
import com.mad.mad_bookworms.MainActivity

class ChangeLanguageActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChangeLanguageBinding


    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvEnglish.setOnClickListener {
            LoacalHelper.setNewLocale(this,"en")
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            Toast.makeText(this, "Successfully Change to English", Toast.LENGTH_LONG).show()
        }

        binding.tvChinese.setOnClickListener {
            LoacalHelper.setNewLocale(this,"zh")
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            Toast.makeText(this, "Successfully Change to Chinese", Toast.LENGTH_LONG).show()
        }
    }
}