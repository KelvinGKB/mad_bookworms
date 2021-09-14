package com.mad.mad_bookworms.admin

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.databinding.ActivityAdminBinding
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Blob

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.navHost)!!.findNavController() }
    private val vm : UserViewModel by viewModels()

    private lateinit var abc: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.listFragment, R.id.list2Fragment, R.id.orderFragment),
            binding.drawerLayout
        )

        setupActionBarWithNavController(nav, abc)
        binding.navView.setupWithNavController(nav)

        binding.navView.menu.findItem(androidx.navigation.ui.R.id.custom).setOnMenuItemClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            binding.drawerLayout.close()
            true
        }

        var img: com.google.firebase.firestore.Blob
        var username: String
        var role: String

        val header = binding.navView.getHeaderView(0)
        CoroutineScope(Dispatchers.IO).launch{
            val u = vm.get(Firebase.auth.currentUser!!.uid)

            img = u?.photo!!
            username = u?.fullname
            role = u?.role

            Handler(Looper.getMainLooper()).postDelayed({

                if(img!!.toBitmap() != null){
            header.findViewById<ImageView>(R.id.photo).setImageBitmap(img!!.toBitmap())
        }else{
            header.findViewById<ImageView>(R.id.photo).setImageResource(R.drawable.user_profile_2)
        }


        header.findViewById<TextView>(R.id.name).text = username
        header.findViewById<TextView>(R.id.email).text = role

                }, 200)
//            binding.navView.getHeaderView(0).name.text = username
//            binding.navView.getHeaderView(0).email.text = role
//            if(img!!.toBitmap() != null){
//                binding.navView.getHeaderView(0).photo.setImageBitmap(img!!.toBitmap())
//            }else{
//                binding.navView.getHeaderView(0).photo.setImageResource(R.drawable.user_profile_2)
//            }

        }
//        if(img!!.toBitmap() != null){
//            header.findViewById<ImageView>(R.id.photo).setImageBitmap(img!!.toBitmap())
//        }else{
//            header.findViewById<ImageView>(R.id.photo).setImageResource(R.drawable.user_profile_2)
//        }
//
//
//        header.findViewById<TextView>(R.id.name).text = username
//        header.findViewById<TextView>(R.id.email).text = role



    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // NOTE: If SDK >= 28 -> menu?.setGroupDividerEnabled(true)
        MenuCompat.setGroupDividerEnabled(menu, true)
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.groupId != R.id.main) return false // Pass control to next level (fragment)

//        // Custom action
//        when (item.itemId) {
//            R.id.custom -> {
//                Snackbar.make(binding.root, "Hello from Activity", Snackbar.LENGTH_SHORT).show()
//                return true // Stop
//            }
//        }

        return item.onNavDestinationSelected(nav) || super.onOptionsItemSelected(item)
    }
}