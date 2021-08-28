package com.mad.mad_bookworms.customer.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.databinding.FragmentExploreBinding
import android.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.mad.mad_bookworms.customer.bookDetail.BookDetailActivity
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.viewModels.BookViewModel
import kotlinx.coroutines.awaitAll
import java.util.*
import kotlin.collections.ArrayList


class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val vm: BookViewModel by activityViewModels()

    private  lateinit var adapter: RecyclerAdapter
    private  lateinit var trendingAdapter: TrendingAdapter

//    private var layoutManager: RecyclerView.LayoutManager? = null
//    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder> ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater,container,false)

        val data: MutableList<Book> = ArrayList()
        val displayBook: MutableList<Book> = ArrayList()
        val trendingBook: MutableList<Book> = ArrayList()

        //Recycler View for book list
        adapter = RecyclerAdapter(){ holder, book ->
            // Item click
            holder.root.setOnClickListener {
//                Toast.makeText(context, "${trendingBook}", Toast.LENGTH_SHORT).show()
//                nav.navigate(R.id.updateFragment, bundleOf("id" to friend.id))

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
        }
        binding.rvBookTrending.adapter = trendingAdapter
        binding.rvBookTrending.setHasFixedSize(true)


        //load all the book data from the firebase
        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
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