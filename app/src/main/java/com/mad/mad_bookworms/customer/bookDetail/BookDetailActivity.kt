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
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import com.mad.mad_bookworms.viewModels.BookViewModel
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mad.mad_bookworms.MainActivity
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.LocalDB
import com.mad.mad_bookworms.data.MyCartDao
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.viewModels.CartOrderViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class BookDetailActivity : AppCompatActivity() {

    lateinit var drawable : Drawable
    lateinit var bookImage : ImageView
    lateinit var bitmap : Bitmap
    private lateinit var binding : ActivityBookDetailBinding
    private val vm: BookViewModel by viewModels()
    private val cartVm: CartOrderViewModel by viewModels()
    private lateinit var dao: MyCartDao
    private lateinit var tempBook : Book

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        dao = LocalDB.getInstance(application).MyCartDao

        //bind all the data
        val bookID = intent.getStringExtra("bookID") ?: ""
        load(bookID)

        bookImage = binding.imageBook

        //button onclick event
        binding.btnMinus.setOnClickListener{ decreaseQty() }
        binding.btnIncrease.setOnClickListener{ increaseQty() }
        binding.btnAddToCart.setOnClickListener{ addToCart() }

        binding.btnShare.setOnClickListener{
            share()
        }



    }

    private fun addToCart() {
        val b = MyCartTable(bookId = tempBook.id,  quantity = binding.edtQty.text.toString().toInt())

        cartVm.insert(b)
    }

    private fun load(bookID: String) {
        lifecycleScope.launch {
            val f = vm.get(bookID)
            Log.d("TAG","${bookID}")
            Log.d("TAG","${f}")
            if (f!=null) {
                with(binding){
                    tempBook = f
                    tvBookTitle.text = f.title
                    tvBookAuthor.text = f.author
                    tvBookDescription.text = f.description
                    tvBookPrice.text = "RM" + "%.2f".format(f.price)
                    tvBookLanguage.text = f.language
                    tvBookPages.text = f.pages.toString()
                    //imageBook.setImageResource(bookImage)
                }
            }
        }


//        if (f == null) {
//            val intent = Intent(this, MainActivity::class.java)
//
//            startActivity(intent)
//        }


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