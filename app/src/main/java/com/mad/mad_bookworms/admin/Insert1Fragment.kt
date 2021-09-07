package com.mad.mad_bookworms.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.cropToBlob
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.databinding.FragmentInsert1Binding
import com.mad.mad_bookworms.errorDialog
import com.mad.mad_bookworms.viewModels.BookViewModel

class Insert1Fragment : Fragment() {

    private lateinit var binding: FragmentInsert1Binding
    private val nav by lazy { findNavController() }
    private val vm: BookViewModel by activityViewModels()


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInsert1Binding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        binding.edtId.text.clear()
        binding.edtTitle.text.clear()
        binding.edtAuthor.text.clear()
        binding.edtDesc.text.clear()
        binding.edtPrice.text.clear()
        binding.edtPage.text.clear()
        binding.edtPoint.text.clear()
        binding.spnCategory.setSelection(0);
        binding.spnLanguage.setSelection(0);
        binding.trendingCheck.setChecked(false);
        binding.imgPhoto.setImageDrawable(null)
        binding.edtId.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val f = Book(
            id    = binding.edtId.text.toString().trim().uppercase(),
            title  = binding.edtTitle.text.toString().trim(),
            author  = binding.edtAuthor.text.toString().trim(),
            description  = binding.edtDesc.text.toString().trim(),
            price  = binding.edtPrice.text.toString().toDoubleOrNull() ?: 0.0,
            pages  = binding.edtPage.text.toString().toIntOrNull() ?: 0,
            requiredPoint  = binding.edtPoint.text.toString().toIntOrNull() ?: 0,
            category = binding.spnCategory.selectedItem as String,
            language = binding.spnLanguage.selectedItem as String,
            trending = binding.trendingCheck.isChecked,
            image = binding.imgPhoto.cropToBlob(130, 200),
        )

        val err = vm.validate(f)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(f)
        nav.navigateUp()
    }

}