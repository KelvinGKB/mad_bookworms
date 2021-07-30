package com.mad.mad_bookworms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mad.mad_bookworms.databinding.FragmentActiveVoucherBinding
import com.mad.mad_bookworms.databinding.FragmentProfileBinding

class ActiveVoucherFragment : Fragment() {

    private lateinit var binding: FragmentActiveVoucherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_active_voucher, container, false)

        val listView = binding.voucherList
        listView.adapter = VoucherListAdapter(requireActivity())

        return binding.root

    }

}