package com.mad.mad_bookworms.customer.explore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentExploreBinding
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.BadgeDrawable
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.viewModels.BookViewModel
import com.mad.mad_bookworms.viewModels.CartOrderViewModel
import com.mad.mad_bookworms.viewModels.UserViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class ExploreFragment : Fragment() {

    private lateinit var bitmap: Bitmap
    private lateinit var drawable: Drawable
    private lateinit var bookImage: ImageView
    private lateinit var binding: FragmentExploreBinding
    private val vm: BookViewModel by activityViewModels()
    private val userVm: UserViewModel by activityViewModels()
    private val cartVm: CartOrderViewModel by activityViewModels()

    private  lateinit var adapter: RecyclerAdapter
    private  lateinit var trendingAdapter: TrendingAdapter
    var image: Bitmap? = null
    private var bookTitle = ""

    var permissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder> ?= null

    override fun onStop() {
        super.onStop()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater,container,false)

        binding.searchView.clearFocus()

        val data: MutableList<Book> = ArrayList()
        val displayBook: MutableList<Book> = ArrayList()
        val trendingBook: MutableList<Book> = ArrayList()

        //Recycler View for book list
        adapter = RecyclerAdapter(){ holder, book ->
            // Item click
            holder.root.setOnClickListener {

                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("bookID",book.id)

                startActivity(intent)
            }

        }
        binding.rvBookList.adapter = adapter
        binding.rvBookList.setHasFixedSize(true)

        //Recycler View for Trending list
        trendingAdapter = TrendingAdapter(){ holder, book ->
            // Item click
            holder.root.setOnClickListener {

//                nav.navigate(R.id.updateFragment, bundleOf("id" to friend.id))


                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("bookID",book.id)

                startActivity(intent)
            }
            holder.btnAddToCart.setOnClickListener {
                val uid = Firebase.auth.currentUser?.uid
                val b = uid?.let {
                    MyCartTable(
                        0, it, bookId = book.id, quantity = 1
                    )
                }
                val order: MutableList<MyCartTable> = ArrayList()
                var cartOrder: MyCartTable
                Log.d("TAG", "${data}")

                CoroutineScope(Dispatchers.IO).launch {
                    var c = uid?.let { cartVm.get(it,book.id) }
                    Log.d("TAG", "$c")
                    if (c != null) {
                        if (c.isNotEmpty()){
                            Log.d("TAG", "im in notempty")
                            for (cart in c){
                                if (cart.bookId == book.id){
                                    Log.d("TAG", "im in notempty")
                                    if (uid != null) {
                                        cartVm.updateQty(uid, book.id, cart.quantity+ 1)

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
                Toast.makeText(requireContext(), getString(R.string.added_cart_successfully_message), Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvBookTrending.adapter = trendingAdapter
        binding.rvBookTrending.setHasFixedSize(true)


        //load all the book data from the firebase
        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            data.addAll(list)
            for(book in list) {
                if (book.trending == true){
                    trendingBook.add(book)
                }
            }
            trendingAdapter.submitList(trendingBook)
        }






        //search_bar
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                if(newText!!.isNotEmpty()){
                    displayBook.clear()

                    val search = newText.lowercase(Locale.getDefault())

                    for (book in data){
                        if(book.title.lowercase(Locale.getDefault()).contains(search)){
                            displayBook.add(book)
                        }
                        adapter.submitList(displayBook)
                        binding.rvBookList.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    adapter.submitList(data)
                    binding.rvBookList.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })


        return binding.root
    }

}