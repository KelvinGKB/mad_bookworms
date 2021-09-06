package com.mad.mad_bookworms.redeem

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.customer.explore.RecyclerAdapter
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.MyVoucher
import com.mad.mad_bookworms.databinding.FragmentActiveVoucherBinding
import com.mad.mad_bookworms.showUseDialog
import com.mad.mad_bookworms.viewModels.BookViewModel
import com.mad.mad_bookworms.viewModels.UserVoucherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ActiveVoucherFragment : Fragment() {

    private lateinit var binding: FragmentActiveVoucherBinding
    private val vm: UserVoucherViewModel by activityViewModels()

    private  lateinit var adapter: UserVoucherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_active_voucher, container, false)

        val voucherList: MutableList<MyVoucher> = ArrayList()

        // Setup refresh listener which triggers new data loading
        binding.swipeContainer.setOnRefreshListener {

            loadUserVoucher(voucherList)
            binding.swipeContainer.setRefreshing(false)
        }


        binding.shimmerView.startShimmerAnimation()

        //Recycler View for user voucher list
        adapter = UserVoucherAdapter(){ holder, voucher ->
            // Item click
            holder.itemButton.setOnClickListener {

                showUseDialog(activity,voucher.id)

            }
        }
        binding.rvUserVoucherList.adapter = adapter
        binding.rvUserVoucherList.setHasFixedSize(true)

        Log.w(ContentValues.TAG, "Referral : " + " HI")
        loadUserVoucher(voucherList)

        return binding.root

    }

    fun loadUserVoucher(voucherList : MutableList<MyVoucher>) {

        //load all the voucher data from the firebase
        val uid = Firebase.auth.currentUser?.uid

        voucherList.clear() // clear list
        adapter.notifyDataSetChanged() // let your adapter know about the changes and reload view.

        if (uid != null) {
            vm.getAll().observe(viewLifecycleOwner) { list ->
                Log.w(ContentValues.TAG, "Voucher : " + list.toString())

                for(voucher in list) {
                    if (voucher.uid == uid && !voucher.expiry_date.before(Calendar.getInstance().time) && voucher.status =="active"){
                        voucherList.add(voucher)
                    }
                }

                adapter.submitList(voucherList)

                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE

                binding.swipeContainer.visibility = View.VISIBLE

            }
        }
    }

//    fun clearData() {
//        myList.clear() // clear list
//        mAdapter.notifyDataSetChanged() // let your adapter know about the changes and reload view.
//    }

}