package com.mad.mad_bookworms.customer.explore

import android.annotation.SuppressLint
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
            data.add(Book("Title $i","Author $i", bookImage = R.drawable.book, 89.00))
        }

        displayBook.addAll(data)

        //Recycle View
        val adapter = RecyclerAdapter(displayBook)
        binding.rvBookList.adapter = adapter
        binding.rvBookList.setHasFixedSize(true)

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