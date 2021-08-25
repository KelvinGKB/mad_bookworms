package com.mad.mad_bookworms.customer.bookDetail

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.ActivityBookDetailBinding
import android.widget.Toast

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class BookDetailActivity : AppCompatActivity() {

    lateinit var drawable : Drawable
    lateinit var bookImage : ImageView
    lateinit var bitmap : Bitmap

    var permissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

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

        bookImage = binding.imageBook

        binding.btnMinus.setOnClickListener{ decreaseQty() }
        binding.btnIncrease.setOnClickListener{ increaseQty() }

        binding.btnShare.setOnClickListener{
            share()
        }

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




    private fun share() {
//        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
        val image: Bitmap? = getBitmapfromView(binding.imageBook)
        drawable = bookImage.drawable
        bitmap = drawable.toBitmap()

        val intent = Intent(Intent.ACTION_SEND)
        val bookTitle = binding.tvBookTitle.text
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, "Check Out $bookTitle. Get It on BookWorn now!!")
        intent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, image!!)) //add image path

        startActivity(Intent.createChooser(intent, "Share image using"))
        try {
            this.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "Software not been installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUri(bookDetailActivity: BookDetailActivity, image: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(bookDetailActivity.contentResolver, image, "Title", null)
        return Uri.parse(path)
    }

    private fun getBitmapfromView(imageBook: ImageView): Bitmap? {
        val bitmap = Bitmap.createBitmap(imageBook.width, imageBook.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        imageBook.draw(canvas)
        return bitmap
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