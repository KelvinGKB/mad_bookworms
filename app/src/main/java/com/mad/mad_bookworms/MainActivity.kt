package com.mad.mad_bookworms

import com.mad.mad_bookworms.profile.ProfileFragment
import com.mad.mad_bookworms.redeem.RedeemFragment
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mad.mad_bookworms.customer.cart.CartFragment
import com.mad.mad_bookworms.customer.explore.ExploreFragment


class MainActivity : AppCompatActivity() {

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

        val r = intent.getStringExtra("cart")?:""

        val exploreFragment= ExploreFragment()
        val cartFragment= CartFragment()
        val redeemFragment= RedeemFragment()
        val profileFragment= ProfileFragment()

        if (r == "cart") {
            setCurrentFragment(cartFragment)
            findViewById<BottomNavigationView>(R.id.bottomNavigationView).getMenu().getItem(1).setChecked(true);
        }
        else{
            setCurrentFragment(exploreFragment)
        }

       findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.explore->setCurrentFragment(exploreFragment)
                R.id.redeem->setCurrentFragment(redeemFragment)
                R.id.cart->setCurrentFragment(cartFragment)
                R.id.profile->setCurrentFragment(profileFragment)
            }
            true
        }

    }


    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }


}