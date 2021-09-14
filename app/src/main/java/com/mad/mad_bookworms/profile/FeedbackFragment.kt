package com.mad.mad_bookworms.profile

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.cropToBlob
import com.mad.mad_bookworms.data.Feedback
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.data.Voucher
import com.mad.mad_bookworms.databinding.FragmentFeedbackBinding
import com.mad.mad_bookworms.errorDialog
import com.mad.mad_bookworms.showEmailDialog
import com.mad.mad_bookworms.viewModels.FeedbackViewModel
import com.mad.mad_bookworms.viewModels.VoucherViewModel

class FeedbackFragment : Fragment() {

    private lateinit var binding: FragmentFeedbackBinding
    private val vm: FeedbackViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false)

        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    fun submit(){

        val f = Feedback(
            desc = binding.edtDesc.text.toString().trim(),
            type  = getType(),
            rating  = binding.ratingBar.rating.toInt(),
        )


        val err = vm.validate(f)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(f)
        Toast.makeText(context, "Submitted.Thank you for you Feedback!", Toast.LENGTH_SHORT).show()
//        nav.navigateUp()
    }

    private fun getType(): String {
        return when(binding.rgpType.checkedRadioButtonId) {
            R.id.radComment -> "1"
            R.id.radSuggest -> "2"
            R.id.radQuestion -> "3"
            else -> ""
        }
    }

}