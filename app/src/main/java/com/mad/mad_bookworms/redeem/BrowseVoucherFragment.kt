package com.mad.mad_bookworms.redeem

import android.content.ContentValues
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
import com.mad.mad_bookworms.data.MyVoucher
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.data.Voucher
import com.mad.mad_bookworms.databinding.FragmentBrowseVoucherBinding
import com.mad.mad_bookworms.showMultiuseDialog
import com.mad.mad_bookworms.viewModels.UserViewModel
import com.mad.mad_bookworms.viewModels.UserVoucherViewModel
import com.mad.mad_bookworms.viewModels.VoucherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class BrowseVoucherFragment : Fragment() {

    private lateinit var binding: FragmentBrowseVoucherBinding
    private val vm: VoucherViewModel by activityViewModels()
    private val userVoucher_vm: UserVoucherViewModel by activityViewModels()
    private val user_vm: UserViewModel by activityViewModels()

    private lateinit var adapter: VoucherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_browse_voucher, container, false
        )

        val voucherList: MutableList<Voucher> = ArrayList()

        // Setup refresh listener which triggers new data loading
        binding.swipeContainer2.setOnRefreshListener {

            loadVoucher(voucherList)
            binding.swipeContainer2.setRefreshing(false)
        }

        binding.shimmerView.startShimmerAnimation()

        //Recycler View for user voucher list
        adapter = VoucherAdapter() { holder, voucher ->
            // Item click
            holder.itemButton.setOnClickListener {


                val user = Firebase.auth.currentUser
                var u : User ?
                CoroutineScope(Dispatchers.IO).launch {

                    u = user?.let { user_vm.get(it.uid) }

                    val level = u?.level
                    val points = u?.usable_points

                    Handler(Looper.getMainLooper()).postDelayed({

                        val title = getString(R.string.level_not_eligible)
                        val content = getString(R.string.earn_more_join_benefits)

                        if (level == "Silver" && (voucher.level == 2 || voucher.level == 3)) {

                            showMultiuseDialog(activity,1,title,content)
                            return@postDelayed

                        } else if (level == "Gold" && voucher.level == 3) {

                            showMultiuseDialog(activity,1,title,content)
                            return@postDelayed
                        }

                        if(points!! < voucher.requiredPoint)
                        {
                            val title = getString(R.string.oh_no)
                            val content = getString(R.string.not_enough_points)

                            showMultiuseDialog(activity,3,title,content)

                        }else{
//                            val remaining_point = points - voucher.requiredPoint
                            val voucher_code = getVoucherCode(6)
                            val type = voucher.type
                            val discount = voucher.discount
                            val uid = user!!.uid

                            val currentDate = Date()

                            // convert date to calendar
                            val c = Calendar.getInstance()
                            c.setTime(currentDate);
                            c.add(Calendar.DATE, 14);

                            // convert calendar to date
                            val expiry_day= c.time

                            c.time = currentDate
                            val v = MyVoucher(
                                id    = voucher_code,
                                discount = discount ,
                                type =  type,
                                uid  = uid,
                                status = "active",
                                expiry_date = expiry_day
                            )

                            userVoucher_vm.set(v)
                            user_vm.updateUsablePoints(uid,points,-(voucher.requiredPoint))

                            val title = getString(R.string.voucher_claim_successfully)
                            val content = getString(R.string.use_within_14days)

                            showMultiuseDialog(activity,2,title,content)

                        }

                    }, 500)

                }
            }
        }

        binding.rvVoucherList.adapter = adapter
        binding.rvVoucherList.setHasFixedSize(true)

        loadVoucher(voucherList)

        return binding.root
    }

    fun loadVoucher(voucherList : MutableList<Voucher>)
    {

        //load all the voucher data from the firebase
        val uid = Firebase.auth.currentUser?.uid

        voucherList.clear() // clear list
        adapter.notifyDataSetChanged() // let your adapter know about the changes and reload view.

        if (uid != null) {
            vm.getAll().observe(viewLifecycleOwner) { list ->
                Log.w(ContentValues.TAG, "Voucher : " + list.toString())

                adapter.submitList(list)

                binding.shimmerView.stopShimmerAnimation()
                binding.shimmerView.visibility = View.GONE

                binding.swipeContainer2.visibility = View.VISIBLE

            }
        }
    }

    fun getVoucherCode(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}