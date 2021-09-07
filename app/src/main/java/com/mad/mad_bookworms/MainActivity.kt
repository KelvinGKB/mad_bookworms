package com.mad.mad_bookworms

import com.mad.mad_bookworms.profile.ProfileFragment
import com.mad.mad_bookworms.redeem.RedeemFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.customer.cart.CartFragment
import com.mad.mad_bookworms.customer.explore.ExploreFragment
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.viewModels.CartOrderViewModel
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.badge_menu_item.view.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {


    private val vm: UserViewModel by viewModels()
    private lateinit var notificationBadges: View
    private lateinit var nullBadge: View
    private var count: Int = 0
    private val cartVm: CartOrderViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        val uid = Firebase.auth.currentUser?.uid

        if (uid != null) {
            lifecycleScope.launch {
                var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val currentDate = formatter.format(Date())
                var today = Date()

                today = formatter.parse(currentDate); // Date with 0:00:00

                val u = vm.get(uid)

                if (u != null) {
                    val title = "Don't miss out daily check in rewards!"
                    val text = "Go to Profile -> Daily Check In to complete your check in!"

                    //Check is this user already check in
                    if (u.checkInDate.before(today)) {
                        showMultiuseDialog(this@MainActivity,4, title, text)
                    }

                }
            }
        }
    }

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
        setContentView(R.layout.activity_main)


        val r = intent.getStringExtra("cart") ?: ""

        val exploreFragment = ExploreFragment()
        val cartFragment = CartFragment()
        val redeemFragment = RedeemFragment()
        val profileFragment = ProfileFragment()

        if (r == "cart") {
            setCurrentFragment(cartFragment)
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).getMenu().getItem(2)
                .setChecked(true);
        } else {
            setCurrentFragment(exploreFragment)
        }

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.explore -> setCurrentFragment(exploreFragment)
                R.id.redeem -> setCurrentFragment(redeemFragment)
                R.id.cart -> setCurrentFragment(cartFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }

        cartVm.getAll().observe(this) { myCart ->
            val o: MutableList<MyCartTable> = ArrayList()
            val uid = Firebase.auth.currentUser?.uid
            if (uid != null) {
                for (c in myCart) {
                    if (c.uid == uid) {

                        o.add(c)
                    }
                    var totalQty = 0
                    for (c in o) {
                        totalQty += c.quantity
                    }
                    updateBadgeCount(totalQty)

                }

            }
        }

    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    private fun updateBadgeCount(count: Int) {
        //at which index of bottom navigation
        val itemView: BottomNavigationView? = bottomNavigationView?.getChildAt(0) as? BottomNavigationView
        Log.d("TAG", "Hellooo")

        //layout inflating for badge count view
        notificationBadges = LayoutInflater.from(this).inflate(R.layout.badge_menu_item, itemView, true)
        nullBadge = LayoutInflater.from(this).inflate(R.layout.null_badge, itemView, true)

        //set text count
        notificationBadges?.notification_badge?.text = count.toString()

        //add the layout to bottom navigation
        if (count < 1 ){
            Log.d("TAG", "nullBadge")
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.removeView(notificationBadges)
        }
        else {
            Log.d("TAG", "NotnullBadge")
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.addView(notificationBadges)
        }

    }


}