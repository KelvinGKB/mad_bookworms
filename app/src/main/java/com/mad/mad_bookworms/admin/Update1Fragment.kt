package com.mad.mad_bookworms.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mad.mad_bookworms.cropToBlob
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.databinding.FragmentUpdate1Binding
import com.mad.mad_bookworms.errorDialog
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.BookViewModel
import kotlinx.coroutines.launch


class Update1Fragment : Fragment() {

    private lateinit var binding: FragmentUpdate1Binding
    private val nav by lazy { findNavController() }
    private val vm: BookViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id") ?: "" }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdate1Binding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }

        return binding.root
    }



    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
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

    private fun load(f: Book) {
        binding.txtId.text = f.id
        binding.edtTitle.setText(f.title)
        binding.edtAuthor.setText(f.author)
        binding.edtDesc.setText(f.description)
        binding.edtPrice.setText(f.price.toString())
        binding.edtPage.setText(f.pages.toString())
        binding.edtPoint.setText(f.requiredPoint.toString())
        binding.spnCategory.setSelection(getIndex(binding.spnCategory, f.category));
        binding.spnLanguage.setSelection(getIndex(binding.spnLanguage, f.language));
        binding.trendingCheck.setChecked(f.trending);

        // TODO: Load photo
        binding.imgPhoto.setImageBitmap(f.image.toBitmap())

        binding.edtTitle.requestFocus()

    }

    private fun submit() {
        // TODO: Update (set)
        val f = Book(
            id    = id,
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

    private fun getIndex(spinner: Spinner, myString: String):Int {
        for (i in 0 until spinner.getCount()) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                return i
                break
            }
        }
        return 0

    }

}