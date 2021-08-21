package com.mad.mad_bookworms.customer.bookDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    private lateinit var binding : ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMinus.setOnClickListener{ decreaseQty() }
        binding.btnIncrease.setOnClickListener{ increaseQty() }

        //binding data
        val bookID = intent.getStringExtra("bookID") ?: ""
        val bookTitle = intent.getStringExtra("bookTitle") ?: ""
        val bookAuthor = intent.getStringExtra("bookAuthor") ?: ""
        val bookDescription = intent.getStringExtra("bookDescription") ?: ""
        val bookPrice = intent.getDoubleExtra("bookPrice", 0.0)
        val requiredPoint = intent.getIntExtra("requiredPoint",0)
        val category = intent.getStringExtra("category") ?: ""
        val language = intent.getStringExtra("language") ?: ""
        val pages = intent.getIntExtra("pages",0)
        val bookImage = intent.getIntExtra("bookImage",0)

        with(binding){
            tvBookTitle.text = bookTitle
            tvBookAuthor.text = bookAuthor
            tvBookDescription.text = bookDescription
            tvBookPrice.text = "RM" + "%.2f".format(bookPrice)
            tvBookLanguage.text = language
            tvBookPages.text = pages.toString()
            imageBook.setImageResource(bookImage)
        }
    }

    private fun increaseQty() {
        display( binding.edtQty.text.toString().toInt() + 1)
    }

    private fun decreaseQty() {
        display( binding.edtQty.text.toString().toInt() - 1)
    }

    private fun display(i: Int) {
        var qty = i
        if(qty < 1){ qty = 1 }
        binding.edtQty.setText("$qty")
    }

}