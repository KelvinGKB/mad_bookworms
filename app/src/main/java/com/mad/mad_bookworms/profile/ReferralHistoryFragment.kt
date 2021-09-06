package com.mad.mad_bookworms.profile

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.MyVoucher
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.databinding.FragmentHistoryBinding
import com.mad.mad_bookworms.redeem.UserVoucherAdapter
import com.mad.mad_bookworms.security.LoginActivity
import com.mad.mad_bookworms.showUseDialog
import com.mad.mad_bookworms.viewModels.ReferralViewModel
import com.mad.mad_bookworms.viewModels.UserViewModel
import com.mad.mad_bookworms.viewModels.UserVoucherViewModel
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val vm: ReferralViewModel by activityViewModels()
    private val user_vm: UserViewModel by activityViewModels()

    private lateinit var adapter: ReferralHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_history, container, false)

        val referralList: MutableList<User> = ArrayList()

        // Setup refresh listener which triggers new data loading
        binding.swipeContainer.setOnRefreshListener {

            loadHistory(referralList)
            binding.swipeContainer.setRefreshing(false)
        }

        binding.shimmerView.startShimmerAnimation()

        //Recycler View for user voucher list
        adapter = ReferralHistoryAdapter(){ holder, voucher ->
            // Item click
//            holder.itemButton.setOnClickListener {
//
//                showUseDialog(activity,voucher.id)
//
//            }
        }

        binding.rvReferralUserList.adapter = adapter
        binding.rvReferralUserList.setHasFixedSize(true)

        loadHistory(referralList)

        return binding.root
    }

    fun loadHistory(referralList : MutableList<User>) {

        //load all the voucher data from the firebase
        val uid = Firebase.auth.currentUser?.uid

//        val user : MutableList<User> = ArrayList()
//
        referralList.clear() // clear list
        adapter.notifyDataSetChanged() // let your adapter know about the changes and reload view.

        if(uid != null){
            vm.getAll().observe(viewLifecycleOwner) { list ->
                Log.w(ContentValues.TAG, "User : " + list.toString())

                for(user in list) {

                    user_vm.getAll().observe(viewLifecycleOwner) { user_list ->
                        for(u in user_list) {
                            if (user.referred_by == u.id){
                                referralList.add(u)
                            }
                        }
                    }
                }

                adapter.submitList(referralList)

                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE

                binding.swipeContainer.visibility = View.VISIBLE

            }
        }
    }

}