package com.mad.mad_bookworms.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mad.mad_bookworms.MyAdapter
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.ReferralAdapter
import com.mad.mad_bookworms.databinding.FragmentMyVoucherBinding
import com.mad.mad_bookworms.databinding.FragmentReferralBinding
import com.mad.mad_bookworms.redeem.RedeemFragment

class ReferralFragment : Fragment() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    private lateinit var binding: FragmentReferralBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_referral, container, false)

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        tabLayout.addTab(tabLayout.newTab().setText("Invited User"))
        tabLayout.addTab(tabLayout.newTab().setText("History"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = ReferralAdapter(this, childFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.toolbar.setOnClickListener(){
            val profileFragment = ProfileFragment()
            setCurrentFragment(profileFragment)
        }

        return binding.root
    }
    private fun setCurrentFragment(fragment: Fragment)=
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }


}