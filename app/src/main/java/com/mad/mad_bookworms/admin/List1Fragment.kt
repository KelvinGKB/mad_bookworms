package com.mad.mad_bookworms.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.insertFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

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

        return binding.root
    }

    private fun deleteAll() {
        // TODO: Delete all
//        vm.deleteAll()
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