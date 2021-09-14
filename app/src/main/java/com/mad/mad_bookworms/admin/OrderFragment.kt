package com.mad.mad_bookworms.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentOrderBinding
import com.mad.mad_bookworms.viewModels.OrderViewModel
import java.text.SimpleDateFormat


class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val nav by lazy { findNavController() }
    private val vm: OrderViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        vm.search("")
//        sort("id")


        val adapter = OrderAdapter() { holder, order ->
            holder.root.setOnClickListener {
                nav.navigate(R.id.orderDetailFragment, bundleOf("id" to order.id))
            }
            holder.btnDelete.setOnClickListener { delete(order.id) }

        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { order ->
            adapter.submitList(order)
            binding.txtCount.text = "${order.size} Order(s)"
        }

        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                // TODO(19): Search by [name] -> vm.search(...)
                vm.search(name)
                return true
            }
        })

//        binding.btnId.setOnClickListener { sort("id") }
        binding.btnType.setOnClickListener { sort("paymentType") }
        binding.btnDate.setOnClickListener { sort("dateTime") }
        binding.btnAmount.setOnClickListener { sort("amount") }

        return binding.root

    }

    private fun sort(field: String) {
        // TODO(26): Sort by [field] -> vm.sort(...)
        val reverse = vm.sort(field)

        // TODO(27): Remove icon -> all buttons
//        binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnType.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        // TODO(28): Set icon -> specific button
        val res = if (reverse) R.drawable.ic_down else R.drawable.ic_up

        when (field) {
//            "id"    -> binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "paymentType"  -> binding.btnType.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "dateTime"  -> binding.btnDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "amount" -> binding.btnAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
        }
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }



}