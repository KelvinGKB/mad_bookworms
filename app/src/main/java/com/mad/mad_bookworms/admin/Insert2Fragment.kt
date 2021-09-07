package com.mad.mad_bookworms.admin

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.data.Voucher
import com.mad.mad_bookworms.databinding.FragmentInsert2Binding
import com.mad.mad_bookworms.errorDialog
import com.mad.mad_bookworms.viewModels.VoucherViewModel
import kotlinx.android.synthetic.main.fragment_insert2.*


class Insert2Fragment : Fragment() {

    private lateinit var binding: FragmentInsert2Binding
    private val nav by lazy { findNavController() }
    private val vm: VoucherViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInsert2Binding.inflate(inflater, container, false)

        reset()
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }

        binding.rgpType.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.radShipping){
                binding.edtDiscount.text.clear()
                binding.edtDiscount.setEnabled(false)
                Toast.makeText(context, "Changed to Shipping Voucher", Toast.LENGTH_SHORT).show()
            }
            if(checkedId == R.id.radDiscount){
                binding.edtDiscount.setEnabled(true)
                Toast.makeText(context, "Changed to Discount Voucher", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root


    }


    private fun reset() {
        binding.rgpType.check(R.id.radShipping)
        binding.rgpLevel.check(R.id.radLevel1)
        binding.edtPoint.text.clear()
        binding.edtDiscount.text.clear()
        binding.edtPoint.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val f = Voucher(
            level  = getLevel(),
            type  = getType(),
            requiredPoint  = binding.edtPoint.text.toString().toIntOrNull() ?: 0,
            discount  = binding.edtDiscount.text.toString().toIntOrNull() ?: 0
        )

        val err = vm.validate(f)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(f)
        nav.navigateUp()
    }

    private fun getType(): String {
        return when(binding.rgpType.checkedRadioButtonId) {
            R.id.radShipping -> "1"
            R.id.radDiscount -> "2"
            else -> ""
        }
    }

    private fun getLevel(): Int {
        return when(binding.rgpLevel.checkedRadioButtonId) {
            R.id.radLevel1 -> 1
            R.id.radLevel2 -> 2
            R.id.radLevel3 -> 3
            else -> 0
        }
    }



}