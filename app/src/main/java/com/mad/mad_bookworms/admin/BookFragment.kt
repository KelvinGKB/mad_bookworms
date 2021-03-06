package com.mad.mad_bookworms.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.databinding.FragmentBookBinding


class BookFragment : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookBinding.inflate(inflater, container, false)

        binding.btnBook.setOnClickListener { nav.navigate(R.id.listFragment) }
        binding.btnVoucher.setOnClickListener { nav.navigate(R.id.list2Fragment) }
        binding.btnSales.setOnClickListener { nav.navigate(R.id.orderFragment) }
        binding.btnFeedBack.setOnClickListener { nav.navigate(R.id.feedbackListFragment) }

        return binding.root
    }


    private fun set() {
        // TODO
//        val f = Friend("A004", "Diana", 24)
//
//        Firebase.firestore
//            .collection("friends")
//            .document(f.id)
//            .set(f)
//            .addOnSuccessListener { toast("Record inserted") }
        toast("Please Click Book above ^")

    }

    private fun update() {
        // TODO
//        Firebase.firestore
//            .collection("friends")
//            .document("A004")
//            .update("age", 99)
//            .addOnSuccessListener { toast("Record updated") }
        toast("Please Click Book above ^")

    }

    private fun delete() {
        // TODO
//        Firebase.firestore
//            .collection("friends")
//            .document("A004")
//            .delete()
//            .addOnSuccessListener { toast("Record deleted") }
        toast("Please Click Book above ^")
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}