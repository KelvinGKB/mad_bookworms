package com.mad.mad_bookworms.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentList1Binding
import com.mad.mad_bookworms.viewModels.BookViewModel

class List1Fragment : Fragment() {

    private lateinit var binding: FragmentList1Binding
    private val nav by lazy { findNavController() }
    private val vm: BookViewModel by activityViewModels()

    private lateinit var adapter: BookAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentList1Binding.inflate(inflater, container, false)

        vm.search("")


        binding.btnInsert.setOnClickListener { nav.navigate(R.id.insertFragment) }

        adapter = BookAdapter() { holder, book ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.updateFragment, bundleOf("id" to book.id))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(book.id) }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtCount.text = "${list.size} book(s)"
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
        binding.btnId.setOnClickListener { sort("id") }
        binding.btnName.setOnClickListener { sort("title") }
        binding.btnPrice.setOnClickListener { sort("price") }

        return binding.root
    }

    private fun sort(field: String) {
        // TODO(26): Sort by [field] -> vm.sort(...)
        val reverse = vm.sort(field)

        // TODO(27): Remove icon -> all buttons
//        binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        // TODO(28): Set icon -> specific button
        val res = if (reverse) R.drawable.ic_down else R.drawable.ic_up

        when (field) {
//            "id"    -> binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "id"  -> binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "title"  -> binding.btnName.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "price" -> binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
        }
    }


    private fun delete(id: String) {
        // TODO: Delete
        Firebase.firestore
            .collection("books")
            .document(id)
            .delete()
        vm.delete(id)
    }


}