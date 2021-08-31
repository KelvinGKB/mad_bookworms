package com.mad.mad_bookworms.redeem

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentRedeemBinding
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RedeemFragment : Fragment() {

//    /// Hide Action Bar
//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//    }

    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    private val vm: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentRedeemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_redeem, container, false)

        val animFadeIn: Animation =
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in)
        val animFadeOut: Animation =
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out)

        binding.shimmerView.startShimmerAnimation()

        val user = Firebase.auth.currentUser

        requireActivity().runOnUiThread { // This code will always run on the UI thread, therefore is safe to modify UI elements.
            CoroutineScope(Dispatchers.IO).launch {

                val u = user?.let { vm.get(it.uid) }

                val level = u?.level
                val points = u?.usable_points.toString() + " usable points"
                val earn_points = u?.earn_points
                val priviledge = u?.level + " priviledge"
                val code = u?.referral_code
                val remaining = 0

                Handler(Looper.getMainLooper()).postDelayed({

                    binding.shimmerView.stopShimmerAnimation()
                    binding.shimmerView.visibility = View.GONE

                    binding.redeemAppBar.visibility= View.VISIBLE
                    binding.redeemScrollView.visibility = View.VISIBLE

                    binding.tvLevel.startAnimation(animFadeIn)
                    binding.tvLevel.text = level

                    binding.tvPoints.startAnimation(animFadeIn)
                    binding.tvPoints.text = points

                    if (earn_points!! < 2000) {
                        binding.levelBar.max = 2000
                        val remaining = 2000 - earn_points
                        binding.levelBar.progress = earn_points
                        binding.tvRemaining.text =
                            "earn more " + remaining + " points to upgrade gold"

                    } else if (earn_points!! < 5000) {
                        binding.levelBar.max = 5000
                        val remaining = 5000 - 2000 - earn_points
                        binding.levelBar.progress = earn_points - 2000
                        binding.tvRemaining.text =
                            "earn more " + remaining + " points to upgrade platinum"
                    }

                    binding.tvPriviledge.startAnimation(animFadeIn)
                    binding.tvPriviledge.text = priviledge

                    binding.btnCode.text = code

                    if(level == "Silver")
                    {
                        binding.itemDiscount.visibility = View.VISIBLE
                        binding.itemShipping.visibility = View.GONE
                        binding.itemVoucher.visibility = View.GONE

                    }else if(level == "Gold")
                    {
                        binding.itemDiscount.visibility = View.VISIBLE
                        binding.itemShipping.visibility = View.VISIBLE
                        binding.itemVoucher.visibility = View.GONE
                    }else{
                        binding.itemDiscount.visibility = View.VISIBLE
                        binding.itemShipping.visibility = View.VISIBLE
                        binding.itemVoucher.visibility = View.VISIBLE
                    }


                }, 500)

            }
        }


        binding.voucherBtn.setOnClickListener() {
            val myVoucherFragment = MyVoucherFragment()
            setCurrentFragment(myVoucherFragment)
        }

        binding.btnCode.setOnClickListener() {

            val id: String = binding.btnCode.text.toString()
            Toast.makeText(context, "Referal Code copied : " + id, Toast.LENGTH_LONG).show()
        }

        binding.btnLink.setOnClickListener() {

            Toast.makeText(context, "Copied", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

//    suspend fun getUser(): User? {
//        val user = Firebase.auth.currentUser
//        val u = user?.let { vm.get(it.uid) }
//
//        return u
//    }


}
