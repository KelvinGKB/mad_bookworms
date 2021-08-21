package com.mad.mad_bookworms.customer.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.explore.classes.Book
import com.mad.mad_bookworms.databinding.FragmentExploreBinding
import android.widget.Toolbar
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import java.util.*
import kotlin.collections.ArrayList


class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding

//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder> ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater,container,false)

        val data: MutableList<Book> = ArrayList()
        val displayBook: MutableList<Book> = ArrayList()

        for(i :Int in 1..10){
            data.add(Book(
                "ID $i",
                "Title $i",
                "Author $i" ,
                "BOOK Description",
                89.00,
                200,
                "Romance",
                "ENG",
                243,
                bookImage = R.drawable.book))
        }

        displayBook.addAll(data)

        //Recycler View for book list
        val adapter = RecyclerAdapter(displayBook)
        binding.rvBookList.adapter = adapter
        adapter.setOnItemClickListener(object : RecyclerAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra("bookID",data[position].bookID)
                intent.putExtra("bookTitle",data[position].bookTitle)
                intent.putExtra("bookAuthor", data[position].bookAuthor)
                intent.putExtra("bookDescription", data[position].bookDescription)
                intent.putExtra("bookPrice",data[position].bookPrice)
                intent.putExtra("requiredPoint",data[position].requiredPoint)
                intent.putExtra("category",data[position].category)
                intent.putExtra("language",data[position].language)
                intent.putExtra("pages",data[position].pages)
                intent.putExtra("bookImage",data[position].bookImage)

                startActivity(intent)

            }

        })
        binding.rvBookList.setHasFixedSize(true)

        //Recycler View for Trending list
        val trendingAdapter = TrendingAdapter(data)
        binding.rvBookTrending.adapter = trendingAdapter
        binding.rvBookTrending.setHasFixedSize(true)

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
                        if(book.bookTitle.lowercase(Locale.getDefault()).contains(search)){
                            displayBook.add(book)
                        }
                        binding.rvBookList.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    displayBook.clear()
                    displayBook.addAll(data)
                    binding.rvBookList.adapter!!.notifyDataSetChanged()
                }
                return true
            }

        })


        return binding.root
    }


}