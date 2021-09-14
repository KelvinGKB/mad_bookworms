package com.mad.mad_bookworms.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.cropToBlob
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.Voucher
import com.mad.mad_bookworms.databinding.FragmentUpdate2Binding
import com.mad.mad_bookworms.errorDialog
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.VoucherViewModel
import kotlinx.coroutines.launch

class Update2Fragment : Fragment() {

    private lateinit var binding: FragmentUpdate2Binding
    private val nav by lazy { findNavController() }
    private val vm: VoucherViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id") ?: "" }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdate2Binding.inflate(inflater, container, false)

        reset()
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }

        binding.rgpType.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.radShipping){
                binding.edtDiscount.text.clear()
                binding.edtDiscount.setEnabled(false)
                Toast.makeText(context, " Shipping Voucher", Toast.LENGTH_SHORT).show()
            }
            if(checkedId == R.id.radDiscount){
                binding.edtDiscount.setEnabled(true)
                Toast.makeText(context, "Discount Voucher", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root

    }

    private fun reset() {
        // TODO: Load data
        lifecycleScope.launch {
            val f = vm.get(id)

            if (f == null) {
                nav.navigateUp()
            }
            else if (f != null) {
                load(f)
            }
        }
    }

    private fun load(f: Voucher) {
        binding.rgpType.check(setType(f.type))
        binding.rgpLevel.check(setLevel(f.level))
        binding.edtPoint.setText(f.requiredPoint.toString())
        binding.edtDiscount.setText(f.discount.toString())
        binding.edtPoint.requestFocus()

    }

    private fun submit() {
        // TODO: Update (set)
        val f = Voucher(
            id = id,
            level  = getLevel(),
            type  = getType(),
            requiredPoint  = binding.edtPoint.text.toString().toIntOrNull() ?: 0,
            discount  = binding.edtDiscount.text.toString().toIntOrNull() ?: 0
        )

        val err = vm.validate(f, false)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(f)
        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)
        nav.navigateUp()
    }


    private fun setType(f: String): Int {
        return when(f) {
            "1" -> R.id.radShipping
            "2" -> R.id.radDiscount
            else -> 0
        }
    }

    private fun setLevel(f: Int): Int {
        return when(f) {
            1 ->R.id.radLevel1
            2 ->R.id.radLevel2
            3 ->R.id.radLevel3
            else -> 0
        }
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