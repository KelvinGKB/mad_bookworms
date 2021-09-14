package com.mad.mad_bookworms.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentFeedbackListBinding
import com.mad.mad_bookworms.viewModels.FeedbackViewModel

class FeedbackListFragment : Fragment() {


    private lateinit var binding: FragmentFeedbackListBinding
    private val nav by lazy { findNavController() }
    private val vm: FeedbackViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedbackListBinding.inflate(inflater, container, false)

        val adapter = FeedbackAdapter()

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { Feedback ->
            adapter.submitList(Feedback)
            binding.txtCount.text = "${Feedback.size} Feedback(s)"
        }

        return binding.root
    }

}