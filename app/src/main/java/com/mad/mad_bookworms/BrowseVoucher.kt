package com.mad.mad_bookworms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mad.mad_bookworms.databinding.FragmentBrowseVoucherBinding

class BrowseVoucher : Fragment() {

    private lateinit var binding: FragmentBrowseVoucherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse_voucher, container, false)

        val listView = binding.browseList
        listView.adapter = BrowseVoucherListAdapter(requireActivity())

        return binding.root
    }

}