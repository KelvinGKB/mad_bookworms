package com.mad.mad_bookworms.customer.bookDetail

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.*
import com.mad.mad_bookworms.customer.payment.RazorPaySuccess
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.LocalDB
import com.mad.mad_bookworms.data.MyCartDao
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.viewModels.CartOrderViewModel
import com.mad.mad_bookworms.viewModels.UserViewModel
import com.mad.mad_bookworms.viewModels.UserVoucherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class BookDetailActivity : AppCompatActivity() {

    lateinit var drawable: Drawable
    lateinit var bookImage: ImageView
    lateinit var bitmap: Bitmap
    private var bookID = ""
    private var isShared = false
    private lateinit var binding: ActivityBookDetailBinding
    private val vm: BookViewModel by viewModels()
    private val cartVm: CartOrderViewModel by viewModels()
    private val userVm: UserViewModel by viewModels()
    private lateinit var dao: MyCartDao
    private lateinit var tempBook: Book
    var image: Bitmap? = null

    val data: MutableList<MyCartTable> = ArrayList()

    var permissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    //To set the base language of the system
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LoacalHelper.setLocale(newBase!!))
    }

    /// Hide Action Bar
    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }

    override fun onRestart() {
        super.onRestart()
        increasePoint()
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra("bookID", this.bookID)
        intent.putExtra("isShared", true)
        startActivity(intent)
        //Toast.makeText(this, "Im back", Toast.LENGTH_SHORT).show()


    }

    private fun increasePoint() {
        if(!isShared){
            val uid = Firebase.auth.currentUser?.uid

            if (uid != null) {
                lifecycleScope.launch {
                    Log.d("TAG","UID NOT NULL")
                    val u = userVm.get(uid)

                    if (u!=null){
                        var currentEarnPoint = u.earn_points.toInt()
                        var currentUsablePoint = u.usable_points.toInt()
                        Log.d("TAG","User NOT NULL")
                        userVm.addCashBackPoints(uid,currentEarnPoint, currentUsablePoint,0.00, "share_earn")

                        Toast.makeText(this@BookDetailActivity, "You have successfully earn 5 sharing points", Toast.LENGTH_SHORT).show()

                    }

                }

            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check if the user already shared
        val isShared = intent.getBooleanExtra("isShared",false)
        this.isShared = isShared

        //bind all the data
        val bookID = intent.getStringExtra("bookID") ?: ""
        this.bookID = bookID
        load(bookID)

        bookImage = binding.imageBook

        //button onclick event
        binding.btnMinus.setOnClickListener { decreaseQty() }
        binding.btnIncrease.setOnClickListener { increaseQty() }
        binding.btnAddToCart.setOnClickListener { addToCart() }
        binding.btnCart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("cart", "cart")
            startActivity(intent)
        }

        binding.btnShare.setOnClickListener {
            val image: Bitmap? = getBitmapfromView(binding.imageBook)
            this.image = image

            checkPermission()
            CoroutineScope(IO).launch { share() }

        }

        cartVm.getAll().observe(this) { list ->
            data.addAll(list)
        }


    }

    private fun addToCart() {
        val uid = Firebase.auth.currentUser?.uid
        val b = uid?.let {
            MyCartTable(
                0, it, bookId = tempBook.id, quantity = binding.edtQty.text.toString().toInt()
            )
        }
        val order: MutableList<MyCartTable> = ArrayList()
        var cartOrder: MyCartTable
        Log.d("TAG", "${data}")

        CoroutineScope(IO).launch {
            var c = uid?.let { cartVm.get(it,tempBook.id) }
            Log.d("TAG", "$c")
            if (c != null) {
                if (c.isNotEmpty()){
                    Log.d("TAG", "im in notempty")
                    for (cart in c){
                        if (cart.bookId == tempBook.id){
                            Log.d("TAG", "im in notempty")
                            if (uid != null) {
                                cartVm.updateQty(uid, tempBook.id, cart.quantity+ binding.edtQty.text.toString().toInt())

                            }
                        }
                    }

                }else{
                    Log.d("TAG", "im in empty")
                    if (b != null) {
                        cartVm.insert(b)

                    }
                }
            }
        }
        Toast.makeText(applicationContext, getString(R.string.added_cart_successfully_message), Toast.LENGTH_SHORT).show()
    }

    private fun load(bookID: String) {
        lifecycleScope.launch {

            val f = vm.get(bookID)
            Log.d("TAG", "${bookID}")
            Log.d("TAG", "${f}")
            if (f != null) {
                with(binding) {
                    tempBook = f
                    tvBookTitle.text = f.title
                    tvBookAuthor.text = f.author
                    tvBookDescription.text = f.description
                    tvBookPrice.text = "RM" + "%.2f".format(f.price)
                    tvBookLanguage.text = f.language
                    tvBookPages.text = f.pages.toString()
                    imageBook.setImageBitmap(f.image.toBitmap())
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            when {
                grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED ->{
                    CoroutineScope(IO).launch { share() }
                }
            }
        }
    }


    private fun checkPermission(){
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions){
            result = ContextCompat.checkSelfPermission(this,p)
            if (result != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                100
            )
        }
    }


    private suspend fun share() =
//        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
//        StrictMode.setVmPolicy(builder.build())
        coroutineScope {
            val uid = Firebase.auth.currentUser?.uid

            val u = userVm.get(uid!!)

            val id = u?.referral_code.toString()

            val bookTitle = binding.tvBookTitle.text

            val link = "http://www.mad_bookworm.com/referral_register/" + id

            val text = "Check Out $bookTitle. Get It on BookWorn now!! \n\n" +
                    "Join the Bookworms now with the links Below to earn points ! \n\n" +
                    "*** Use TARUC student email will earn extra 50% points ! *** \n\n" +
                    link

            val image: Bitmap? = getBitmapfromView(binding.imageBook)
            drawable = bookImage.drawable
            bitmap = drawable.toBitmap()

            val sendIntent = Intent(Intent.ACTION_SEND)

            sendIntent.type = "image/*"
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(this@BookDetailActivity, image!!)) //add image path

            startActivity(Intent.createChooser(sendIntent, "Share book using"))
            startActivity(sendIntent)
        }



    private fun getImageUri(bookDetailActivity: BookDetailActivity, image: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            bookDetailActivity.contentResolver,
            image,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun getBitmapfromView(imageBook: ImageView): Bitmap? {
        val bitmap = Bitmap.createBitmap(imageBook.width, imageBook.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        imageBook.draw(canvas)
        return bitmap
    }

    private fun increaseQty() {
        display(binding.edtQty.text.toString().toInt() + 1)
    }

    private fun decreaseQty() {
        display(binding.edtQty.text.toString().toInt() - 1)
    }

    private fun display(i: Int) {
        var qty = i
        if (qty < 1) {
            qty = 1
        }
        binding.edtQty.setText("$qty")
    }

}