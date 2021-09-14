package com.mad.mad_bookworms.admin

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Voucher
import com.mad.mad_bookworms.databinding.FragmentList2Binding
import com.mad.mad_bookworms.viewModels.VoucherViewModel


class List2Fragment : Fragment() {

    private lateinit var binding: FragmentList2Binding
    private val nav by lazy { findNavController() }
    private val vm: VoucherViewModel by activityViewModels()

    private lateinit var adapter: VoucherListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentList2Binding.inflate(inflater, container, false)

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.insert2Fragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = VoucherListAdapter() { holder, voucher ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.update2Fragment, bundleOf("id" to voucher.id))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(voucher.id) }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtCount.text = "${list.size} Voucher(s)"
        }



        return binding.root
    }



    private fun deleteAll() {
        // TODO: Delete all
        vm.deleteAll()
    }

    private fun delete(id: String) {
        // TODO: Delete
        Firebase.firestore
            .collection("Voucher")
            .document(id)
            .delete()
        vm.delete(id)
    }

}