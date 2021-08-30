package com.mad.mad_bookworms

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mad.mad_bookworms.databinding.FragmentActiveVoucherBinding
import com.mad.mad_bookworms.databinding.FragmentProfileBinding
import com.mad.mad_bookworms.security.LoginActivity

class ActiveVoucherFragment : Fragment() {

    private lateinit var binding: FragmentActiveVoucherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_active_voucher, container, false)

        binding.shimmerView.startShimmerAnimation()


        Handler(Looper.getMainLooper()).postDelayed({

            binding.shimmerView.stopShimmerAnimation()
            binding.shimmerView.visibility = View.GONE

            binding.voucherList.visibility = View.VISIBLE

            val listView = binding.voucherList
            listView.adapter = VoucherListAdapter(requireActivity())

        }, 2000)

        return binding.root

    }

}