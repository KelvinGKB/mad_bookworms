package com.mad.mad_bookworms.customer.bookDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentBookDetailBinding
import com.mad.mad_bookworms.databinding.FragmentExploreBinding


class BookDetailFragment : Fragment() {


    private lateinit var binding: FragmentBookDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailBinding.inflate(inflater,container,false)


        return binding.root
    }


}